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

import com.baloise.open.thisorthat.api.dto.*;
import com.baloise.open.thisorthat.dto.Image;
import com.baloise.open.thisorthat.dto.Survey;
import com.baloise.open.thisorthat.exception.*;
import com.baloise.open.thisorthat.service.SurveyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.xml.bind.DatatypeConverter;

import static org.springframework.http.ResponseEntity.ok;

@RestController
public class ApiController {

    private final SurveyService surveyService;

    public ApiController(SurveyService surveyService) {
        this.surveyService = surveyService;
    }

    @PostMapping(path = "/create", consumes = "application/json")
    @CrossOrigin(origins = "*")
    public SurveyResponse createSurvey(@RequestBody CreateSurveyRequest createSurveyRequest) {
        try {
            Survey survey = surveyService.createSurvey(createSurveyRequest.getPerspective());
            return SurveyResponse.builder()
                    .code(survey.getCode())
                    .build();
        } catch (Exception e) {

            throw buildError(e);
        }
    }

    @DeleteMapping("/{code}/delete")
    @CrossOrigin(origins = "*")
    public void deleteSurvey(@PathVariable("code") String surveyCode) {
        try {
            surveyService.deleteSurvey(surveyCode);
        } catch (Exception e) {
            throw buildError(e);
        }
    }

    @PostMapping("/{code}/start")
    @CrossOrigin(origins = "*")
    public void startSurvey(@PathVariable("code") String surveyCode) {
        try {
            surveyService.startSurvey(surveyCode);
        } catch (Exception e) {
            throw buildError(e);
        }
    }

    @PostMapping("/{code}/stop")
    @CrossOrigin(origins = "*")
    public void stopSurvey(@PathVariable("code") String surveyCode) {
        try {
            surveyService.stopSurvey(surveyCode);
            surveyService.persistSurvey(surveyCode);
        } catch (Exception e) {
            throw buildError(e);
        }
    }

    @GetMapping(value = "/{code}/vote")
    @CrossOrigin(origins = "*")
    public VoteResponse getVote(@RequestHeader("userId") String userId, @PathVariable("code") String surveyCode) {
        try {
            return surveyService.getVote(surveyCode, userId);
        } catch (Exception e) {
            throw buildError(e);
        }
    }

    @PostMapping(value = "/{code}/vote", consumes = "application/json")
    @CrossOrigin(origins = "*")
    public ResponseEntity<VoteResponse> setVote(@RequestHeader("userId") String userId, @PathVariable("code") String surveyCode, @RequestBody VoteRequest voteRequest) {
        try {
            surveyService.setVote(surveyCode, voteRequest, userId);
            return ok().build();
        } catch (Exception e) {
            throw buildError(e);
        }
    }

    @GetMapping(value = "/{code}/score")
    @CrossOrigin(origins = "*")
    public ScoreResponse getScore(@PathVariable("code") String surveyCode) {
        try {
            return surveyService.getScore(surveyCode);
        } catch (Exception e) {
            throw buildError(e);
        }
    }

    @PostMapping(value = "/{code}/image", consumes = "application/json")
    @CrossOrigin(origins = "*")
    public ImageResponse addImageToSurvey(@PathVariable("code") String surveyCode, @RequestBody ImageRequest imageRequest) {
        try {
            Image image = Image.builder()
                    .file(imageRequest.getFile())
                    .build();
            String id = surveyService.addImageToSurvey(surveyCode, image);

            return ImageResponse.builder()
                    .id(id)
                    .build();
        } catch (Exception e) {
            throw buildError(e);
        }
    }

    @GetMapping(value = "/{code}/image/{imageId}", produces = "image/jpeg")
    @CrossOrigin(origins = "*")
    public byte[] getImageFromSurvey(@PathVariable("code") String surveyCode, @PathVariable("imageId") String imageId) {
        try {
            Image image = surveyService.getImageFromSurvey(surveyCode, imageId);
            String base64Image = image.getFile().split(",")[1];
            return DatatypeConverter.parseBase64Binary(base64Image);
        } catch (Exception e) {
            throw buildError(e);
        }
    }

    private ResponseStatusException buildError(Exception exception) {
        if (exception instanceof SurveyNotFoundException) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, exception.getMessage());
        } else if (exception instanceof ImageNotFoundException) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, exception.getMessage());
        } else if (exception instanceof SurveyStillRunningException) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exception.getMessage());
        } else if (exception instanceof SurveyStoppedException) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exception.getMessage());
        } else if (exception instanceof SurveyAlreadyStartedException) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exception.getMessage());
        } else {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An error occured: " + exception.getMessage(), exception);
        }
    }

}
