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
import com.baloise.open.thisorthat.dto.Image;
import com.baloise.open.thisorthat.dto.Survey;
import com.baloise.open.thisorthat.dto.VoteItem;
import com.baloise.open.thisorthat.exception.*;
import com.baloise.open.thisorthat.vote.SimpleVoteAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SurveyService {

    private final DatabaseService databaseInMemory;
    private final DatabaseService databaseMongo;

    private final SimpleVoteAlgorithm randomAlgorithm;


    public SurveyService(@Qualifier("inMemoryDatabaseService") DatabaseService databaseInMemory,
                         @Qualifier("mongoDatabaseService") DatabaseService databaseMongo,
                         SimpleVoteAlgorithm randomAlgorithm) {
        this.databaseInMemory = databaseInMemory;
        this.databaseMongo = databaseMongo;
        this.randomAlgorithm = randomAlgorithm;
    }

    public Survey createSurvey(String perspective) {
        log.info("create survey with perspective {}", perspective);
        long index = databaseMongo.surveyCount();
        String code = CodeCreator.createCode(index);
        log.info("code created: {}", code);
        databaseMongo.addSurvey(Survey.builder()
                .code(code)
                .creationDate(new Date())
                .started(false)
                .build());
        log.info("added empty survey to mongo");
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
        log.info("added survey to inMemory");
        return survey;
    }

    public void startSurvey(String surveyCode) {
        checkIfSurveyIsAlreadyStarted(surveyCode);
        databaseInMemory.startSurvey(surveyCode);
        randomAlgorithm.initialize(surveyCode);
        log.info("survey {} started and initialized", surveyCode);
    }

    public void stopSurvey(String surveyCode) {
        checkIfSurveyIsStopped(surveyCode);
        randomAlgorithm.calculateScoresAndStopSurvey(surveyCode);
        log.info("survey {} stopped", surveyCode);
    }

    public VoteResponse getVote(String surveyCode, String userId) {
        checkIfSurveyIsStopped(surveyCode);
        VoteItem voteItem = randomAlgorithm.getVote(surveyCode, userId);
        log.info("survey {} get vote from {} image1: {} image2: {}", surveyCode, userId, voteItem.getFile1().getId(), voteItem.getFile2().getId());
        return VoteResponse.builder()
                .id1(voteItem.getFile1().getId())
                .id2(voteItem.getFile2().getId())
                .perspective(databaseInMemory.getSurvey(surveyCode).getPerspective())
                .surveyIsRunning(true)
                .build();
    }

    public void setVote(String surveyCode, VoteRequest voteRequest, String userId) {
        checkIfSurveyIsStopped(surveyCode);
        randomAlgorithm.setVote(surveyCode, voteRequest.getWinner(), voteRequest.getLoser(), userId);
        log.info("survey {} set vote by {} winnerId: {} looserId: {}", surveyCode, userId, voteRequest.getWinner(), voteRequest.getLoser());
    }

    public ScoreResponse getScore(String surveyCode) {
        Survey survey;
        try {
            survey = databaseInMemory.getSurvey(surveyCode);
        } catch (SurveyNotFoundException error) {
            log.warn("survey not found in memory looking for in database surveyCode {}", surveyCode);
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
        log.info("survey {} getScore", surveyCode);
        return ScoreResponse.builder()
                .scores(scores)
                .numberOfVotes(survey.getVotes().size())
                .numberOfUsers(userIdSet.size())
                .surveyIsRunning(false)
                .perspective(survey.getPerspective())
                .build();
    }

    private Image getImageFromSurvey(Survey survey, String imageId) {
        log.info("survey {} get image {}", survey.getCode(), imageId);
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
        log.info("survey {} added image", surveyCode);
        return databaseInMemory.addImageToSurvey(surveyCode, image);
    }

    public void deleteSurvey(String surveyCode) {
        checkIfSurveyIsStopped(surveyCode);
        log.info("survey {} deleted", surveyCode);
        databaseInMemory.removeSurvey(surveyCode);
    }

    public void persistSurvey(String surveyCode) {
        Survey survey = databaseInMemory.getSurvey(surveyCode);
        if (survey.getStarted()) {
            log.error("survey {} is not stopped", survey.getCode());
            throw new DatabaseException("survey " + surveyCode + "is not stopped");
        }
        databaseMongo.updateSurvey(survey);
        log.info("survey {} persist", surveyCode);
    }

    private void checkIfSurveyIsAlreadyStarted(String surveyCode) {
        if (databaseInMemory.getSurvey(surveyCode).getStarted() != null && databaseInMemory.getSurvey(surveyCode).getStarted()) {
            log.warn("survey {} is not running", surveyCode);
            throw new SurveyAlreadyStartedException("survey " + surveyCode + " is already started");
        }
    }

    private void checkIfSurveyIsStopped(String surveyCode) {
        if (databaseInMemory.getSurvey(surveyCode).getStarted() != null && !databaseInMemory.getSurvey(surveyCode).getStarted()) {
            log.warn("survey {} is not running", surveyCode);
            throw new SurveyStoppedException("survey " + surveyCode + " is already stopped");
        }
    }

}
