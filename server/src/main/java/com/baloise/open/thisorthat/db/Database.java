package com.baloise.open.thisorthat.db;

import com.baloise.open.thisorthat.dto.Image;
import com.baloise.open.thisorthat.dto.ScoreItem;
import com.baloise.open.thisorthat.dto.Survey;
import com.baloise.open.thisorthat.dto.Vote;

import java.util.Date;
import java.util.List;

public interface Database {

    long surveyCount();

    void addSurvey(Survey survey);

    void removeSurvey(String code);

    Survey getSurvey(String code);

    String addImageToSurvey(String surveyCode, Image image);

    void startSurvey(String surveyCode);

    void stopSurvey(String surveyCode);

    Image getImageFromSurvey(String surveyCode, String imageId);

    List<Survey> getSurveysOlderThan(Date cutOffDate);

    void addScore(String surveyCode, ScoreItem scoreItem);

    void persistVote(String surveyCode, Vote vote);
}
