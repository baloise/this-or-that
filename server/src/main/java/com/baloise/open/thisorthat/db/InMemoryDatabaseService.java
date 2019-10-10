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
package com.baloise.open.thisorthat.db;

import com.baloise.open.thisorthat.dto.Image;
import com.baloise.open.thisorthat.dto.ScoreItem;
import com.baloise.open.thisorthat.dto.Survey;
import com.baloise.open.thisorthat.dto.Vote;
import com.baloise.open.thisorthat.exception.DeleteFailedException;
import com.baloise.open.thisorthat.exception.ImageNotFoundException;
import com.baloise.open.thisorthat.exception.SurveyNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class InMemoryDatabaseService implements DatabaseService {

    private static final CopyOnWriteArrayList<Survey> surveys = new CopyOnWriteArrayList<>();
    private final Logger LOGGER = LoggerFactory.getLogger("APPL." + MethodHandles.lookup().lookupClass());


    @Override
    public long surveyCount() {
        return surveys.size();
    }

    @Override
    public void updateSurvey(Survey survey) {
        int index = 0;
        for (int i = 0; i < surveys.size(); i++) {
            if (surveys.get(i).getCode().equals(survey.getCode())) {
                index = i;
            }
        }
        surveys.set(index, survey);
    }

    public void addSurvey(Survey survey) {
        surveys.add(survey);
        LOGGER.info("added survey {}", survey.getCode());
    }

    public void removeSurvey(String code) {
        boolean removed = surveys.removeIf(survey -> survey.getCode().equals(code));
        if (removed) {
            LOGGER.info("survey deleted {}", code);
        } else {
            LOGGER.error("survey with {} could not be deleted", code);
            throw new DeleteFailedException("survey " + code + " could not be deleted");
        }
    }

    public Survey getSurvey(String code) {
        Optional<Survey> surveyOptional = surveys.stream().filter(survey -> survey.getCode().equals(code)).findFirst();
        if (surveyOptional.isPresent()) {
            return surveyOptional.get();
        } else {
            LOGGER.error("survey {} not found", code);
            throw new SurveyNotFoundException("survey " + code + " not found");
        }
    }

    public String addImageToSurvey(String surveyCode, Image image) {
        Survey survey = getSurvey(surveyCode);
        survey.getImages().add(image);
        String id = Integer.toString(survey.getImages().indexOf(image));
        image.setId(id);
        LOGGER.info("added image {} to {}", image.getId(), surveyCode);
        return id;
    }

    public void startSurvey(String surveyCode) {
        getSurvey(surveyCode).setStarted(true);
        LOGGER.info("started survey {}", surveyCode);
    }

    public void stopSurvey(String surveyCode) {
        getSurvey(surveyCode).setStarted(false);
        LOGGER.info("closed survey {}", surveyCode);
    }

    public Image getImageFromSurvey(String surveyCode, String imageId) {
        Survey survey = getSurvey(surveyCode);
        return survey.getImages().stream()
                .filter(image -> image.getId().equals(imageId))
                .findFirst()
                .orElseThrow(() -> new ImageNotFoundException("survey" + surveyCode + " image " + imageId + "not found "));
    }

    @Override
    public List<Survey> getSurveysOlderThan(Date cutOffDate) {
        return surveys.stream().filter(s -> s.getCreationDate().before(cutOffDate)).collect(Collectors.toList());
    }

    @Override
    public void addScore(String surveyCode, ScoreItem scoreItem) {
        getSurvey(surveyCode).getScores().add(scoreItem);
    }

    public void persistVote(String surveyCode, Vote vote) {
        Survey survey = getSurvey(surveyCode);
        survey.getVotes().add(vote);
    }

}
