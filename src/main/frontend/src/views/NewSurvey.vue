<template>
<div id="new_survey">
    <br>
    <DescriptionPanel @getSurveyDescription="getSurveyDescriptionFromChild($event)" />
    <br>
    <CreateMetric @getMetricQuestions="getMetricQuestionsFromChild($event)" @failure="failure($event)" />
    <br>
    <CreateSurvey @getSurveyQuestions="getSurveyQuestionsFromChild($event)" @failure="failure($event)" />
    <br>
    <vue-recaptcha sitekey="6LcsB9YZAAAAAP8R-Xq0Ff0BkZv2LKfz6cS2OhZv"></vue-recaptcha>
    <br>
    <div class="row">
        <div class="column"><button class="button-blue" id="previewSurveyButton" style="float:left" @click="previewSurvey">{{previewButtonText}}</button></div>
        <div class="column"><button class="button-yellow" id="createSurveyButton" @click="submitSurvey">Create the survey</button> </div>
    </div>
    <br>

    <SurveyPreview v-if="preview" :survey="survey"></SurveyPreview>
</div>
</template>

<script>
import CreateMetric from './NewSurvey/CreateMetric';
import CreateSurvey from './NewSurvey/CreateSurvey';
import DescriptionPanel from './NewSurvey/DescriptionPanel';
import SurveyPreview from './SurveyPreview';
import {
    dataBus
} from '../main';

export default {
    components: {
        CreateMetric,
        CreateSurvey,
        DescriptionPanel,
        SurveyPreview,
        'vue-recaptcha': VueRecaptcha,
    },
    props: ['surveyID'],
    data() {
        return {
            survey: {
                title: '',
                description: '',
                questions: [],
                metrics: []
            },
            preview: false,
            previewButtonText: "preview"
        }
    },
    methods: {
        getSurveyDescriptionFromChild(description) {
            this.survey.title = description.title;
            this.survey.description = description.description;
        },

        getMetricQuestionsFromChild(questions) {
            this.survey.metrics = questions;
        },

        getSurveyQuestionsFromChild(questions) {
            this.survey.questions = questions;
        },

        callForSurveyElements() {
            dataBus.$emit('callForSurveyElements');

            var isSurveyComplet = true;
            if (this.survey.title.trim() === '' || this.survey.description.trim() === '') {
                isSurveyComplet = false;
            }
            if (this.survey.questions.length <= 0) {
                isSurveyComplet = false;
            }
            if (this.survey.metrics.length <= 0) {
                isSurveyComplet = false;
            }

            if (isSurveyComplet) {
                this.isDbContainsTitle(this.survey.title);
            } else {
                this.failure('To create new survey you need to fulfill all the inputs!')
            }
        },

        isDbContainsTitle(title) {
            this.warning('Please wait...');
            var titlesFromDb;
            var url = "survey/findbytitle/" + title;
            this.$http.get(url)
                .then(response => {
                    titlesFromDb = response.body;
                    if (titlesFromDb.length > 0) {
                        this.failure("A survey with that title already exists!");
                    } else {
                        this.createSurvey();
                    }
                })
                .catch(response => {
                    //TODO
                });
        },

        createSurvey() {
            this.warning("creating the survey...")
            this.$http.post('survey', this.survey)
                .then(response => {
                    this.surveyID = response.body.id;
                    this.success("Congratulations! New survey created. You can try it with ID: " + this.surveyID);
                    document.body.scrollTop = document.documentElement.scrollTop = 0;
                    this.$emit('goToFillSurvey', this.surveyID);
                })
                .catch(response => {
                    this.failure("Something went wrong. The survey couldn't be created.");
                    document.body.scrollTop = document.documentElement.scrollTop = 0;
                });
        },

        previewSurvey() {
            this.preview = !this.preview;
            if (this.preview) {
                this.previewButtonText = "hide preview"
            } else {
                this.previewButtonText = "preview"
            }
        },

        success(message) {
            this.$emit("success", message);
        },

        failure(message) {
            this.$emit("error", message);
        },

        warning(message) {
            this.$emit("warning", message);
        },

        submitSurvey() {
            this.verifyRecaptcha();
        },

        verifyRecaptcha() {
            var recaptchaToken = grecaptcha.getResponse();
            if (recaptchaToken != '') {
                this.$http.post('recaptcha/' + recaptchaToken)
                    .then(response => {
                        var isSuccess = response.body;
                        if (isSuccess) {
                            this.callForSurveyElements();
                        } else {
                            this.failure('reCAPTCHA verification failure! You are a robot!');
                        }
                    })
                    .catch(response => {
                        //TODO
                    });
            } else {
                this.failure("'I am not a robot' reCAPTCHA unselected!'");
            }
        }
    }
};
</script>

<style>

</style>
