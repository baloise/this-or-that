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

import com.baloise.open.thisorthat.db.InMemoryDatabase;
import com.baloise.open.thisorthat.dto.*;
import com.baloise.open.thisorthat.exception.SurveyStoppedException;
import com.baloise.open.thisorthat.vote.image.selection.ImageSelectionAlgorithm;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Log4j2
public class SimpleVoteAlgorithm implements VoteAlgorithm {

    private static final double DEFAULT_WEIGHT_PER_VOTE = 10.0;

    private final ImageSelectionAlgorithm imageSelectionAlgorithm;
    private final InMemoryDatabase inMemoryDatabase;

    public SimpleVoteAlgorithm(ImageSelectionAlgorithm imageSelectionAlgorithm,
                               InMemoryDatabase inMemoryDatabase) {
        this.imageSelectionAlgorithm = imageSelectionAlgorithm;
        this.inMemoryDatabase = inMemoryDatabase;
    }

    @Override
    public void initialize(String surveyCode) {
        List<ScoreItem> scores = new ArrayList<>();
        inMemoryDatabase.getSurvey(surveyCode).getImages().forEach(image -> {
            ScoreItem score = ScoreItem.builder()
                    .imageId(image.getId())
                    .score(0)
                    .build();
            scores.add(score);
        });
        for (ScoreItem scoreItem : scores) {
            inMemoryDatabase.addScore(surveyCode, scoreItem);
        }
        log.info("initialized algorithm for {}", inMemoryDatabase.getSurvey(surveyCode).getId());
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
    public void setVote(String surveyCode, String imageIdWinner, String imageIdLooser, String userId) {
        inMemoryDatabase.persistVote(surveyCode, Vote.builder().loser(imageIdLooser).winner(imageIdWinner).userId(userId).build());
        log.info("voted for {} winner {} looser {}", surveyCode, imageIdWinner, imageIdLooser);
    }

    @Override
    public List<ScoreItem> calculateScores(String surveyCode) {
        log.info("ENTRY calculateScore(surveyCode={})", surveyCode);
        Survey survey = inMemoryDatabase.getSurvey(surveyCode);
        Set<String> usersIds = survey.getVotes().stream().map(Vote::getUserId).collect(Collectors.toSet());
        Map<String, Double> imageScores = new HashMap<>();

        for (String userId : usersIds) {
            List<Vote> votes = survey.getVotes().stream()
                    .filter(v -> v.getUserId().equals(userId)).collect(Collectors.toList());
            double weight = getWeight(survey, userId);
            votes.forEach(v -> {
                imageScores.putIfAbsent(v.getLoser(), 0.0);
                imageScores.putIfAbsent(v.getWinner(), 0.0);
                imageScores.put(v.getLoser(), imageScores.get(v.getLoser()) - weight);
                imageScores.put(v.getWinner(), imageScores.get(v.getWinner()) + weight);
            });
        }

        final List<ScoreItem> list = imageScores.keySet().stream()
                .map(imageId -> ScoreItem.builder()
                        .imageId(imageId)
                        .score(imageScores.get(imageId).intValue())
                        .build())
                .collect(Collectors.toList());
        log.info("EXIT calculateScore(scores={})", list);
        return list;
    }

    public void calculateScoresAndStopSurvey(String surveyCode) {
        Survey survey = inMemoryDatabase.getSurvey(surveyCode);
        if (!survey.getStarted()) {
            throw new SurveyStoppedException("survey " + surveyCode + " already stopped");
        }
        survey.setScores(calculateScores(surveyCode));
        inMemoryDatabase.stopSurvey(surveyCode);
    }

    /**
     * The weight of a users vote is normalized to the number of images
     */
    private double getWeight(Survey survey, String userId) {
        List<Vote> votes = survey.getVotes().stream()
                .filter(v -> v.getUserId().equals(userId)).collect(Collectors.toList());
        if ((votes.size() * 2) <= survey.getImages().size()) { // if votes * 2 are less or equal than number of images each picture has been evaluated once at max!
            return DEFAULT_WEIGHT_PER_VOTE;
        }
        return DEFAULT_WEIGHT_PER_VOTE * (double) (survey.getImages().size()) / (double) (votes.size() * 2);
    }

}
