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
import com.baloise.open.thisorthat.db.DatabaseServiceProvider;
import com.baloise.open.thisorthat.dto.Survey;
import com.baloise.open.thisorthat.exception.DatabaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.Calendar;
import java.util.List;

public class CleanupJob implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger("APPL." + MethodHandles.lookup().lookupClass());

    @Override
    public void run() {
        LOGGER.info("ENTRY CleanupJob RUN()");
        DatabaseService databaseService = DatabaseServiceProvider.getInMemoryDBServiceInstance();
        Calendar yesterday = Calendar.getInstance();
        yesterday.add(Calendar.DAY_OF_MONTH, -7);
        List<Survey> oldSurveys = databaseService.getSurveysOlderThan(yesterday.getTime());
        LOGGER.info("Removing {} surveys", oldSurveys.size());
        for (Survey survey : oldSurveys) {
            try {
                databaseService.removeSurvey(survey.getCode());
            } catch (DatabaseException e) {
                LOGGER.error("Error when trying to remove survey with code {}. Message {}", survey.getCode(), e.getMessage());
            }
        }
        LOGGER.info("EXIT CleanupJob RUN()");
    }

}
