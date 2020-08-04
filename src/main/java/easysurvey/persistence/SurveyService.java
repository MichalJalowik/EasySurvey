package easysurvey.persistence;

import easysurvey.dataModel.*;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Date;


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

    public SurveyService() {
        session = HibernateUtil.getSessionFactory().openSession();
    }

    public void close() {
        session.close();
        HibernateUtil.shutdown();
    }

    public void run() {
        createSurvey();
        fillUpSurvey();
    }

    public void createSurvey(){
        addNewSurvey("badanie kolegow","www.easysurvey.com","www.easysurvey.edit.com",LocalDate.of(2019,8,30),LocalDate.of(2025,8,30),true);

        createQuestion(1L,"jak lubisz kolor?");
        addPotentialQuestionAnswer(1L,"zielony");
        addPotentialQuestionAnswer(1L,"czarny");
        addPotentialQuestionAnswer(1L,"niebieski");

        createMetric(1L,"podaj przedial wiekowy?");
        addPotentialMetricAnswer(1L,"20-30");
        addPotentialMetricAnswer(1L,"31-40");
        addPotentialMetricAnswer(1L,"41-50");
        addPotentialMetricAnswer(1L,"51-100");

        createQuestion(1L,"dokad chcialbys pojechac na wakacje?");
        addPotentialQuestionAnswer(2L,"USA");
        addPotentialQuestionAnswer(2L,"Indonezja");
        addPotentialQuestionAnswer(2L,"Australia");
        addPotentialQuestionAnswer(2L,"Chile");

        createMetric(1L, "i ilu krajach juz bylem?");
        addPotentialMetricAnswer(2L,"1-10");
        addPotentialMetricAnswer(2L,"11-20");
        addPotentialMetricAnswer(2L,"21-50");
        addPotentialMetricAnswer(2L,"50-299");
    }

    public void fillUpSurvey(){
        addNewInterviewee("MichalJ");
        giveMetricAnswerByIntervieweeId(1L,1L,1L,2L);
        giveMetricAnswerByIntervieweeId(1L,1L,2L,7L);
        giveQuestionAnswerByIntervieweeId(1L,1L,1L,3L);
        giveQuestionAnswerByIntervieweeId(1L,1L,2L,6L);

        addNewInterviewee("Marco01");
        giveMetricAnswerByIntervieweeId(2L,1L,1L,1L);
        giveMetricAnswerByIntervieweeId(2L,1L,2L,5L);
        giveQuestionAnswerByIntervieweeId(2L,1L,1L,1L);
        giveQuestionAnswerByIntervieweeId(2L,1L,2L,6L);

        addNewInterviewee("Pawel01");
        giveMetricAnswerByIntervieweeId(3L,1L,1L,2L);
        giveMetricAnswerByIntervieweeId(3L,1L,2L,4L);
        giveQuestionAnswerByIntervieweeId(3L,1L,1L,2L);
        giveQuestionAnswerByIntervieweeId(3L,1L,2L,7L);
    }

    public void addNewInterviewee(String nickName){
        Transaction txn = session.getTransaction();
        txn.begin();

        interviewee = new Interviewee(nickName);

        session.persist(interviewee);
        txn.commit();
    }

    public void giveQuestionAnswerByIntervieweeId(Long intervieweeId ,Long surveyId, Long questionId, Long questionAnswerId ){
        Transaction txn = session.getTransaction();
        txn.begin();

        Interviewee findIntervieweeById = (Interviewee) session.get(Interviewee.class,intervieweeId);
        Survey findSurveyById = (Survey) session.get(Survey.class,surveyId);
        Question findQuestionById = (Question) session.get(Question.class,questionId);
        PotentialQuestionAnswer findPotentialQuestionAnswerById = (PotentialQuestionAnswer) session.get(PotentialQuestionAnswer.class,questionAnswerId);

        questionAnswer = new QuestionAnswer(findSurveyById,findQuestionById,findPotentialQuestionAnswerById);
        findIntervieweeById.getQuestionAnswers().add(questionAnswer);

        session.persist(questionAnswer);
        txn.commit();
    }

    public void giveMetricAnswerByIntervieweeId(Long intervieweeId ,Long surveyId, Long metricId, Long metricAnswerId ){
        Transaction txn = session.getTransaction();
        txn.begin();

        Interviewee findIntervieweeById = (Interviewee) session.get(Interviewee.class,intervieweeId);
        Survey findSurveyById = (Survey) session.get(Survey.class,surveyId);
        Metric findMetricById = (Metric) session.get(Metric.class,metricId);
        PotentialMetricAnswer findPotentialMetricAnswerById = (PotentialMetricAnswer) session.get(PotentialMetricAnswer.class,metricAnswerId);

        metricAnswer = new MetricAnswer(findSurveyById,findMetricById,findPotentialMetricAnswerById);
        findIntervieweeById.getMetricAnswers().add(metricAnswer);

        session.persist(metricAnswer);
        txn.commit();
    }

    public void addNewSurvey(String description, String surveyLink, String editLink, LocalDate startDate, LocalDate endDate, boolean isOpen){
        Transaction txn = session.getTransaction();
        txn.begin();

        survey = new Survey(description, surveyLink, editLink, startDate, endDate, isOpen);

        session.persist(survey);
        txn.commit();
    }

    public void createQuestion(Long surveyId, String questionText){
        Transaction txn = session.getTransaction();
        txn.begin();

        Survey findSurveyById = (Survey) session.get(Survey.class,surveyId);
        question = new Question(questionText);
        findSurveyById.getQuestions().add(question);

        session.persist(question);
        txn.commit();
    }

    public void addPotentialQuestionAnswer(Long questionId, String potentialQuestionAnswerText){
        Transaction txn = session.getTransaction();
        txn.begin();

        Question findQuestionById = (Question) session.get(Question.class,questionId);
        potentialQuestionAnswer = new PotentialQuestionAnswer(potentialQuestionAnswerText);
        findQuestionById.getPotentialQuestionAnswers().add(potentialQuestionAnswer);

        session.persist(potentialQuestionAnswer);
        txn.commit();
    }

    public void createMetric(Long surveyId, String metricText){
        Transaction txn = session.getTransaction();
        txn.begin();

        Survey findSurveyById = (Survey) session.get(Survey.class,surveyId);
        metric = new Metric(metricText);
        findSurveyById.getMetrics().add(metric);

        session.persist(metric);
        txn.commit();
    }

    public void addPotentialMetricAnswer(Long metricId, String potentialMetricAnswerText){
        Transaction txn = session.getTransaction();
        txn.begin();

        Metric findMetricById = (Metric) session.get(Metric.class,metricId);
        potentialMetricAnswer = new PotentialMetricAnswer(potentialMetricAnswerText);
        findMetricById.getPotentialMetricAnswers().add(potentialMetricAnswer);

        session.persist(potentialMetricAnswer);
        txn.commit();
    }
}
