<template>
    <section id="admin">
        <Header></Header>
        <section class="hero is-info is-bold is-fullheight-with-navbar">
            <div class="hero-body">
                <div class="container">
                    <div class="has-text-centered" v-if="score">
                        <h3 class="title is-3">Your survey: {{ this.$route.params.surveyCode }}</h3>
                        <h4 class="subtitle is-4">{{ score.perspective }}</h4>
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
                                    <p class="title">{{score.numberOfUsers}}</p>
                                </div>
                            </div>
                            <div class="level-item has-text-centered">
                                <div>
                                    <p class="heading">Votes</p>
                                    <p class="title">{{score.numberOfVotes}}</p>
                                </div>
                            </div>
                        </nav>

                        <hr>

                        <div class="columns is-mobile is-multiline" v-if="score && score.scores">
                            <div class="column is-half is-one-quarter-desktop" v-for="(item, index) in score.scores"
                                 :key="item.imageId">
                                <figure class="image is-square" style="position: relative">
                                    <img :src="getImageURL(item.imageId)" :alt="'Image ' + (index+1)">
                                    <div class="score has-text-grey">Score: <b>{{item.score}}</b></div>
                                </figure>
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
    import Header from '@/app/components/Header.vue';

    @Component({
        components: {Header, QrcodeVue},
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
    .score {
        position: absolute;
        top: 0;
        left: 0;
        z-index: 5;
        padding: 0.6rem;
        background: rgba(255, 255, 255, 0.75);
    }
</style>
