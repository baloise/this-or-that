/*
 * Copyright 2018 Baloise Group
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.baloise.open.thisorthat.vote;

import com.baloise.open.thisorthat.db.DatabaseService;
import com.baloise.open.thisorthat.dto.*;
import com.baloise.open.thisorthat.exception.DatabaseException;

import java.util.*;
import java.util.stream.Collectors;

public class EloScoreAlgorithm extends AbstractAlgorithm {

    private static final double DEFAULT_WEIGHT_PER_VOTE = 40;

    private static final int DEFAULT_SCORE = 1000;

    private final AbstractImageSelectionAlgorithm imageSelectionAlgorithm;

    EloScoreAlgorithm(DatabaseService database) {
        super(database);
        imageSelectionAlgorithm = new ImageSelectionAlgorithmImpl(database);
    }

    @Override
    public void initialize(String surveyCode) throws DatabaseException {
        List<ScoreItem> scores = new ArrayList<>();
        database.getSurvey(surveyCode).getImages().forEach(image -> {
            ScoreItem score = ScoreItem.builder()
                    .imageId(image.getId())
                    .score(DEFAULT_SCORE) // every picture starts with a score of 1000
                    .build();
            scores.add(score);
        });
        database.getSurvey(surveyCode).setScores(scores);
        LOGGER.info("initialized algorithm for {}", database.getSurvey(surveyCode).getCode());
    }

    @Override
    public VoteItem getVote(String surveyCode, String userId) {
        Pair<Image> images = imageSelectionAlgorithm.getNextImagePair(surveyCode, userId);
        return VoteItem.builder()
                .file1(images.getT1())
                .file2(images.getT2())
                .build();
    }

    @Override
    protected List<ScoreItem> calculateScores(String surveyCode) {
        LOGGER.info("ENTRY calculateScore(surveyCode={})", surveyCode);
        Survey survey = database.getSurvey(surveyCode);
        Set<String> usersIds = survey.getVotes().stream().map(Vote::getUserId).collect(Collectors.toSet());
        Map<String, Integer> imageScores = new HashMap<>();

        for (String userId : usersIds) {
            List<Vote> votes = survey.getVotes().stream()
                    .filter(v -> v.getUserId().equals(userId)).collect(Collectors.toList());
            int weight = Math.max(1, (int) (getWeight(survey, userId, DEFAULT_WEIGHT_PER_VOTE) + 0.5));
            votes.forEach(v -> {
                imageScores.putIfAbsent(v.getLoser(), DEFAULT_SCORE);
                imageScores.putIfAbsent(v.getWinner(), DEFAULT_SCORE);
                int oldScoreWinner = imageScores.get(v.getWinner());
                int oldScoreLooser = imageScores.get(v.getLoser());
                int newScoreWinner = EloScoreCalculator.calculateNewScore(oldScoreWinner, oldScoreLooser, weight, Outcome.WIN);
                int newScoreLooser = EloScoreCalculator.calculateNewScore(oldScoreLooser, oldScoreWinner, weight, Outcome.LOSS);
                imageScores.put(v.getWinner(), newScoreWinner);
                imageScores.put(v.getLoser(), newScoreLooser);
            });
        }

        final List<ScoreItem> list = imageScores.keySet().stream()
                .map(imageId -> ScoreItem.builder()
                        .imageId(imageId)
                        .score(imageScores.get(imageId))
                        .build())
                .collect(Collectors.toList());
        LOGGER.info("EXIT calculateScore(scores={})", list);
        return list;
    }

}
