package com.baloise.open.thisorthat.api;

import com.baloise.open.thisorthat.api.dto.*;
import com.baloise.open.thisorthat.dto.Image;
import com.baloise.open.thisorthat.dto.Survey;
import com.baloise.open.thisorthat.service.SurveyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.server.ResponseStatusException;

import javax.xml.bind.DatatypeConverter;

import static org.springframework.http.ResponseEntity.ok;

@RestController
public class ApiController {

    private SurveyService surveyService = new SurveyService();

    private String getUserId() {
        String sessionId = RequestContextHolder.currentRequestAttributes().getSessionId();
        return sessionId != null ? sessionId : "sessionIdUnavailable";
    }

    private ResponseStatusException buildError(Throwable t) {
        // FIXME
        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An error occured: " + t.getMessage(), t);
    }

    @PostMapping(consumes = "application/json")
    public ResponseEntity<SurveyResponse> createSurvey(@RequestBody CreateSurveyRequest createSurveyRequest) {
        try {
            Survey survey = surveyService.createSurvey(createSurveyRequest.getPerspective());
            SurveyResponse build = SurveyResponse.builder()
                    .code(survey.getCode())
                    .build();
            return new ResponseEntity<>(build, HttpStatus.OK);
        } catch (Exception e) {
            throw buildError(e);
        }
    }

    @DeleteMapping
    public ResponseEntity deleteSurvey(@PathVariable("code") String surveyCode) {
        try {
            surveyService.deleteSurvey(surveyCode);
            return ok().build();
        } catch (Exception e) {
            throw buildError(e);
        }
    }

    @PostMapping("/{code}/start")
    public ResponseEntity startSurvey(@PathVariable("code") String surveyCode) {
        try {
            surveyService.startSurvey(surveyCode);
            return ok().build();
        } catch (Exception e) {
            throw buildError(e);
        }
    }

    @PostMapping("/{code}/stop")
    public ResponseEntity stopSurvey(@PathVariable("code") String surveyCode) {
        try {
            surveyService.stopSurvey(surveyCode);
            return ok().build();
        } catch (Exception e) {
            throw buildError(e);
        }
    }

    @PostMapping("/{code}/persist")
    public ResponseEntity persistSurvey(@PathVariable("code") String surveyCode) {
        try {
            surveyService.persistSurvey(surveyCode);
            return ok().build();
        } catch (Exception e) {
            throw buildError(e);
        }
    }

    @GetMapping(value = "/{code}/vote")
    public ResponseEntity<VoteResponse> getSurvey(@PathVariable("code") String surveyCode) {
        try {
            VoteResponse vote = surveyService.getVote(surveyCode, getUserId());
            return new ResponseEntity<>(vote, HttpStatus.OK);
        } catch (Throwable t) {
            throw buildError(t);
        }
    }

    @PostMapping(value = "/{code}/vote", consumes = "application/json")
    public ResponseEntity<VoteResponse> postSurvey(@PathVariable("code") String surveyCode, @RequestBody VoteRequest voteRequest) {
        try {
            surveyService.setVote(surveyCode, voteRequest, getUserId());
            return ok().build();
        } catch (Throwable t) {
            throw buildError(t);
        }
    }

    @GetMapping(value = "/{code}/score")
    public ResponseEntity<ScoreResponse> getScore(@PathVariable("code") String surveyCode) {
        try {
            ScoreResponse scoreResponse = surveyService.getScore(surveyCode);
            return new ResponseEntity<>(scoreResponse, HttpStatus.OK);
        } catch (Exception e) {
            throw buildError(e);
        }
    }

    @PostMapping(value = "/{code}/image", consumes = "application/json")
    public ResponseEntity<ImageResponse> createImage(@PathVariable("code") String surveyCode, @RequestBody ImageRequest imageRequest) {
        try {
            Image image = Image.builder()
                    .file(imageRequest.getFile())
                    .build();
            String id = surveyService.addImageToSurvey(surveyCode, image);
            ImageResponse build = ImageResponse.builder()
                    .id(id)
                    .file(imageRequest.getFile())
                    .build();

            return new ResponseEntity<>(build, HttpStatus.OK);
        } catch (Exception e) {
            throw buildError(e);
        }
    }

    @GetMapping(value = "/{code}/image/{imageId}", produces = "image/jpeg")
    public ResponseEntity<byte[]> getImage(@PathVariable("code") String surveyCode, @PathVariable("imageId") String imageId) {
        try {
            Image image = surveyService.getImageFromSurvey(surveyCode, imageId);
            String base64Image = image.getFile().split(",")[1];
            byte[] bytes = DatatypeConverter.parseBase64Binary(base64Image);
            return new ResponseEntity<>(bytes, HttpStatus.OK);
        } catch (Exception e) {
            throw buildError(e);
        }
    }

}
