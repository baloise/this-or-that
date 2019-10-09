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
import com.baloise.open.thisorthat.dto.ScoreItem;
import com.baloise.open.thisorthat.dto.Survey;
import com.baloise.open.thisorthat.dto.Vote;
import com.baloise.open.thisorthat.dto.VoteItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.stream.Collectors;

abstract class AbstractAlgorithm {

    static final Logger LOGGER = LoggerFactory.getLogger("APPL." + MethodHandles.lookup().lookupClass());

    final DatabaseService database;

    AbstractAlgorithm(DatabaseService database) {
        this.database = database;
    }

    public abstract void initialize(String surveyCode);

    public abstract VoteItem getVote(String surveyCode, String userId);

    protected abstract List<ScoreItem> calculateScores(String surveyCode);

    /**
     * The weight of a users vote is normalized to the number of images
     */
    double getWeight(Survey survey, String userId, double defaultWeightPerVote) {
        List<Vote> votes = survey.getVotes().stream()
                .filter(v -> v.getUserId().equals(userId)).collect(Collectors.toList());
        if ((votes.size() * 2) <= survey.getImages().size()) { // if votes * 2 are less or equal than number of images each picture has been evaluated once at max!
            return defaultWeightPerVote;
        }
        return defaultWeightPerVote * (double) (survey.getImages().size()) / (double) (votes.size() * 2);
    }

    public void setVote(String surveyCode, String imageIdWinner, String imageIdLooser, String userId) {
        database.persistVote(surveyCode, Vote.builder().loser(imageIdLooser).winner(imageIdWinner).userId(userId).build());
        LOGGER.info("voted for {} winner {} looser {}", surveyCode, imageIdWinner, imageIdLooser);
    }

    public Survey getSurvey(String surveyCode) {
        return database.getSurvey(surveyCode);
    }

    public void calculateScoresAndStopSurvey(String surveyCode) {
        Survey survey = database.getSurvey(surveyCode);
        survey.setScores(calculateScores(surveyCode));
        database.stopSurvey(surveyCode);
    }

}
