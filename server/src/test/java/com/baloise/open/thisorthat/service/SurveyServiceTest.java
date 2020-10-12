package com.baloise.open.thisorthat.service;

import com.baloise.open.thisorthat.db.InMemoryDatabase;
import com.baloise.open.thisorthat.dto.Survey;
import com.baloise.open.thisorthat.exception.SurveyAlreadyStartedException;
import com.baloise.open.thisorthat.exception.SurveyStoppedException;
import com.baloise.open.thisorthat.vote.SimpleVoteAlgorithm;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
public class SurveyServiceTest {

    @Mock
    private InMemoryDatabase inMemoryDatabaseMock;

    @Mock
    private SimpleVoteAlgorithm randomAlgorithmMock;

    @InjectMocks
    private SurveyService surveyService;

    @Test
    public void createSurvey() {
        when(inMemoryDatabaseMock.surveyCount()).thenReturn(0L);
        doNothing().when(inMemoryDatabaseMock).addSurvey(any());
        Survey test = surveyService.createSurvey("test");
        verify(inMemoryDatabaseMock, times(1)).addSurvey(any());
        verify(inMemoryDatabaseMock, times(1)).surveyCount();
        assertEquals("1pv1", test.getId());
        assertEquals("test", test.getPerspective());
        assertEquals(false, test.getStarted());
        assertNotNull(test.getImages());
        assertNotNull(test.getImages());
        assertNotNull(test.getScores());
        assertNotNull(test.getVotes());
        assertNotNull(test.getCreationDate());
    }

    @Test
    public void startSurvey() {
        doNothing().when(inMemoryDatabaseMock).startSurvey(any());
        doNothing().when(randomAlgorithmMock).initialize(any());
        Survey survey = Survey.builder()
                .started(false)
                .build();
        when(inMemoryDatabaseMock.getSurvey(anyString())).thenReturn(survey);
        surveyService.startSurvey("surveyCode");
        verify(inMemoryDatabaseMock, times(1)).startSurvey(any());
        verify(randomAlgorithmMock, times(1)).initialize(any());
        verify(inMemoryDatabaseMock, times(1)).getSurvey(anyString());
    }

    @Test(expected = SurveyAlreadyStartedException.class)
    public void startSurvey_expect_already_started_value_exception() {
        Survey survey = Survey.builder()
                .started(true)
                .build();
        when(inMemoryDatabaseMock.getSurvey(anyString())).thenReturn(survey);
        surveyService.startSurvey("surveyCode");
        verify(inMemoryDatabaseMock, times(1)).getSurvey(anyString());
    }

    @Test(expected = SurveyAlreadyStartedException.class)
    public void startSurvey_expect_already_started_null_exception() {
        when(inMemoryDatabaseMock.getSurvey(anyString())).thenReturn(null);
        surveyService.startSurvey("surveyCode");
        verify(inMemoryDatabaseMock, times(1)).getSurvey(anyString());
    }

    @Test
    public void stopSurvey() {
        doNothing().when(randomAlgorithmMock).calculateScoresAndStopSurvey(anyString());
        Survey survey = Survey.builder()
                .started(true)
                .build();
        when(inMemoryDatabaseMock.getSurvey(anyString())).thenReturn(survey);
        surveyService.stopSurvey("surveyCode");
        verify(randomAlgorithmMock, times(1)).calculateScoresAndStopSurvey(anyString());
        verify(inMemoryDatabaseMock, times(1)).getSurvey(anyString());
    }

    @Test(expected = SurveyStoppedException.class)
    public void stopSurvey_survey_stopped_expect_exception() {
        doNothing().when(randomAlgorithmMock).calculateScoresAndStopSurvey(anyString());
        Survey survey = Survey.builder()
                .started(false)
                .build();
        when(inMemoryDatabaseMock.getSurvey(anyString())).thenReturn(survey);
        surveyService.stopSurvey("surveyCode");
    }

    @Test
    public void getVote() {
    }

    @Test
    public void setVote() {
    }

    @Test
    public void getScore() {
    }

    @Test
    public void getImageFromSurvey() {
    }

    @Test
    public void addImageToSurvey() {
    }

    @Test
    public void deleteSurvey() {
    }
}