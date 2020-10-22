<template>
    <div id="statistics">
        <br>

        <h4>Total number of voters: <b>{{statistic.numberOfAnswers}}</b></h4>
<!--        <p1>{{this.chosenMetrics}}</p1>-->

        <div class="surveyCreatorComponent">
            <p class="mainNames">Metric filter</p>
            <table class="d">
                <tr>
                    <td class="wide">Metric Question:</td>
                    <td align="center" >Metrics Filter:</td>
                </tr>
                <tr v-for="metric in statistic.survey.metrics" :key="metric.id">
                    <td class="odpowiedziText">{{ metric.metricText }}</td>
                    <td>
                        <table class="noMargin">
                            <tbody class="d">
                            <tr class="odpowiedziMetrics" v-for="potentialMetricAnswer in metric.potentialMetricAnswers" v-bind:key="metric.id">

                                <input type="checkbox" style="margin: 0px 0px 0px 0px" v-model="chosenMetrics" :value="potentialMetricAnswer.id">
                                {{potentialMetricAnswer.text}}
                            </tr>
                            </tbody>
                        </table>
                    </td>
                </tr>
            </table>
        </div>

        <br>
        <div class="surveyCreatorComponent">
            <p class="mainNames">Survey statistics</p>
            <table class="d">
                <tr>
                    <td class="wide">Question:</td>
                    <td align="center">Question Statistics:</td>
                </tr>
                <tr v-for="question in statistic.questionStats" :key="question.id">
                    <td class="odpowiedziText">{{question.questionText}}</td>
                    <td>
                        <table class="noMargin">
                            <tr class="a">
                                <th class="topOdpowiedzi1">Answer:</th>
                                <th class="topOdpowiedzi">%</th>
                                <th class="topOdpowiedzi">Votes:</th>
                            </tr>
                            <tbody>
                            <tr  v-for="potentialQuestionAnswer in question.answerStats"
                                :key="potentialQuestionAnswer.id">
                                <td class="odpowiedzi"  >{{potentialQuestionAnswer.potentialQuestionAnswer.text}}</td>
                                <td class="innerTableWidth">{{Math.round(((potentialQuestionAnswer.numberOfAnswers /
                                    question.numberOfMetricAnswers) + Number.EPSILON) *10000)/100}} %
                                </td>
                                <td class="innerTableWidth2">{{potentialQuestionAnswer.numberOfAnswers}}</td>
                            </tr>
                            </tbody>
                        </table>
                    </td>
                </tr>

            </table>
        </div>
    </div>
</template>

<script>
    export default {
        props: ["surveyId"],

        data() {
            return {
                statistic: "",
                chosenMetrics: [],
                response: [],
            }
        },

        created() {

        },

        mounted() {
            //this.loadTest();
            this.pageStart();

        },

        computed() {
            
        },

        watch: {
            surveyId: function () {
                this.empty();
                this.loadTest();
            },

            chosenMetrics: function () {
                this.loadStat();
            }
        },

        methods: {

            // test() {
            //     this.$http.get('statistics/' + this.surveyId)
            //         .then(response => {
            //             this.testee = response.body;
            //             console.log('testOne ', response.status);
            //                 // this.testee = response.bodyText
            //         });
            // },

            
            loadTest() {

                this.warning("loading...");
                this.$http.post('statistics/' + this.surveyId + '/metrics', this.chosenMetrics).then(response => {
                    this.statistic = response.body;
                    this.success("Successfully loaded survey no. " + this.statistic.surveyId + ": " + this.statistic.surveyDescription);
                    this.fill();
                    //this.loadStat()
                })
                    .catch(response => {
                        this.failure('Error ' + response.status + ' while loading the survey statistics. No such survey ID.');
                    })
            },

            pageStart() {

                this.warning("loading...");
                this.$http.get('statistics/' + this.surveyId).then(response => {
                    this.statistic = response.body;
                    this.success("Successfully loaded survey no. " + this.statistic.surveyId + ": " + this.statistic.surveyDescription);
                    this.fill();
                    //this.loadStat()
                })
                    .catch(response => {
                        this.failure('Error ' + response.status + ' while loading the survey statistics. No such survey ID.');
                    })
            },

            loadStat() {
                this.warning("LOADING ...");
                this.$http.post('statistics/' + this.surveyId + '/metrics', this.chosenMetrics).then(response => {
                    this.statistic = response.body;
                    this.success("Updated survey no. " + this.statistic.surveyId  + ' according to ' + this.chosenMetrics.length + ' chosen filters');
                })
                    .catch(response => {
                        this.failure('Error ' + response.status + ' while loading the survey statistics. No such survey ID.');
                    })
            },

            fill() {
                for (var i in this.statistic.survey.metrics) {
                    for (var j in this.statistic.survey.metrics[i].potentialMetricAnswers) {
                        this.chosenMetrics.push(this.statistic.survey.metrics[i].potentialMetricAnswers[j].id);
                    }
                }
            },

            empty() {
              this.chosenMetrics = [];
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
        },
    };
</script>

<style lang="scss">
    #app {
        max-width: 1000px;
        margin: 0 auto;
    }

    .alert {
        padding: 10px;
        margin-bottom: 10px;
        border: 3px solid black;
        position: center;
        left: 50%;
        top: 50%;
        margin-left: 10%;
        margin-top: 10%;

    }

    .questions {
        display: flex;

        &-item {
            border: solid blue 2px;
            border-radius: 25px;
            padding: 10px;
            text-align: center;
            background-color: violet;
            color: darkblue;
            font-size: 2rem;
            margin: 10px;
            flex: 1;

            &:hover {
                background-color: blue;
                color: white;
                cursor: pointer;
            }
        }
    }

    .topOdpowiedzi1{
        padding-top: 5px;
        border-style: solid;
        border-width: 1px;
        border-color: #e1e1e1;
        border-left: 0;
        border-right: 0;
        border-bottom: 0;
        border-top: 0;
        text-align: left;
    }

    .odpowiedzi {

        padding: 0;
        border-style: solid;
        border-width: 1px;
        border-color: #e1e1e1;
        border-left: 0;
        border-right: 0;
        border-bottom: 0;


    }

    .topOdpowiedzi {
        padding-top: 5px;
        border-style: solid;
        border-width: 1px;
        border-color: #e1e1e1;
        border-left: 0;
        border-right: 0;
        border-bottom: 0;
        border-top: 0;
        text-align: center;

    }

    .odpowiedziText{
        //font-max-size:16px;
        font-size:16px;
        //width: max(20vw, 400px)
        @media screen and (max-width: 500px) {
            font-size:3vw;

        }
    }

    .odpowiedziMetrics {
        padding: 0;
        border-style: solid;
        border-width: 1px;
        border-color: #e1e1e1;
        border-left: 0;
        border-right: 0;
        border-bottom: 0;
        border-top: 0;

    }


    th, td {
        //padding: 5px;
        padding: 1px 5px;
    }



    .a {
        font-size: 12px;
    }




    table.d {
        table-layout: fixed;
        margin-left: auto;
        margin-right: auto;
        width: 95%;
    }

    table.e {
        table-layout: fixed;
        margin-left: auto;
        margin-right: auto;
        width: 100%;
    }

    .noMargin {
        margin-top: 1px;
        margin-bottom: 1px;
    }

    .f {
        text-align: center;
        vertical-align: bottom;

    }


    .wide {
        width: 30%;
    }

    .innerTableWidth {
        padding: 0;
        border-style: solid;
        border-width: 1px;
        border-color: #e1e1e1;
        border-left: 0;
        border-right: 0;
        border-bottom: 0;
        width: 6%;
        min-width: 70px;
        text-align: center;
    }

    .innerTableWidth2 {
        padding: 0;
        border-style: solid;
        border-width: 1px;
        border-color: #e1e1e1;
        border-left: 0;
        border-right: 0;
        border-bottom: 0;
        width: 6%;
        min-width: 50px;
        text-align: center;
    }


</style>
