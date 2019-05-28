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

import com.baloise.open.thisorthat.dto.Outcome;
import org.jglue.cdiunit.CdiRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

@RunWith(CdiRunner.class)
public class EloScoreCalculatorTest {

    @Test
    public void test_winner() {
        // see: https://de.wikipedia.org/wiki/Elo-Zahl
        int newElo = EloScoreCalculator.calculateNewScore(2806, 2577, 10, Outcome.LOSS);
        assertEquals(2798, newElo);

        newElo = EloScoreCalculator.calculateNewScore(2577, 2806, 10, Outcome.WIN);
        assertEquals(2584, newElo);
    }

    @Test
    public void test_draw() {
        // see: https://de.wikipedia.org/wiki/Elo-Zahl
        int newElo = EloScoreCalculator.calculateNewScore(2806, 2577, 10, Outcome.DRAW);
        assertEquals(2803, newElo);
    }

}