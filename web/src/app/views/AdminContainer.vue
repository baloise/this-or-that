<template>
    <section id="admin">
        <section class="hero is-info is-bold is-fullheight-with-navbar">
            <div class="hero-body">
                <div class="container">
                    <div class="has-text-centered">
                        <h3 class="title is-3">Your survey: {{ this.$route.params.surveyCode }}</h3>
                        <h4 class="subtitle is-4">{{ this.score.perspective }}</h4>
                        <b-button type="is-info"
                                  size="is-medium"
                                  inverted
                                  @click="back()">
                            Back
                        </b-button>
                        <hr>

                        <nav class="level">
                            <div class="level-item has-text-centered">
                                <div>
                                    <p class="heading">Participants</p>
                                    <p class="title">{{this.score.numberOfUsers}}</p>
                                </div>
                            </div>
                            <div class="level-item has-text-centered">
                                <div>
                                    <p class="heading">Votes</p>
                                    <p class="title">{{this.score.numberOfVotes}}</p>
                                </div>
                            </div>
                        </nav>

                        <hr>

                        <div class="columns is-vcentered is-centered is-multiline" v-if="score && score.scores">
                            <div class="column is-one-quarter" v-for="item in score.scores" v-bind:key="item.imageId">
                                <div class="vote-result box">
                                    <img :src="getImageURL(item.imageId)" alt="img" class="element"/>
                                    <div class="score has-text-grey">Score: <b>{{item.score}}</b></div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <b-loading :active.sync="isLoading" :can-cancel="true" :is-full-page="true"></b-loading>
        </section>
    </section>
</template>

<script lang="ts">
    import {Component, Vue} from 'vue-property-decorator';
    import {getImageURL, getScore, stopSurvey} from '@/app/api/survey.api';
    import {ScoreResponse} from '@/app/models/score-response';
    import QrcodeVue from 'qrcode.vue';
    import Layout from '@/app/components/layout.vue';

    @Component({
        components: {QrcodeVue, Layout},
    })
    export default class AdminContainer extends Vue {
        public isLoading = false;
        public score: ScoreResponse | null = null;

        public getImageURL(imageId: string) {
            if (this.score != null) {
                return getImageURL(this.$route.params.surveyCode, imageId);
            }
            return null;
        }

        public async mounted() {
            this.isLoading = true;
            try {
                await this.stopSurveyIfNotClosed();
                this.score = await getScore(this.$route.params.surveyCode);
            } catch (error) {
                if (
                    'response' in error &&
                    'status' in error.response &&
                    error.response.status === 404
                ) {
                    this.$router.push('/404');
                    return;
                }
                throw error;
            } finally {
                this.isLoading = false;
            }
        }

        public async stopSurveyIfNotClosed() {
            try {
                await stopSurvey(this.$route.params.surveyCode);
            } catch (error) {
                if (
                    'response' in error &&
                    'status' in error.response &&
                    error.response.status === 400
                ) {
                    return;
                }
                throw error;
            }
        }

        public back() {
            this.$router.push('/');
        }
    }
</script>

<style lang="scss" scoped>
    .vote-result {
        position: relative;

        .element {
            border: none;
            box-shadow: none;
        }

        .score {
            position: absolute;
            z-index: 5;
            top: 3px;
            left: 3px;
            padding: 0.6rem;
            background: rgba(255, 255, 255, 0.5);
        }
    }
</style>
