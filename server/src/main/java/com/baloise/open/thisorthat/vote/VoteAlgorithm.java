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

import com.baloise.open.thisorthat.dto.ScoreItem;
import com.baloise.open.thisorthat.dto.VoteItem;

import java.util.List;

public interface VoteAlgorithm {

    void initialize(String surveyCode);

    VoteItem getVote(String surveyCode, String userId);

    void setVote(String surveyCode, String imageIdWinner, String imageIdLooser, String userId);

    List<ScoreItem> calculateScores(String surveyCode);
}
