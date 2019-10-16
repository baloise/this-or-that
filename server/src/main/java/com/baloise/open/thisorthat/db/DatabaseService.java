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

import java.util.Date;
import java.util.List;

public interface DatabaseService {

    long surveyCount();

    long imageCount();

    void updateSurvey(Survey survey);

    void addSurvey(Survey survey);

    void removeSurvey(String code);

    Survey getSurvey(String code);

    void addImageToSurvey(String surveyCode, Image image);

    Image getImage(String imageId);

    void startSurvey(String surveyCode);

    void stopSurvey(String surveyCode);

    String getImageIdFromSurvey(String surveyCode, String imageId);

    void addScore(String surveyCode, ScoreItem scoreItem);

    void persistVote(String surveyCode, Vote build);

    List<Survey> getSurveysOlderThan(Date cutOffDate);

}
