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

import java.util.Date;
import java.util.List;

public interface DatabaseService {

    long surveyCount();

    void updateSurvey(Survey survey);

    void addSurvey(Survey survey);

    void removeSurvey(String code);

    Survey getSurvey(String code);

    String addImageToSurvey(String surveyCode, Image image);

    void startSurvey(String surveyCode);

    void stopSurvey(String surveyCode);

    Image getImageFromSurvey(String surveyCode, String imageId);

    void increaseScore(String surveyCode, String imageId);

    void decreaseScore(String surveyCode, String imageId);

    void addScore(String surveyCode, ScoreItem scoreItem);

    void removeScore(String surveyCode, String imageId);

    void persistVote(String surveyCode, Vote build);

    ScoreItem getScore(String surveyCode, String imageId);

    void setScore(String surveyCode, String imageId, int score);

    List<Survey> getSurveysOlderThan(Date cutOffDate);

}
