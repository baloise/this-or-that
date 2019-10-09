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
package com.baloise.open.thisorthat;


public class VoteResponse {
    private String id1;
    private String id2;
    private Boolean surveyIsRunning;
    private String perspective;

    public VoteResponse(String id1, String id2, Boolean surveyIsRunning, String perspective) {
        this.id1 = id1;
        this.id2 = id2;
        this.surveyIsRunning = surveyIsRunning;
        this.perspective = perspective;
    }

    public VoteResponse() {
    }

    public String getId1() {
        return id1;
    }

    public void setId1(String id1) {
        this.id1 = id1;
    }

    public String getId2() {
        return id2;
    }

    public void setId2(String id2) {
        this.id2 = id2;
    }

    public Boolean getSurveyIsRunning() {
        return surveyIsRunning;
    }

    public void setSurveyIsRunning(Boolean surveyIsRunning) {
        this.surveyIsRunning = surveyIsRunning;
    }

    public String getPerspective() {
        return perspective;
    }

    public void setPerspective(String perspective) {
        this.perspective = perspective;
    }
}
