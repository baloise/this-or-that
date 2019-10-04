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
package com.baloise.open.thisorthat.server.db;

import com.baloise.open.thisorthat.server.dto.Image;
import com.baloise.open.thisorthat.server.dto.ScoreItem;
import com.baloise.open.thisorthat.server.dto.Survey;
import com.baloise.open.thisorthat.server.dto.Vote;
import com.baloise.open.thisorthat.server.exception.*;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.lt;
import static com.mongodb.client.model.Updates.*;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class MongoDatabaseService implements DatabaseService {

    private final static String MONGO_DB_NAME = "BV_THIS_OR_THAT";
    private final Logger LOGGER = LoggerFactory.getLogger("APPL." + MethodHandles.lookup().lookupClass());


    private final MongoCollection<Survey> surveys;

    MongoDatabaseService() {
        CodecRegistry pojoCodecRegistry = fromRegistries(MongoClient.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build()));
        MongoClient mongoClient;
        if (System.getProperty("thisorthat.environment") != null && System.getProperty("thisorthat.environment").equals("LOCAL")) {
            final String serverUrl = System.getProperty("thisorthat.mongodb.server.url");
            final String port = System.getProperty("thisorthat.mongodb.server.port");
            final String username = System.getProperty("thisorthat.mongodb.username");
            final String password = System.getProperty("thisorthat.mongodb.password");
            MongoCredential credentials = MongoCredential.createCredential(username, MONGO_DB_NAME, password.toCharArray());
            mongoClient = new MongoClient(new ServerAddress(serverUrl, Integer.valueOf(port)), credentials, MongoClientOptions.builder().build());
        } else {
            throw new IllegalStateException("not supported environment");
        }
        MongoDatabase database = mongoClient.getDatabase(MONGO_DB_NAME);
        database = database.withCodecRegistry(pojoCodecRegistry);
        surveys = database.getCollection("surveys", Survey.class);
    }

    @Override
    public long surveyCount() {
        return surveys.countDocuments();
    }

    @Override
    public void addSurvey(Survey survey) {
        surveys.insertOne(survey);
        LOGGER.info("added survey {}", survey.getCode());
    }

    public void updateSurvey(Survey survey) {
        //add images one per time because there is a request limit on the azure cloud of 2mb
        List<Image> images = new ArrayList<>(survey.getImages());
        List<ScoreItem> scores = new ArrayList<>(survey.getScores());
        Survey copyOfSurvey = Survey.builder()
                .creationDate(survey.getCreationDate())
                .code(survey.getCode())
                .started(survey.getStarted())
                .perspective(survey.getPerspective())
                .scores(new ArrayList<>())
                .votes(survey.getVotes())
                .images(new ArrayList<>())
                .build();
        surveys.replaceOne(eq("code", survey.getCode()), copyOfSurvey);
        for (Image image : images) {
            addImageToSurvey(survey.getCode(), image);
        }
        for (ScoreItem scoreItem : scores) {
            addScore(survey.getCode(), scoreItem);
        }
    }

    @Override
    public void removeSurvey(String surveyCode) {
        DeleteResult deleteResult = surveys.deleteOne(eq("code", surveyCode));
        deleteResultHandler(deleteResult);
        LOGGER.info("survey removed {}", surveyCode);
    }

    @Override
    public Survey getSurvey(String surveyCode) {
        Survey surveyDocument = surveys.find(eq("code", surveyCode)).first();
        if (surveyDocument != null) {
            return surveyDocument;
        }
        LOGGER.error("survey not found survey {}", surveyCode);
        throw new SurveyNotFoundException("survey not found");
    }

    @Override
    public String addImageToSurvey(String surveyCode, Image image) {
        UpdateResult updateResult = surveys.updateOne(eq("code", surveyCode), addToSet("images", image));
        updateResultHandler(updateResult);
        LOGGER.info("survey {} added image {}", surveyCode, image.getId());
        return image.getId();
    }

    @Override
    public void startSurvey(String surveyCode) {
        UpdateResult updateResult = surveys.updateOne(eq("code", surveyCode), set("started", true));
        updateResultHandler(updateResult);
        LOGGER.info("started survey {}", surveyCode);
    }

    @Override
    public void stopSurvey(String surveyCode) {
        UpdateResult updateResult = surveys.updateOne(eq("code", surveyCode), set("started", false));
        updateResultHandler(updateResult);
        LOGGER.info("stopped survey {}", surveyCode);
    }

    @Override
    public Image getImageFromSurvey(String surveyCode, String imageId) {
        Survey survey = getSurvey(surveyCode);
        return survey.getImages().stream()
                .filter(image -> image.getId().equals(imageId))
                .findFirst()
                .orElseThrow(() -> new ImageNotFoundException("survey " + surveyCode + " could not find image " + imageId));
    }

    @Override
    public void increaseScore(String surveyCode, String imageId) {
        int score = getScore(surveyCode, imageId).getScore() + 1;
        setScore(surveyCode, imageId, score);
        LOGGER.info("increased score for {} imageId {}", surveyCode, imageId);
    }

    @Override
    public void decreaseScore(String surveyCode, String imageId) {
        int score = getScore(surveyCode, imageId).getScore() - 1;
        setScore(surveyCode, imageId, score);
        LOGGER.info("decreased score for {} imageId {}", surveyCode, imageId);
    }

    @Override
    public void addScore(String surveyCode, ScoreItem scoreItem) {
        UpdateResult updateResult = surveys.updateOne(eq("code", surveyCode), addToSet("scores", scoreItem));
        updateResultHandler(updateResult);
        LOGGER.info("added score for {} survey image {} score {}", surveyCode, scoreItem.getImageId(), scoreItem.getScore());
    }

    @Override
    public void removeScore(String surveyCode, String imageId) {
        UpdateResult updateResult = surveys.updateOne(eq("code", surveyCode), pull("scores.image.id", imageId));
        updateResultHandler(updateResult);
        LOGGER.info("removed score imageId {} survey {}", imageId, surveyCode);
    }

    @Override
    public void persistVote(String surveyCode, Vote vote) {
        UpdateResult updateResult = surveys.updateOne(eq("code", surveyCode), addToSet("votes", vote));
        updateResultHandler(updateResult);
        LOGGER.info("saved vote for {} userId {}", surveyCode, vote.getUserId());
    }

    @Override
    public ScoreItem getScore(String surveyCode, String imageId) {
        Survey survey = getSurvey(surveyCode);
        return survey.getScores().stream()
                .filter(scoreItem -> scoreItem.getImageId().equals(imageId))
                .findFirst()
                .orElseThrow(() -> new ImageNotFoundException("survey " + surveyCode + " could not find image " + imageId));
    }

    @Override
    public void setScore(String surveyCode, String imageId, int scoreValue) {
        Survey survey = getSurvey(surveyCode);
        removeSurvey(surveyCode);
        survey.getScores().stream()
                .filter(scoreItem -> scoreItem.getImageId().equals(imageId))
                .findFirst()
                .orElseThrow(() -> new DatabaseException("survey " + surveyCode + " could not find score for " + imageId))
                .setScore(scoreValue);
        addSurvey(survey);
        LOGGER.info("set score {} on survey {} image {}", scoreValue, surveyCode, imageId);
    }

    @Override
    public List<Survey> getSurveysOlderThan(Date cutOffDate) {
        return surveys.find(lt("creationDate", cutOffDate)).into(
                new ArrayList<>());
    }

    private void updateResultHandler(UpdateResult updateResult) {
        if (updateResult.getMatchedCount() == 0) {
            LOGGER.error("update on mongoDB failed");
            throw new UpdateFailedException("update failed matched count=" + updateResult.getMatchedCount());
        }
    }

    private void deleteResultHandler(DeleteResult deleteResult) {
        if (deleteResult.getDeletedCount() == 0) {
            LOGGER.error("delete on mongoDB failed");
            throw new DeleteFailedException("delete failed delete count=" + deleteResult.getDeletedCount());
        }
    }
}
