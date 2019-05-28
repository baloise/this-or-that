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
package com.baloise.open.thisorthat.api;

import com.baloise.open.thisorthat.api.dto.Error;
import com.baloise.open.thisorthat.api.dto.*;
import com.baloise.open.thisorthat.dto.Image;
import com.baloise.open.thisorthat.dto.Survey;
import com.baloise.open.thisorthat.exception.*;
import com.baloise.open.thisorthat.service.SurveyService;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("survey")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Slf4j
public class SurveyRestService {

    private SurveyService surveyService = new SurveyService();

    @Context
    private HttpServletRequest request;

    @POST
    public Response createSurvey(CreateSurveyRequest createSurveyRequest) {
        try {
            Survey survey = surveyService.createSurvey(createSurveyRequest.getPerspective());
            return Response
                    .ok()
                    .entity(SurveyResponse.builder()
                            .code(survey.getCode())
                            .build())
                    .build();
        } catch (Exception e) {
            return errorHandler(e);
        }
    }

    @DELETE
    @Path("/{code}")
    public Response deleteSurvey(@PathParam("code") String surveyCode) {
        try {
            surveyService.deleteSurvey(surveyCode);
            return Response
                    .ok()
                    .build();
        } catch (Exception e) {
            return errorHandler(e);
        }
    }

    @POST
    @Path("/{code}/start")
    public Response startSurvey(@PathParam("code") String surveyCode) {
        try {
            surveyService.startSurvey(surveyCode);
            return Response
                    .ok()
                    .build();
        } catch (Exception e) {
            return errorHandler(e);
        }
    }

    @POST
    @Path("/{code}/stop")
    public Response stopSurvey(@PathParam("code") String surveyCode) {
        try {
            surveyService.stopSurvey(surveyCode);
            return Response
                    .ok()
                    .build();
        } catch (Exception e) {
            return errorHandler(e);
        }
    }

    @POST
    @Path("/{code}/persist")
    public Response persistSurvey(@PathParam("code") String surveyCode) {
        try {
            surveyService.persistSurvey(surveyCode);
            return Response
                    .ok()
                    .build();
        } catch (Exception e) {
            return errorHandler(e);
        }
    }

    @GET
    @Path("/{code}/vote")
    public Response getVote(@PathParam("code") String surveyCode) {
        try {
            log.info("ENTRY getVote(code={})", surveyCode);
            VoteResponse voteResponse = surveyService.getVote(surveyCode, getUserId());
            final CacheControl cc = new CacheControl();
            cc.setNoCache(true);
            log.info("EXIT getVote()");
            return Response
                    .ok()
                    .entity(voteResponse)
                    .cacheControl(cc)
                    .build();
        } catch (Exception e) {
            return errorHandler(e);
        }
    }

    @POST
    @Path("/{code}/vote")
    public Response setVote(@PathParam("code") String surveyCode, VoteRequest voteRequest) {
        try {
            surveyService.setVote(surveyCode, voteRequest, getUserId());
            return Response
                    .ok()
                    .build();
        } catch (Exception e) {
            return errorHandler(e);
        }
    }

    @GET
    @Path("/{code}/score")
    public Response getScore(@PathParam("code") String surveyCode) {
        try {
            ScoreResponse scoreResponse = surveyService.getScore(surveyCode);
            final CacheControl cc = new CacheControl();
            cc.setNoCache(true);
            return Response
                    .ok()
                    .entity(scoreResponse)
                    .cacheControl(cc)
                    .build();
        } catch (Exception e) {
            return errorHandler(e);
        }
    }

    @POST
    @Path("/{code}/image")
    public Response createImage(@PathParam("code") String surveyCode, ImageRequest imageRequest) {
        try {
            Image image = Image.builder()
                    .file(imageRequest.getFile())
                    .build();
            String id = surveyService.addImageToSurvey(surveyCode, image);
            return Response
                    .ok()
                    .entity(ImageResponse.builder()
                            .id(id)
                            .file(imageRequest.getFile())
                            .build())
                    .build();
        } catch (Exception e) {
            return errorHandler(e);
        }

    }

    private String getUserId() {
        return request.getSession() != null && request.getSession().getId() != null ?
                request.getSession().getId() : "localhost";
    }

    private Response errorHandler(Exception error) {
        log.error("Error: {} Stacktrace: {}", error.getMessage(), error.getStackTrace());
        try {
            throw error;
        } catch (DeleteFailedException e) {
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Error.builder().error(ErrorCode.IMAGE_NOT_FOUND).message(e.getMessage()).build())
                    .build();
        } catch (UpdateFailedException e) {
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Error.builder().error(ErrorCode.UPDATE_FAILED_ERROR).message(e.getMessage()).build())
                    .build();
        } catch (ImageNotFoundException e) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity(Error.builder().error(ErrorCode.DELETE_FAILED_ERROR).message(e.getMessage()).build())
                    .build();
        } catch (SurveyNotFoundException e) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity(Error.builder().error(ErrorCode.SURVEY_NOT_FOUND).message(e.getMessage()).build())
                    .build();
        } catch (SurveyIncompleteException e) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(Error.builder().error(ErrorCode.SURVEY_INCOMPLETE).message(e.getMessage()).build())
                    .build();
        } catch (DatabaseException e) {
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Error.builder().error(ErrorCode.DATABASE_ERROR).message(e.getMessage()).build())
                    .build();
        } catch (AlgorithmException e) {
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Error.builder().error(ErrorCode.ALGORITHM_ERROR).message(e.getMessage()).build())
                    .build();
        } catch (Exception e) {
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Error.builder().error(ErrorCode.UNKNOWN_ERROR).message(e.getMessage()).build())
                    .build();
        }
    }

}
