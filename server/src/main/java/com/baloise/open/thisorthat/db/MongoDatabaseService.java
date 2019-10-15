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
import com.baloise.open.thisorthat.exception.UpdateFailedException;
import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import lombok.extern.slf4j.Slf4j;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.lt;
import static com.mongodb.client.model.Updates.addToSet;
import static com.mongodb.client.model.Updates.set;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

@Repository
@Slf4j
public class MongoDatabaseService implements DatabaseService {

    private final static String MONGO_DB_NAME = "BV_THIS_OR_THAT";

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
            mongoClient = new MongoClient(new ServerAddress(serverUrl, Integer.parseInt(port)), credentials, MongoClientOptions.builder().build());
        } else {
            String mongoDBConnectionString = System.getenv("MongoDBConnectionString");
            MongoClientURI connectionString = new MongoClientURI(mongoDBConnectionString);
            mongoClient = new MongoClient(connectionString);
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
        log.info("added survey {}", survey.getCode());
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
        log.info("survey removed {}", surveyCode);
    }

    @Override
    public Survey getSurvey(String surveyCode) {
        Survey surveyDocument = surveys.find(eq("code", surveyCode)).first();
        if (surveyDocument != null) {
            return surveyDocument;
        }
        log.error("survey not found survey {}", surveyCode);
        throw new SurveyNotFoundException("survey not found");
    }

    @Override
    public String addImageToSurvey(String surveyCode, Image image) {
        UpdateResult updateResult = surveys.updateOne(eq("code", surveyCode), addToSet("images", image));
        updateResultHandler(updateResult);
        log.info("survey {} added image {}", surveyCode, image.getId());
        return image.getId();
    }

    @Override
    public void startSurvey(String surveyCode) {
        UpdateResult updateResult = surveys.updateOne(eq("code", surveyCode), set("started", true));
        updateResultHandler(updateResult);
        log.info("started survey {}", surveyCode);
    }

    @Override
    public void stopSurvey(String surveyCode) {
        UpdateResult updateResult = surveys.updateOne(eq("code", surveyCode), set("started", false));
        updateResultHandler(updateResult);
        log.info("stopped survey {}", surveyCode);
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
    public void addScore(String surveyCode, ScoreItem scoreItem) {
        UpdateResult updateResult = surveys.updateOne(eq("code", surveyCode), addToSet("scores", scoreItem));
        updateResultHandler(updateResult);
        log.info("added score for {} survey image {} score {}", surveyCode, scoreItem.getImageId(), scoreItem.getScore());
    }

    @Override
    public void persistVote(String surveyCode, Vote vote) {
        UpdateResult updateResult = surveys.updateOne(eq("code", surveyCode), addToSet("votes", vote));
        updateResultHandler(updateResult);
        log.info("saved vote for {} userId {}", surveyCode, vote.getUserId());
    }

    @Override
    public List<Survey> getSurveysOlderThan(Date cutOffDate) {
        return surveys.find(lt("creationDate", cutOffDate)).into(
                new ArrayList<>());
    }

    private void updateResultHandler(UpdateResult updateResult) {
        if (updateResult.getMatchedCount() == 0) {
            log.error("update on mongoDB failed");
            throw new UpdateFailedException("update failed matched count=" + updateResult.getMatchedCount());
        }
    }

    private void deleteResultHandler(DeleteResult deleteResult) {
        if (deleteResult.getDeletedCount() == 0) {
            log.error("delete on mongoDB failed");
            throw new DeleteFailedException("delete failed delete count=" + deleteResult.getDeletedCount());
        }
    }
}
