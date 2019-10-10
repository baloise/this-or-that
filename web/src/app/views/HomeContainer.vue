<template>
    <div class="home">
        <section class="hero is-light is-bold is-fullheight">
            <div class="hero-header">
                <img src="../../assets/logo.png" alt="logo" width="120px" style="margin: 5px;"/>
            </div>
            <div class="hero-body" style="align-items: flex-start;">
                <div class="container">
                    <div class="columns">
                        <div class="column"></div>
                        <div class="column is-two-thirds-tablet is-half-desktop">
                            <!-- <div class="tile box has-background-light"> -->
                            <div class="content is-center">
                                <h2 class="title is-2">Make your prioritization</h2>
                                <h4 class="subtitle is-4">With This-or-That, prioritizing is fun and fast.</h4>
                                <br/>
                                <br/>
                                <button @click="create()" class="button is-primary is-medium">Create Survey</button>
                                <br/>
                                <br/>
                                <b-field label="Survey Code">
                                    <b-input v-model="surveyCode"></b-input>
                                </b-field>

                                <div class="columns is-desktop">
                                    <div class="column">
                                        <button :disabled="surveyCode.length === 0" @click="vote()"
                                                class="button is-primary is-medium">Vote
                                        </button>
                                    </div>
                                    <div class="column">
                                        <button @click="manageSurvey()" :disabled="surveyCode.length === 0" class="button is-primary is-medium">
                                            Close Survey
                                        </button>
                                    </div>
                                </div>
                                <br/>
                                <br/>
                                <img alt="robot" src="../../assets/this_or_that_robot_tranparent.gif"/>
                            </div>
                            <!-- </div> -->
                        </div>
                        <div class="column"></div>
                    </div>
                </div>
            </div>
        </section>
    </div>
</template>

<script lang="ts">
import {Component, Vue} from 'vue-property-decorator';

    @Component
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
