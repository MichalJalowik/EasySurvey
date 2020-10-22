package easysurvey.dataModel.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import easysurvey.dataModel.Interviewee;
import easysurvey.dataModel.Question;
import easysurvey.dataModel.Statistics.Statistics;
import easysurvey.dataModel.Survey;
import easysurvey.persistence.QuestionService;
import easysurvey.persistence.SurveyService;
import org.apache.tomcat.util.json.JSONParser;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

@RestController
@RequestMapping("/api/statistics")
public class StatisticsRestController {

    @Autowired
    SurveyService surveyService;
    @Autowired
    QuestionService questionService;
    

//    @RequestMapping(value = "", method = RequestMethod.GET)
//    public ResponseEntity<?> getStatistics() {
//        //Collection<Survey> surveys = surveyService.getAllSurveys();
////        String test = "test1";
////        String[] test3 = {"dd","dddds"};
////        int liczba = 3;
////        Collection<String> collection = new ArrayList<>();
////        collection.add("test2");
//        Collection<Interviewee> result = surveyService.statCalculation();
//
//        return new ResponseEntity<Collection<Interviewee>>(result, HttpStatus.OK);
//    }

//    @RequestMapping(value = "/1", method = RequestMethod.GET)
//    public ResponseEntity<?> getInterviewees() {
//        Collection<Interviewee> interviewees = surveyService.getAllInterviewees();
//        return new ResponseEntity<Collection<Interviewee>>(interviewees, HttpStatus.OK);
//    }
//
//    @RequestMapping(value = "/2", method = RequestMethod.GET)
//    public ResponseEntity<?> getIntervieweesTest() {
////        Gson g = new Gson();
//        String test = "test1";
////        JSONParser parser = new JSONParser();
////        JSONObject json = (JSONObject) parser.parse(test);
////        Player ronaldo = new ObjectMapper().readValue(test, Player.class);
//
//        return new ResponseEntity<String>(test, HttpStatus.OK);
//    }

//    @RequestMapping(value = "/{questionId}/{questionAnswerId}", method = RequestMethod.GET)
//    public ResponseEntity<?> getStatsOfAns(@PathVariable("questionId") long qID, @PathVariable("questionAnswerId") long qaID ) {
//
//        BigInteger intQty = surveyService.countInterviewee();
//        Long ansQty = surveyService.statAnswers(qID,qaID);
//        Long result = ansQty*100/intQty;
//
//        return new ResponseEntity<Long>(result, HttpStatus.OK);
//    }

    @RequestMapping(value = "/{surveyid}", method = RequestMethod.GET)
    public ResponseEntity<?> getSurvey(@PathVariable("surveyid") long surveyID) {
        Survey survey = surveyService.getSurvey(surveyID);
        Statistics statisticsWithFilter = new Statistics(survey);
        statisticsWithFilter.getStatsWithFilter();
        if (survey == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Statistics>(statisticsWithFilter, HttpStatus.OK);
    }

    ArrayList<Long> chosenMetrics = new ArrayList<Long>();
    @RequestMapping(value = "/{surveyId}/metrics", method = RequestMethod.POST)
    public ResponseEntity<?> getSurveyWithMetrics(@PathVariable("surveyId") long surveyID, @RequestBody ArrayList<Long> chosenMetrics) {
        Survey survey = surveyService.getSurvey(surveyID);
        Statistics statisticsWithFilter = new Statistics(survey, chosenMetrics);
        statisticsWithFilter.getStatsWithFilter();
        if (survey == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<Statistics>(statisticsWithFilter,HttpStatus.OK);

    }



}
