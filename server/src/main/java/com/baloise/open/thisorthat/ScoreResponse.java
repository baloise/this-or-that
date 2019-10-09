package com.baloise.open.thisorthat;

import java.util.List;

public class ScoreResponse {
    private List<Score> scores;
    private Boolean surveyIsRunning;
    private String perspective;
    private Integer numberOfVotes;
    private Integer numberOfUsers;

    public ScoreResponse(List<Score> scores, Boolean surveyIsRunning, String perspective, Integer numberOfVotes, Integer numberOfUsers) {
        this.scores = scores;
        this.surveyIsRunning = surveyIsRunning;
        this.perspective = perspective;
        this.numberOfVotes = numberOfVotes;
        this.numberOfUsers = numberOfUsers;
    }

    public ScoreResponse() {
    }

    public List<Score> getScores() {
        return scores;
    }

    public void setScores(List<Score> scores) {
        this.scores = scores;
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

    public Integer getNumberOfVotes() {
        return numberOfVotes;
    }

    public void setNumberOfVotes(Integer numberOfVotes) {
        this.numberOfVotes = numberOfVotes;
    }

    public Integer getNumberOfUsers() {
        return numberOfUsers;
    }

    public void setNumberOfUsers(Integer numberOfUsers) {
        this.numberOfUsers = numberOfUsers;
    }
}