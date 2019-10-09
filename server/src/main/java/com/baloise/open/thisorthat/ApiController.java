package com.baloise.open.thisorthat;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
public class ApiController {


    @RequestMapping(value = "/{code}/vote", method = GET)
    @ResponseBody
    public ResponseEntity<VoteResponse> getSurvey(@PathVariable("code") String surveyCode) throws InterruptedException {
        Thread.sleep(1000);
        if (surveyCode.equalsIgnoreCase("fail")) {
            return new ResponseEntity<VoteResponse>(new VoteResponse(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        int randomNum1 = ThreadLocalRandom.current().nextInt(0, 5);
        int randomNum2 = ThreadLocalRandom.current().nextInt(0, 5);

        VoteResponse vr = new VoteResponse();
        vr.setId1(String.valueOf(randomNum1));
        vr.setId2(String.valueOf(randomNum2));
        vr.setPerspective("Something");
        vr.setSurveyIsRunning(!surveyCode.equalsIgnoreCase("done"));
        return new ResponseEntity<VoteResponse>(vr, HttpStatus.OK);
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
        if (imageId.equalsIgnoreCase("0")) {
            return new ResponseEntity<byte[]>(javax.xml.bind.DatatypeConverter.parseBase64Binary(Img.img1), HttpStatus.OK);
        }
        if (imageId.equalsIgnoreCase("1")) {
            return new ResponseEntity<byte[]>(javax.xml.bind.DatatypeConverter.parseBase64Binary(Img.img2), HttpStatus.OK);
        }
        if (imageId.equalsIgnoreCase("2")) {
            return new ResponseEntity<byte[]>(javax.xml.bind.DatatypeConverter.parseBase64Binary(Img.img3), HttpStatus.OK);
        }
        return new ResponseEntity<byte[]>(javax.xml.bind.DatatypeConverter.parseBase64Binary(Img.img4), HttpStatus.OK);
    }

}
