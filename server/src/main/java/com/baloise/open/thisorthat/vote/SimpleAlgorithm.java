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
import com.baloise.open.thisorthat.vote.image.selection.AbstractImageSelectionAlgorithm;
import com.baloise.open.thisorthat.vote.image.selection.ImageSelectionAlgorithmImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.*;
import java.util.stream.Collectors;

public class SimpleAlgorithm extends AbstractAlgorithm {

    private static final Logger LOGGER = LoggerFactory.getLogger("APPL." + MethodHandles.lookup().lookupClass());

    private final AbstractImageSelectionAlgorithm imageSelectionAlgorithm;

    public SimpleAlgorithm(DatabaseService database) {
        super(database);
        imageSelectionAlgorithm = new ImageSelectionAlgorithmImpl(database);
    }

    public void initialize(String surveyCode) {
        List<ScoreItem> scores = new ArrayList<>();
        database.getSurvey(surveyCode).getImages().forEach(image -> {
            ScoreItem score = ScoreItem.builder()
                    .imageId(image.getId())
                    .score(0)
                    .build();
            scores.add(score);
        });
        for (ScoreItem scoreItem : scores) {
            database.addScore(surveyCode, scoreItem);
        }
        LOGGER.info("initialized algorithm for {}", database.getSurvey(surveyCode).getCode());
    }

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
        LOGGER.info("EXIT calculateScore(scores={})", list);
        return list;
    }

}
