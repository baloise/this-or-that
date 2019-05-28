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
package com.baloise.open.thisorthat.vote;

import com.baloise.open.thisorthat.db.DatabaseService;
import com.baloise.open.thisorthat.db.InMemoryDatabaseService;
import com.baloise.open.thisorthat.dto.Image;
import com.baloise.open.thisorthat.dto.ScoreItem;
import com.baloise.open.thisorthat.dto.Survey;
import com.baloise.open.thisorthat.dto.Vote;
import org.jglue.cdiunit.CdiRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertTrue;

@RunWith(CdiRunner.class)
public class EloScoreAlgorithmTest {

    private final DatabaseService databaseService = new InMemoryDatabaseService();
    private EloScoreAlgorithm eloScoreAlgorithm;

    @Test
    public void calculateScoresAndStopSurvey_simple_test() {
        Survey survey = new Survey();

        survey.setImages(Arrays.asList(Image.builder().id("1").build(), Image.builder().id("2").build()));

        survey.setVotes(Arrays.asList(Vote.builder().loser("2").winner("1").userId("Bob").build()));

        List<ScoreItem> scores = new ArrayList<>();
        survey.getImages().forEach(image -> {
            ScoreItem score = ScoreItem.builder()
                    .imageId(image.getId())
                    .score(1000) // every picture starts with a score of 1000
                    .build();
            scores.add(score);
        });
        survey.setScores(scores);

        databaseService.addSurvey(survey);
        String code = "someCode";
        survey.setCode(code);

        eloScoreAlgorithm = new EloScoreAlgorithm(databaseService);

        eloScoreAlgorithm.calculateScoresAndStopSurvey(code);

        ScoreItem scoreItemImage1 = survey.getScores().stream().filter(s -> s.getImageId().equals("1")).findFirst().get();
        ScoreItem scoreItemImage2 = survey.getScores().stream().filter(s -> s.getImageId().equals("2")).findFirst().get();

        assertTrue(scoreItemImage1.getScore() > scoreItemImage2.getScore());
    }

    @Test
    public void calculateScoresAndStopSurvey_normalizeNumberOfVotes() {
        Survey survey = new Survey();

        survey.setImages(Arrays.asList(Image.builder().id("1").build(), Image.builder().id("2").build(), Image.builder().id("3").build(), Image.builder().id("4").build()));

        survey.setVotes(Arrays.asList( // tom prefers picture "1", Charly prefers picture "2". Charly and Alice vote more frequently.
                Vote.builder().loser("1").winner("2").userId("Tom").build(),
                Vote.builder().loser("1").winner("2").userId("Tom").build(),

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
                Vote.builder().loser("2").winner("1").userId("Charly").build(),
                Vote.builder().loser("2").winner("1").userId("Charly").build(),
                Vote.builder().loser("2").winner("1").userId("Charly").build(),
                Vote.builder().loser("2").winner("1").userId("Charly").build(),
                Vote.builder().loser("2").winner("1").userId("Charly").build(),
                Vote.builder().loser("2").winner("1").userId("Charly").build()
        ));

        List<ScoreItem> scores = new ArrayList<>();
        survey.getImages().forEach(image -> {
            ScoreItem score = ScoreItem.builder()
                    .imageId(image.getId())
                    .score(1000) // every picture starts with a score of 1000
                    .build();
            scores.add(score);
        });
        survey.setScores(scores);

        databaseService.addSurvey(survey);

        eloScoreAlgorithm = new EloScoreAlgorithm(databaseService);

        String code = "someCode";
        eloScoreAlgorithm.calculateScoresAndStopSurvey(code);

        ScoreItem scoreItemImage1 = survey.getScores().stream().filter(s -> s.getImageId().equals("1")).findFirst().get();
        ScoreItem scoreItemImage2 = survey.getScores().stream().filter(s -> s.getImageId().equals("2")).findFirst().get();

        assertTrue((int) (scoreItemImage1.getScore() * 1.02) > scoreItemImage2.getScore()); // scores are identical except for rounding errors.
        assertTrue((int) (scoreItemImage1.getScore() * 0.98) < scoreItemImage2.getScore());
    }

}
