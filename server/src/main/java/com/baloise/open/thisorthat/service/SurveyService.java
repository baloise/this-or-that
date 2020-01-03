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
import com.baloise.open.thisorthat.db.InMemoryDatabase;
import com.baloise.open.thisorthat.dto.Image;
import com.baloise.open.thisorthat.dto.Survey;
import com.baloise.open.thisorthat.dto.VoteItem;
import com.baloise.open.thisorthat.exception.*;
import com.baloise.open.thisorthat.vote.SimpleVoteAlgorithm;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Log4j2
public class SurveyService {

    private final InMemoryDatabase inMemoryDatabase;

    private final SimpleVoteAlgorithm randomAlgorithm;

    public SurveyService(InMemoryDatabase inMemoryDatabase,
                         SimpleVoteAlgorithm randomAlgorithm) {
        this.inMemoryDatabase = inMemoryDatabase;
        this.randomAlgorithm = randomAlgorithm;
    }

    public Survey createSurvey(String perspective) {
        log.info("create survey with perspective {}", perspective);
        long index = inMemoryDatabase.surveyCount();
        String code = CodeCreator.createCode(index);
        log.info("code created: {}", code);
        Survey survey = Survey.builder()
                .id(code)
                .creationDate(new Date())
                .perspective(perspective)
                .images(new ArrayList<>())
                .scores(new ArrayList<>())
                .votes(new ArrayList<>())
                .started(false)
                .build();
        inMemoryDatabase.addSurvey(survey);
        log.info("added survey to inMemory");
        return survey;
    }

    public void startSurvey(String surveyCode) {
        checkIfSurveyIsAlreadyStarted(surveyCode);
        inMemoryDatabase.startSurvey(surveyCode);
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
                .perspective(inMemoryDatabase.getSurvey(surveyCode).getPerspective())
                .surveyIsRunning(true)
                .build();
    }

    public void setVote(String surveyCode, VoteRequest voteRequest, String userId) {
        checkIfSurveyIsStopped(surveyCode);
        randomAlgorithm.setVote(surveyCode, voteRequest.getWinner(), voteRequest.getLoser(), userId);
        log.info("survey {} set vote by {} winnerId: {} looserId: {}", surveyCode, userId, voteRequest.getWinner(), voteRequest.getLoser());
    }

    public ScoreResponse getScore(String surveyCode) {
        Survey survey = getSurvey(surveyCode);
        if (survey.getStarted() != null && survey.getStarted()) { // survey still running. Scores are not available yet!
            throw new SurveyStillRunningException("survey " + surveyCode + " is still running:");
        }
        //extract numberOfUsers for Response
        Set<String> userIdSet = new HashSet<>();

        if (survey.getVotes() == null || survey.getScores() == null) {
            throw new SurveyIncompleteException("Incomplete survey");
        }
        survey.getVotes().forEach(vote -> userIdSet.add(vote.getUserId()));

        List<Score> scores = survey.getScores().stream()
                .map(scoreItem -> {
                    Image file = getImageFromSurvey(survey, scoreItem.getImageId());
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

    private Survey getSurvey(String surveyCode) {
        return inMemoryDatabase.getSurvey(surveyCode);
    }

    private Image getImageFromSurvey(Survey survey, String imageId) {
        log.info("survey {} get image {}", survey.getId(), imageId);
        return survey.getImages().stream()
                .filter(image -> image.getId().equals(imageId))
                .findFirst()
                .orElseThrow(() -> new ImageNotFoundException("survey " + survey.getId() + " image " + imageId + " not found"));
    }

    public Image getImageFromSurvey(String surveyCode, String imageId) {
        Survey survey = getSurvey(surveyCode);
        return getImageFromSurvey(survey, imageId);
    }

    public String addImageToSurvey(String surveyCode, Image image) {
        log.info("survey {} added image", surveyCode);
        return inMemoryDatabase.addImageToSurvey(surveyCode, image);
    }

    public void deleteSurvey(String surveyCode) {
        checkIfSurveyIsStopped(surveyCode);
        log.info("survey {} deleted", surveyCode);
        inMemoryDatabase.removeSurvey(surveyCode);
    }

    private void checkIfSurveyIsAlreadyStarted(String surveyCode) {
        if (inMemoryDatabase.getSurvey(surveyCode).getStarted() != null && inMemoryDatabase.getSurvey(surveyCode).getStarted()) {
            log.warn("survey {} is not running", surveyCode);
            throw new SurveyAlreadyStartedException("survey " + surveyCode + " is already started");
        }
    }

    private void checkIfSurveyIsStopped(String surveyCode) {
        if (inMemoryDatabase.getSurvey(surveyCode).getStarted() != null && !inMemoryDatabase.getSurvey(surveyCode).getStarted()) {
            log.warn("survey {} is not running", surveyCode);
            throw new SurveyStoppedException("survey " + surveyCode + " is already stopped");
        }
    }

}
