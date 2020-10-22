package easysurvey.dataModel.Statistics;

import easysurvey.dataModel.PotentialQuestionAnswer;
import easysurvey.dataModel.Question;
import easysurvey.dataModel.Survey;
import easysurvey.persistence.SurveyService;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;

public class Statistics {

    private Survey survey;
    private ArrayList<Long> metricsIds = new ArrayList<>();
    private SurveyService surveyService = new SurveyService();
    private ArrayList<QuestionStat> questionStats = new ArrayList<>();
    private long numberOfAnswers;
    private long surveyId;
    private String surveyDescription;

    public Statistics(Survey survey) {
        this.survey = survey;
    }

    public Statistics(Survey survey, ArrayList<Long> metricsIds) {
        this.survey = survey;
        this.metricsIds = metricsIds;
    }

    public ArrayList<QuestionStat> getQuestionStats() {
        return questionStats;
    }

    public void getStatsWithFilter() {

        surveyService.setSurvey(survey);
        this.surveyId = survey.getId();
        this.surveyDescription = survey.getDescription();
        long countInterviewee = surveyService.countInterviewee(surveyId).longValue();
        this.numberOfAnswers = countInterviewee;
        HashSet<Long> metrics_id = new HashSet<>();

        for (Question question : survey.getQuestions()) {
            QuestionStat questionStat = new QuestionStat();
            questionStat.setQuestionId(question.getId());
            questionStat.setQuestionText(question.getQuestionText());
            questionStats.add(questionStat);

            long howManyMetricsAnswers=0;
            for (PotentialQuestionAnswer potentialQuestionAnswer : question.getPotentialQuestionAnswers()) {
                AnswerStat answerStat = new AnswerStat();
                answerStat.setPotentialQuestionAnswer(potentialQuestionAnswer);

                long howManyAnswers = 0;
                for (Long metricId : metricsIds) {
                    metrics_id.add(surveyService.takeMetric_id(metricId).longValue());
                    howManyAnswers = surveyService.statAnswersWithFilter(question.getId(), potentialQuestionAnswer.getId(), metricId, surveyId).longValue() + howManyAnswers;
                    //System.out.println("    metricID: " +  metricId + " .Answers qty: " + howManyAnswers);
                }

                if(metrics_id.size() > 1){
                    answerStat.setNumberOfAnswers(howManyAnswers / metrics_id.size());
                    howManyMetricsAnswers = howManyMetricsAnswers + howManyAnswers / metrics_id.size();
                } else {
                    answerStat.setNumberOfAnswers(howManyAnswers);
                    howManyMetricsAnswers = howManyMetricsAnswers + howManyAnswers;
                }
                //System.out.println("pot quest: " + potentialQuestionAnswer.getText()  + " .how many answers: " + howManyAnswers);
                questionStat.setAnswerStats(answerStat);
            }
            questionStat.setNumberOfMetricAnswers(howManyMetricsAnswers);
            //System.out.println("how many metric answers: " + questionStat.getNumberOfMetricAnswers());
            //System.out.println(metrics_id);
        }
    }

    public Survey getSurvey() {
        return survey;
    }

    public long getSurveyId() {
        return surveyId;
    }

    public String getSurveyDescription() {
        return surveyDescription;
    }

    public long getNumberOfAnswers() {
        return numberOfAnswers;
    }

}
