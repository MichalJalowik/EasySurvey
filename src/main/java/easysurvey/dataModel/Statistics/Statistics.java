package easysurvey.dataModel.Statistics;

import easysurvey.dataModel.PotentialQuestionAnswer;
import easysurvey.dataModel.Question;
import easysurvey.dataModel.Survey;
import easysurvey.persistence.SurveyService;

import java.math.BigInteger;
import java.util.ArrayList;

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

    public void getStats() {

        surveyService.setSurvey(survey);
        this.surveyId = survey.getId();
        this.surveyDescription = survey.getDescription();
        long countInterviewee = surveyService.countInterviewee(surveyId).longValue();
        this.numberOfAnswers = countInterviewee;


        for (Question question : survey.getQuestions()) {
            QuestionStat questionStat = new QuestionStat();
            questionStat.setQuestionId(question.getId());
            questionStat.setQuestionText(question.getQuestionText());
            questionStats.add(questionStat);

            for (PotentialQuestionAnswer potentialQuestionAnswer : question.getPotentialQuestionAnswers()) {
                AnswerStat answerStat = new AnswerStat();
                answerStat.setPotentialQuestionAnswer(potentialQuestionAnswer);

                long howManyAnswers = surveyService.statAnswers(question.getId(), potentialQuestionAnswer.getId());
                double prc = (double) howManyAnswers / countInterviewee * 100;
                answerStat.setAnswerPercentage(round(prc, 1));

                answerStat.setNumberOfAnswers(howManyAnswers);
                questionStat.setAnswerStats(answerStat);
            }

        }


    }

    public void getStatsWithFilter() {

        surveyService.setSurvey(survey);
        this.surveyId = survey.getId();
        this.surveyDescription = survey.getDescription();
        long countInterviewee = surveyService.countInterviewee(surveyId).longValue();
        this.numberOfAnswers = countInterviewee;


        //System.out.println("xxxxxx" + metricId);

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
                    howManyAnswers = surveyService.statAnswersWithFilter(question.getId(), potentialQuestionAnswer.getId(), metricId, surveyId).longValue() + howManyAnswers;
                }
                howManyMetricsAnswers = howManyMetricsAnswers + howManyAnswers;


                double prc = (double) howManyAnswers / howManyMetricsAnswers;
                answerStat.setAnswerPercentage(round(prc, 1));

                answerStat.setNumberOfAnswers(howManyAnswers);
                questionStat.setAnswerStats(answerStat);

            }
            questionStat.setNumberOfMetricAnswers(howManyMetricsAnswers);
            System.out.println(questionStat.getNumberOfMetricAnswers());

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

    public double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

}
