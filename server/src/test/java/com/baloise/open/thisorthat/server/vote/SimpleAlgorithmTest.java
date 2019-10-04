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
package com.baloise.open.thisorthat.server.vote;


import com.baloise.open.thisorthat.server.db.DatabaseService;
import com.baloise.open.thisorthat.server.db.InMemoryDatabaseService;
import com.baloise.open.thisorthat.server.dto.Image;
import com.baloise.open.thisorthat.server.dto.ScoreItem;
import com.baloise.open.thisorthat.server.dto.Survey;
import com.baloise.open.thisorthat.server.dto.Vote;
import org.jglue.cdiunit.CdiRunner;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;

import static org.junit.Assert.assertTrue;

@Ignore
@RunWith(CdiRunner.class)
public class SimpleAlgorithmTest {

    private final DatabaseService databaseService = new InMemoryDatabaseService();
    private SimpleAlgorithm simpleAlgorithm;

    @Test
    public void calculateScoresAndStopSurvey_simple_test() {
        Survey survey = new Survey();

        survey.setImages(Arrays.asList(Image.builder().id("1").build(), Image.builder().id("2").build()));

        survey.setVotes(Arrays.asList(Vote.builder().loser("2").winner("1").userId("Bob").build()));

        databaseService.addSurvey(survey);

        simpleAlgorithm = new SimpleAlgorithm(databaseService);

        String code = "comeCode";
        simpleAlgorithm.calculateScoresAndStopSurvey(code);

        ScoreItem scoreItemImage1 = survey.getScores().stream().filter(s -> s.getImageId().equals("1")).findFirst().get();
        ScoreItem scoreItemImage2 = survey.getScores().stream().filter(s -> s.getImageId().equals("2")).findFirst().get();

        assertTrue(scoreItemImage1.getScore() > scoreItemImage2.getScore());
        assertTrue(scoreItemImage1.getScore() == -scoreItemImage2.getScore());
    }

    @Test
    public void calculateScoresAndStopSurvey_normalizeNumberOfVotes() {
        Survey survey = new Survey();

        survey.setImages(Arrays.asList(Image.builder().id("1").build(), Image.builder().id("2").build(), Image.builder().id("3").build(), Image.builder().id("4").build()));

        survey.setVotes(Arrays.asList( // tom and bob prefer picture "1", Charly and Alice prefer picture "2". Charly and Alice vote more frequently.
                Vote.builder().loser("1").winner("2").userId("Tom").build(),
                Vote.builder().loser("1").winner("2").userId("Tom").build(),
                Vote.builder().loser("1").winner("2").userId("Bob").build(),
                Vote.builder().loser("1").winner("2").userId("Bob").build(),

                Vote.builder().loser("2").winner("1").userId("Alice").build(),
                Vote.builder().loser("2").winner("1").userId("Alice").build(),
                Vote.builder().loser("2").winner("1").userId("Alice").build(),
                Vote.builder().loser("2").winner("1").userId("Alice").build(),
                Vote.builder().loser("2").winner("1").userId("Alice").build(),
                Vote.builder().loser("2").winner("1").userId("Charly").build(),
                Vote.builder().loser("2").winner("1").userId("Charly").build(),
                Vote.builder().loser("2").winner("1").userId("Charly").build(),
                Vote.builder().loser("2").winner("1").userId("Charly").build(),
                Vote.builder().loser("2").winner("1").userId("Charly").build(),
                Vote.builder().loser("2").winner("1").userId("Charly").build(),
                Vote.builder().loser("2").winner("1").userId("Charly").build(),
                Vote.builder().loser("2").winner("1").userId("Charly").build(),
                Vote.builder().loser("2").winner("1").userId("Charly").build(),
                Vote.builder().loser("2").winner("1").userId("Charly").build(),
                Vote.builder().loser("2").winner("1").userId("Charly").build(),
                Vote.builder().loser("2").winner("1").userId("Charly").build(),
                Vote.builder().loser("2").winner("1").userId("Charly").build()
        ));

        databaseService.addSurvey(survey);

        simpleAlgorithm = new SimpleAlgorithm(databaseService);

        String code = "someCode";
        simpleAlgorithm.calculateScoresAndStopSurvey(code);

        ScoreItem scoreItemImage1 = survey.getScores().stream().filter(s -> s.getImageId().equals("1")).findFirst().get();
        ScoreItem scoreItemImage2 = survey.getScores().stream().filter(s -> s.getImageId().equals("2")).findFirst().get();

        assertTrue(scoreItemImage1.getScore().equals(scoreItemImage2.getScore()));
    }

}
