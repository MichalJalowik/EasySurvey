package easysurvey.persistence;

import easysurvey.dataModel.*;

import easysurvey.dataModel.Statistics.QuestionStat;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.*;

@Component("surveyService")
public class SurveyService {

    Session session;
    Survey survey;
    Question question;
    PotentialQuestionAnswer potentialQuestionAnswer;
    Metric metric;
    PotentialMetricAnswer potentialMetricAnswer;
    Interviewee interviewee;
    QuestionAnswer questionAnswer;
    MetricAnswer metricAnswer;
    @Autowired
    QuestionService questionService;

    public SurveyService() {
        session = HibernateUtil.getSessionFactory().openSession();
    }

    public void close() {
        session.close();
        HibernateUtil.shutdown();
    }

    public void setSurvey(Survey survey) {
        this.survey = survey;
    }

    public void createSurvey() {
        survey = addNewSurvey("badanie kolegow", "www.easysurvey.com", "www.easysurvey.edit.com", LocalDate.of(2019, 8, 30), LocalDate.of(2025, 8, 30), true);

        Question question1 = createQuestion(survey.getId(), "jak lubisz kolor?");
        PotentialQuestionAnswer pqa1 = addPotentialQuestionAnswer(question.getId(), "zielony");
        PotentialQuestionAnswer pqa2 = addPotentialQuestionAnswer(question.getId(), "czarny");
        PotentialQuestionAnswer pqa3 = addPotentialQuestionAnswer(question.getId(), "niebieski");

        Question question2 = createQuestion(survey.getId(), "dokad chcialbys pojechac na wakacje?");
        PotentialQuestionAnswer pqa4 = addPotentialQuestionAnswer(question.getId(), "USA");
        PotentialQuestionAnswer pqa5 = addPotentialQuestionAnswer(question.getId(), "Indonezja");
        PotentialQuestionAnswer pqa6 = addPotentialQuestionAnswer(question.getId(), "Australia");
        PotentialQuestionAnswer pqa7 = addPotentialQuestionAnswer(question.getId(), "Chile");

        Metric metric1 = createMetric(survey.getId(), "podaj przedial wiekowy?");
        PotentialMetricAnswer pma1 = addPotentialMetricAnswer(metric.getId(), "20-30");
        PotentialMetricAnswer pma2 = addPotentialMetricAnswer(metric.getId(), "31-40");
        PotentialMetricAnswer pma3 = addPotentialMetricAnswer(metric.getId(), "41-50");
        PotentialMetricAnswer pma4 = addPotentialMetricAnswer(metric.getId(), "51-100");

        Metric metric2 = createMetric(survey.getId(), "i ilu krajach juz bylem?");
        PotentialMetricAnswer pma5 = addPotentialMetricAnswer(metric.getId(), "1-10");
        PotentialMetricAnswer pma6 = addPotentialMetricAnswer(metric.getId(), "11-20");
        PotentialMetricAnswer pma7 = addPotentialMetricAnswer(metric.getId(), "21-50");
        PotentialMetricAnswer pma8 = addPotentialMetricAnswer(metric.getId(), "50-299");

        Interviewee interviewee1 = addNewInterviewee("MichalJ");
        questionService.giveMetricAnswerByIntervieweeId(interviewee1, survey, metric1, pma2);
        questionService.giveMetricAnswerByIntervieweeId(interviewee1, survey, metric2, pma8);
        questionService.giveQuestionAnswerByIntervieweeId(interviewee1, survey, question1, pqa2);
        questionService.giveQuestionAnswerByIntervieweeId(interviewee1, survey, question2, pqa7);

        Interviewee interviewee2 = addNewInterviewee("Marco01");
        questionService.giveMetricAnswerByIntervieweeId(interviewee2, survey, metric1, pma1);
        questionService.giveMetricAnswerByIntervieweeId(interviewee2, survey, metric2, pma7);
        questionService.giveQuestionAnswerByIntervieweeId(interviewee2, survey, question1, pqa2);
        questionService.giveQuestionAnswerByIntervieweeId(interviewee2, survey, question2, pqa6);

        Interviewee interviewee3 = addNewInterviewee("Pawel01");
        questionService.giveMetricAnswerByIntervieweeId(interviewee3, survey, metric1, pma3);
        questionService.giveMetricAnswerByIntervieweeId(interviewee3, survey, metric2, pma6);
        questionService.giveQuestionAnswerByIntervieweeId(interviewee3, survey, question1, pqa3);
        questionService.giveQuestionAnswerByIntervieweeId(interviewee3, survey, question2, pqa5);

    }

    public Interviewee addNewInterviewee(String nickName) {
        Transaction txn = session.getTransaction();
        txn.begin();

        interviewee = new Interviewee(nickName);

        session.persist(interviewee);
        txn.commit();
        return interviewee;
    }


    public Survey addNewSurvey(String description, String surveyLink, String editLink, LocalDate startDate, LocalDate endDate, boolean isOpen) {
        Transaction txn = session.getTransaction();
        txn.begin();

        survey = new Survey(description, surveyLink, editLink, startDate, endDate, isOpen);

        session.persist(survey);
        txn.commit();
        return survey;
    }

    public Question createQuestion(Long surveyId, String questionText) {
        Transaction txn = session.getTransaction();
        txn.begin();

        Survey findSurveyById = (Survey) session.get(Survey.class, surveyId);
        question = new Question(questionText);
        findSurveyById.getQuestions().add(question);

        session.persist(question);
        txn.commit();

        return question;
    }

    public PotentialQuestionAnswer addPotentialQuestionAnswer(Long questionId, String potentialQuestionAnswerText) {
        Transaction txn = session.getTransaction();
        txn.begin();

        Question findQuestionById = (Question) session.get(Question.class, questionId);
        potentialQuestionAnswer = new PotentialQuestionAnswer(potentialQuestionAnswerText);
        findQuestionById.getPotentialQuestionAnswers().add(potentialQuestionAnswer);

        session.persist(potentialQuestionAnswer);
        txn.commit();

        return potentialQuestionAnswer;
    }

    public Metric createMetric(Long surveyId, String metricText) {
        Transaction txn = session.getTransaction();
        txn.begin();

        Survey findSurveyById = (Survey) session.get(Survey.class, surveyId);
        metric = new Metric(metricText);
        findSurveyById.getMetrics().add(metric);

        session.persist(metric);
        txn.commit();

        return metric;
    }

    public PotentialMetricAnswer addPotentialMetricAnswer(Long metricId, String potentialMetricAnswerText) {
        Transaction txn = session.getTransaction();
        txn.begin();

        Metric findMetricById = (Metric) session.get(Metric.class, metricId);
        potentialMetricAnswer = new PotentialMetricAnswer(potentialMetricAnswerText);
        findMetricById.getPotentialMetricAnswers().add(potentialMetricAnswer);

        session.persist(potentialMetricAnswer);
        txn.commit();

        return potentialMetricAnswer;
    }

    public Collection<Survey> getAllSurveys() {
        Collection<Survey> surveys = session.createCriteria(Survey.class).list();
        return surveys;
    }

    public Survey getSurvey(Long surveyID) {
        return (Survey) session.get(Survey.class, surveyID);
    }

    public Interviewee getInterviewee(long intervieweeId) {
        return (Interviewee) session.get(Interviewee.class, intervieweeId);
    }

    public Collection<Interviewee> getAllInterviewees() {
//        String hql = "SELECT Interviewee.nickName FROM Interviewee";
//        Query query = session.createQuery(hql);
//        List results = query.list();
//        return results;
        Collection<Interviewee> interviewees = session.createCriteria(Interviewee.class).list();
        return interviewees;
    }

    public List<Interviewee> statCalculation() {

        String hql = "FROM Interviewee ";
        Query query = session.createQuery(hql);
        List<Interviewee> results = query.list();
        return results;
    }

    public long statAnswers(long questionId, long questionAnswerId) {

        Query query2 = session.createQuery("select count(q) from QuestionAnswer q where q.question='" + questionId + "' and q.questionAnswer='" + questionAnswerId + "'");

        Long interCount2 = (Long) query2.uniqueResult();
        //List<Interviewee> results = query.list();
        //System.out.println("ilosc interwiwes to " + interCount2);
        return interCount2;

    }

    //to do
    public BigInteger statAnswersWithFilter(long questionId, long questionAnswerId, long metricId, long surveyId) {

        String sql = "SELECT COUNT(*) AS c1 FROM (SELECT questionanswers.question_id, questionanswers.question_answer_id, questionanswers.survey_id, metricanswers.metric_answer_id, questionanswers.interwivee_id FROM questionanswers INNER JOIN metricanswers ON questionanswers.interwivee_id=metricanswers.interwivee_id WHERE metricanswers.metric_answer_id='" + metricId + "' AND questionanswers.question_id='" + questionId + "' AND questionanswers.question_answer_id='" + questionAnswerId + "' AND questionanswers.survey_id='" + surveyId + "') AS deliveredTable";
        //String sql = "SELECT COUNT(*) AS c1 FROM metricanswers WHERE metricanswers.metric_answer_id='"+ metricId +"'";
        Query query = session.createSQLQuery(sql);
        BigInteger interCount = (BigInteger) query.uniqueResult();
        return interCount;

    }

    public BigInteger takeMetric_id(long metricId) {
        String sql = "SELECT metric_id FROM potentialmetricanswer WHERE potentialmetricanswer.id='" + metricId + "'";
        Query query = session.createSQLQuery(sql);
        BigInteger interCount = (BigInteger) query.uniqueResult();
        return interCount;

    }

    public BigInteger countInterviewee(long surveyId) {
        String sql = "SELECT COUNT(DISTINCT interwivee_id) FROM questionanswers WHERE questionanswers.survey_id='" + surveyId + "'";
        Query query = session.createSQLQuery(sql);
        BigInteger interCount = (BigInteger) query.uniqueResult();
        return interCount;
    }

    public Survey createNewSurvey(Survey survey) {
        Transaction txn = session.getTransaction();
        txn.begin();
        session.persist(survey);
        txn.commit();
        return survey;
    }

    public List<Survey> getLatestSurveys() {
        String sqlQuery = "SELECT title, id FROM surveys ORDER BY id DESC LIMIT 6";
        Query query = session.createSQLQuery(sqlQuery);
        List<Survey> latestSurveys = query.list();
        return latestSurveys;
    }

    public List<Survey> getMostPopularSurveys() {
        String sqlQuery = "SELECT title, survey_id, COUNT(*) AS numOfInterviewees FROM (SELECT title, survey_id, interwivee_id"
                + " FROM surveys INNER JOIN questionanswers ON surveys.id = questionanswers.survey_id GROUP BY"
                + " title, survey_id, interwivee_id) AS list GROUP BY title, survey_id ORDER BY numOfInterviewees DESC LIMIT 6";
        Query query = session.createSQLQuery(sqlQuery);
        List<Survey> mostPopularSurveys = query.list();
        return mostPopularSurveys;
    }

    public Collection<Survey> findSurveyByKeyWord(String keyWord) {
        String sqlQuery = "SELECT title, id FROM surveys WHERE UPPER (title) LIKE UPPER ('%" + keyWord + "%') "
                + "OR UPPER (description) LIKE UPPER ('%" + keyWord + "%')";
        Query query = session.createSQLQuery(sqlQuery);
        List<Survey> foundedSurveys = query.list();
        return foundedSurveys;
    }

    public Collection<Survey> getSurveyByTitle(String title) {
        String hqlQuery = "FROM Survey S WHERE LOWER(S.title) = '" + title.toLowerCase() + "'";
        Query query = session.createQuery(hqlQuery);
        List<Survey> foundedSurveys = query.list();
        return foundedSurveys;
    }
}

