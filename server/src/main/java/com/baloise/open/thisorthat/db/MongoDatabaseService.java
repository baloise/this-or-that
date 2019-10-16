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
import com.baloise.open.thisorthat.dto.Survey;
import com.baloise.open.thisorthat.exception.DeleteFailedException;
import com.baloise.open.thisorthat.exception.ImageNotFoundException;
import com.baloise.open.thisorthat.exception.SurveyNotFoundException;
import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import lombok.extern.slf4j.Slf4j;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.mongodb.client.model.Filters.eq;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

@Repository
@Slf4j
public class MongoDatabaseService {

    private final static String MONGO_DB_NAME = "BV_THIS_OR_THAT";

    private final MongoCollection<Survey> surveys;
    private final MongoCollection<Image> images;

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
        images = database.getCollection("images", Image.class);
    }

    public long surveyCount() {
        return surveys.countDocuments();
    }

    public void initializeSurvey(Survey survey) {
        surveys.insertOne(survey);
        log.info("added survey {}", survey.getId());
    }

    public void persistSurvey(Survey survey) {
        List<Image> images = survey.getImages().stream()
                .map(image -> Image.builder()
                        .id(survey.getId() + "_" + image.getId())
                        .build())
                .collect(Collectors.toList());
        Survey copyOfSurvey = survey.toBuilder()
                .images(images)
                .build();
        surveys.replaceOne(eq("_id", survey.getId()), copyOfSurvey);
        for (Image image : images) {
            addImage(image);
        }
    }

    public void removeSurvey(String surveyCode) {
        DeleteResult deleteResult = surveys.deleteOne(eq("_id", surveyCode));
        deleteResultHandler(deleteResult);
        log.info("survey removed {}", surveyCode);
    }

    public Survey getSurvey(String surveyCode) {
        Survey surveyDocument = surveys.find(eq("_id", surveyCode)).first();
        if (surveyDocument != null) {
            List<Image> images = getImagesFromSurvey(surveyDocument.getImages());
            return surveyDocument.toBuilder()
                    .images(images)
                    .build();
        }
        log.error("survey not found survey {}", surveyCode);
        throw new SurveyNotFoundException("survey not found");
    }

    private List<Image> getImagesFromSurvey(List<Image> images) {
        List<Image> tempImageList = new ArrayList<>();
        images.forEach(image -> {
            Image tempImage = this.images.find(eq("_id", image.getId())).first();
            if (tempImage == null) {
                throw new ImageNotFoundException("image: " + image.getId() + " not found");
            }
            tempImageList.add(tempImage);
        });
        return tempImageList;
    }

    private void addImage(Image image) {
        images.insertOne(image);
        log.info("added image {}", image.getId());
    }

    private void deleteResultHandler(DeleteResult deleteResult) {
        if (deleteResult.getDeletedCount() == 0) {
            log.error("delete on mongoDB failed");
            throw new DeleteFailedException("delete failed delete count=" + deleteResult.getDeletedCount());
        }
    }
}
