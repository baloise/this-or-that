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
package com.baloise.open.thisorthat.service;

import com.baloise.open.thisorthat.api.dto.Score;
import com.baloise.open.thisorthat.api.dto.ScoreResponse;
import com.baloise.open.thisorthat.api.dto.VoteRequest;
import com.baloise.open.thisorthat.api.dto.VoteResponse;
import com.baloise.open.thisorthat.db.DatabaseService;
import com.baloise.open.thisorthat.db.DatabaseServiceProvider;
import com.baloise.open.thisorthat.dto.Image;
import com.baloise.open.thisorthat.dto.Survey;
import com.baloise.open.thisorthat.dto.VoteItem;
import com.baloise.open.thisorthat.exception.*;
import com.baloise.open.thisorthat.vote.SimpleAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.*;
import java.util.stream.Collectors;

public class SurveyService {

    private final DatabaseService databaseInMemory;


    private final SimpleAlgorithm randomAlgorithm;
    private final Logger LOGGER = LoggerFactory.getLogger("APPL." + MethodHandles.lookup().lookupClass());
    private final DatabaseService databaseMongo;

    public SurveyService() {
        databaseInMemory = DatabaseServiceProvider.getInMemoryDBServiceInstance();
        databaseMongo = DatabaseServiceProvider.getMongoDBServiceInstance();
        randomAlgorithm = new SimpleAlgorithm(databaseInMemory);
    }

    public Survey createSurvey(String perspective) {
        LOGGER.info("create survey with perspective {}", perspective);
        long index = databaseMongo.surveyCount();
        String code = CodeCreator.createCode(index);
        LOGGER.info("code created: {}", code);
        databaseMongo.addSurvey(Survey.builder()
                .code(code)
                .creationDate(new Date())
                .started(false)
                .build());
        LOGGER.info("added empty survey to mongo");
        Survey survey = Survey.builder()
                .code(code)
                .creationDate(new Date())
                .perspective(perspective)
                .images(new ArrayList<>())
                .scores(new ArrayList<>())
                .votes(new ArrayList<>())
                .started(false)
                .build();
        databaseInMemory.addSurvey(survey);
        LOGGER.info("added survey to inMemory");
        return survey;
    }

    public void startSurvey(String surveyCode) {
        databaseInMemory.startSurvey(surveyCode);
        randomAlgorithm.initialize(surveyCode);
        LOGGER.info("survey {} started and initialized", surveyCode);
    }

    public void stopSurvey(String surveyCode) {
        randomAlgorithm.calculateScoresAndStopSurvey(surveyCode);
        LOGGER.info("survey {} stopped", surveyCode);
    }

    public VoteResponse getVote(String surveyCode, String userId) {
        if (databaseInMemory.getSurvey(surveyCode).getStarted() == null || !databaseInMemory.getSurvey(surveyCode).getStarted()) {
            LOGGER.warn("survey {} is not running", surveyCode);
            return VoteResponse.builder()
                    .surveyIsRunning(false)
                    .build();
        }
        VoteItem voteItem = randomAlgorithm.getVote(surveyCode, userId);
        LOGGER.info("survey {} get vote from {} image1: {} image2: {}", surveyCode, userId, voteItem.getFile1().getId(), voteItem.getFile2().getId());
        return VoteResponse.builder()
                .id1(voteItem.getFile1().getId())
                .id2(voteItem.getFile2().getId())
                .perspective(databaseInMemory.getSurvey(surveyCode).getPerspective())
                .surveyIsRunning(true)
                .build();
    }

    public void setVote(String surveyCode, VoteRequest voteRequest, String userId) {
        if (databaseInMemory.getSurvey(surveyCode).getStarted() != null && databaseInMemory.getSurvey(surveyCode).getStarted()) {
            randomAlgorithm.setVote(surveyCode, voteRequest.getWinner(), voteRequest.getLoser(), userId);
            LOGGER.info("survey {} set vote by {} winnerId: {} looserId: {}", surveyCode, userId, voteRequest.getWinner(), voteRequest.getLoser());
        }
    }

    public ScoreResponse getScore(String surveyCode) {
        Survey survey;
        try {
            survey = databaseInMemory.getSurvey(surveyCode);
        } catch (SurveyNotFoundException error) {
            LOGGER.warn("survey not found in memory looking for in database surveyCode {}", surveyCode);
            survey = databaseMongo.getSurvey(surveyCode);
        }
        if (survey.getStarted() != null && survey.getStarted()) { // survey still running. Scores are not available yet!
            throw new SurveyStillRunningException("survey " + surveyCode + " is still running:");
        }
        //extract numberOfUsers for Response
        Set<String> userIdSet = new HashSet<>();

        if (survey.getVotes() == null || survey.getScores() == null) {
            throw new SurveyIncompleteException("Incomplete survey");
        }
        survey.getVotes().forEach(vote -> userIdSet.add(vote.getUserId()));

        Survey finalSurvey = survey;
        List<Score> scores = survey.getScores().stream()
                .map(scoreItem -> {
                    Image file = getImageFromSurvey(finalSurvey, scoreItem.getImageId());
                    return Score.builder()
                            .score(scoreItem.getScore())
                            .imageId(file.getId())
                            .build();
                })
                .sorted((score1, score2) -> score2.getScore().compareTo(score1.getScore()))
                .collect(Collectors.toList());
        LOGGER.info("survey {} getScore", surveyCode);
        return ScoreResponse.builder()
                .scores(scores)
                .numberOfVotes(survey.getVotes().size())
                .numberOfUsers(userIdSet.size())
                .surveyIsRunning(false)
                .perspective(survey.getPerspective())
                .build();
    }

    private Image getImageFromSurvey(Survey survey, String imageId) {
        LOGGER.info("survey {} get image {}", survey.getCode(), imageId);
        return survey.getImages().stream()
                .filter(image -> image.getId().equals(imageId))
                .findFirst()
                .orElseThrow(() -> new ImageNotFoundException("survey " + survey.getCode() + " image " + imageId + " not found"));
    }

    public Image getImageFromSurvey(String surveyCode, String imageId) {
        Survey survey = databaseInMemory.getSurvey(surveyCode);
        return getImageFromSurvey(survey, imageId);
    }

    public String addImageToSurvey(String surveyCode, Image image) {
        LOGGER.info("survey {} added image", surveyCode);
        return databaseInMemory.addImageToSurvey(surveyCode, image);
    }

    public void deleteSurvey(String surveyCode) {
        LOGGER.info("survey {} deleted", surveyCode);
        databaseInMemory.removeSurvey(surveyCode);
    }

    public void persistSurvey(String surveyCode) {
        Survey survey = databaseInMemory.getSurvey(surveyCode);
        if (survey.getStarted()) {
            LOGGER.error("survey {} is not stopped", survey.getCode());
            throw new DatabaseException("survey " + surveyCode + "is not stopped");
        }
        databaseMongo.updateSurvey(survey);
        LOGGER.info("survey {} persist", surveyCode);
    }

}
