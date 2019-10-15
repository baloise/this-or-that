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
package com.baloise.open.thisorthat.job;

import com.baloise.open.thisorthat.db.DatabaseService;
import com.baloise.open.thisorthat.dto.Survey;
import com.baloise.open.thisorthat.exception.DatabaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.Calendar;
import java.util.List;

@Slf4j
public class CleanupJob implements Runnable {

    @Autowired
    @Qualifier("inMemoryDatabaseService")
    private DatabaseService databaseService;

    @Override
    public void run() {
        log.info("ENTRY CleanupJob RUN()");
        Calendar yesterday = Calendar.getInstance();
        yesterday.add(Calendar.DAY_OF_MONTH, -7);
        List<Survey> oldSurveys = databaseService.getSurveysOlderThan(yesterday.getTime());
        log.info("Removing {} surveys", oldSurveys.size());
        for (Survey survey : oldSurveys) {
            try {
                databaseService.removeSurvey(survey.getCode());
            } catch (DatabaseException e) {
                log.error("Error when trying to remove survey with code {}. Message {}", survey.getCode(), e.getMessage());
            }
        }
        log.info("EXIT CleanupJob RUN()");
    }

}
