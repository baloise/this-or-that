<template>
    <section id="home">
        <Header></Header>
        <section class="hero is-light is-bold is-fullheight-with-navbar">
            <div class="hero-body">
                <div class="container has-text-centered">
                    <h1 class="title">Make your prioritization</h1>
                    <h2 class="subtitle">With This-or-That, prioritizing is fun and fast.</h2>
                    <br>
                    <br>
                    <div class="columns">
                        <div class="column is-half">
                            <div class="box" style="min-height: 245px">
                                <div class="content is-center">
                                    <img alt="robot" width="160px"
                                         src="../../assets/this_or_that_robot_tranparent.gif"/>
                                    <h4 class="title is-5 has-text-grey"></h4>
                                    <button @click="create()" class="button is-info is-medium">Create a Survey</button>
                                </div>
                            </div>
                        </div>
                        <div class="column is-half">
                            <div class="box" style="min-height: 245px">
                                <div class="content is-center">
                                    <h4 class="title is-5 has-text-grey">Enter your Survey Code</h4>
                                    <b-field  style="margin-top: 40px">
                                        <b-input placeholder="Survey Code"
                                                 v-model="surveyCode"
                                                 size="is-medium">
                                        </b-input>
                                    </b-field>
                                    <div class="columns" style="margin-top: 40px">
                                        <div class="column is-half">
                                            <button :disabled="surveyCode.length === 0"
                                                    @click="vote()"
                                                    class="button is-primary is-medium is-fullwidth">
                                                Let's Vote
                                            </button>
                                        </div>
                                        <div class="column is-half">
                                            <button :disabled="surveyCode.length === 0"
                                                    @click="manageSurvey()"
                                                    class="button is-danger is-medium is-fullwidth">
                                                Close Survey
                                            </button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                </div>
            </div>
        </section>

    </section>
</template>

<script lang="ts">
    import {Component, Vue} from 'vue-property-decorator';
    import Header from '@/app/components/Header.vue';

    @Component({
        components: {Header},
    })
    export default class HomeContainer extends Vue {
        public surveyCode = '';

        public create() {
            this.$router.push('create');
        }

        public vote() {
            this.$router.push(this.surveyCode + '/vote');
        }

        public manageSurvey() {
            this.$router.push(this.surveyCode + '/admin');
        }
    }
</script>
