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

import com.baloise.open.thisorthat.server.dto.Outcome;

/**
 * Helper class to calculate elo scores.
 * see: https://de.wikipedia.org/wiki/Elo-Zahl
 */
class EloScoreCalculator {

    static int calculateNewScore(int playerElo, int opponentElo, Outcome outcome) {
        int defaultWeight = 20;
        return calculateNewScore(playerElo, opponentElo, defaultWeight, outcome);
    }

    static int calculateNewScore(int playerElo, int opponentElo, int weight, Outcome outcome) {
        return (int) (playerElo + (weight * (outcome.getValue() - calculateEa(playerElo, opponentElo))));
    }

    private static double calculateEa(int player, int opponent) {
        return 1.0 / (1.0 + Math.pow(10, ((double) opponent - (double) player) / 400.0));
    }

}
