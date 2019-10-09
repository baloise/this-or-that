package com.baloise.open.thisorthat.api;

import com.baloise.open.thisorthat.api.dto.Score;
import com.baloise.open.thisorthat.api.dto.ScoreResponse;
import com.baloise.open.thisorthat.api.dto.VoteRequest;
import com.baloise.open.thisorthat.api.dto.VoteResponse;
import com.baloise.open.thisorthat.service.SurveyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.Arrays;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
public class ApiController {

    private SurveyService surveyService = new SurveyService();

    private String getUserId() {
        String sessionId = RequestContextHolder.currentRequestAttributes().getSessionId();
        return sessionId != null ? sessionId : "sessionIdUnavailable";
    }

    @RequestMapping(value = "/{code}/vote", method = GET)
    @ResponseBody
    public ResponseEntity<VoteResponse> getSurvey(@PathVariable("code") String surveyCode) {
        try {
            VoteResponse voteResponse = surveyService.getVote(surveyCode, getUserId());
            return new ResponseEntity<>(new VoteResponse(), HttpStatus.OK);
        } catch (Throwable t) {
            VoteResponse voteResponse = new VoteResponse();
            voteResponse.setPerspective(t.getMessage());
            return new ResponseEntity<>(voteResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/{code}/score", method = GET)
    @ResponseBody
    public ResponseEntity<ScoreResponse> getScore(@PathVariable("code") String surveyCode) {
        ScoreResponse scoreResponse = new ScoreResponse();
        scoreResponse.setNumberOfUsers(2);
        scoreResponse.setNumberOfVotes(30);
        scoreResponse.setPerspective("Some topic");
        scoreResponse.setSurveyIsRunning(false);
        scoreResponse.setScores(Arrays.asList(
                new Score("0", "0", 6),
                new Score("1", "1", 4),
                new Score("2", "2", 15),
                new Score("3", "3", 5)
        ));

        return new ResponseEntity<ScoreResponse>(scoreResponse, HttpStatus.OK);
    }

    @PostMapping("/{code}/vote")
    public void setWinner(@PathVariable("code") String surveyCode, VoteRequest voteRequest) {
        // do nothing
    }

    @GetMapping(value = "/{code}/image/{imageId}", produces = "image/jpeg")
    public ResponseEntity<byte[]> getSurvey(@PathVariable("code") String surveyCode, @PathVariable("imageId") String
            imageId) {
        /*if (imageId.equalsIgnoreCase("0")) {
            return new ResponseEntity<byte[]>(javax.xml.bind.DatatypeConverter.parseBase64Binary(Img.img1), HttpStatus.OK);
        }
        if (imageId.equalsIgnoreCase("1")) {
            return new ResponseEntity<byte[]>(javax.xml.bind.DatatypeConverter.parseBase64Binary(Img.img2), HttpStatus.OK);
        }
        if (imageId.equalsIgnoreCase("2")) {
            return new ResponseEntity<byte[]>(javax.xml.bind.DatatypeConverter.parseBase64Binary(Img.img3), HttpStatus.OK);
        }
        return new ResponseEntity<byte[]>(javax.xml.bind.DatatypeConverter.parseBase64Binary(Img.img4), HttpStatus.OK);
        */
        return null;
    }

}
