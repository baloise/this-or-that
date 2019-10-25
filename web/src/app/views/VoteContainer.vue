<template>
    <section id="vote">
        <section class="hero is-info is-bold is-fullheight-with-navbar">
            <div class="hero-body">
                <div class="container has-text-centered">
                    <div v-if="voteResponse && voteResponse.id1 != null">
                        <h1 class="title">
                            {{ voteResponse.perspective }}
                            <small class="is-hidden-tablet">(Votes: {{submittedVotes}})</small>
                        </h1>
                        <h1 class="subtitle is-hidden-mobile">Submitted votes: {{submittedVotes}}</h1>
                        <div class="columns" :class="isLandscape ? 'is-mobile':''">
                            <div class="column is-half">
                                <div class="box">
                                    <img class="element"
                                         @click="vote(1)"
                                         :src="getImageURL1()"/>
                                </div>
                            </div>
                            <div class="column is-half">
                                <div class="box">
                                    <img class="element"
                                         @click="vote(2)"
                                         :src="getImageURL2()"/>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="columns is-centered has-text-centered"
                         v-if="isOver || (voteResponse &&voteResponse.id1 == null)">
                        <div class="column">
                            <h1 class="title">Its over ...</h1>
                            <h1 class="subtitle">... survay has already finished, but you can view the results</h1>

                            <b-button type="is-info"
                                      size="is-medium"
                                      inverted
                                      @click="manageSurvey()">
                                View survey results
                            </b-button>
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
    import {getImageURL, getVote, setVote} from '@/app/api/survey.api';
    import {VoteRequest} from '@/app/models/vote-request';
    import {VoteResponse} from '@/app/models/vote-response';
    import Layout from '@/app/components/layout.vue';

    @Component({
        components: {Layout},
    })
    export default class VoteContainer extends Vue {
        public isLoading = true;
        public isOver = false;
        public isLandscape = false;
        public voteResponse: VoteResponse | null = null;
        public submittedVotes: number = 0;

        public getImageURL1() {
            if (this.voteResponse != null) {
                return getImageURL(
                    this.$route.params.surveyCode,
                    this.voteResponse.id1,
                );
            }
            return null;
        }

        public getImageURL2() {
            if (this.voteResponse != null) {
                return getImageURL(
                    this.$route.params.surveyCode,
                    this.voteResponse.id2,
                );
            }
            return null;
        }

        public async mounted() {
            this.isLoading = true;
            this.isLandscape = window.innerHeight < window.innerWidth;
            window.addEventListener('resize', () => {
                this.isLandscape = window.innerHeight < window.innerWidth;
            }, false);
            try {
                this.voteResponse = await getVote(this.$route.params.surveyCode);
                if (
                    localStorage.getItem(
                        `this-or-that:${this.$route.params.surveyCode}`,
                    ) !== null
                ) {
                    this.submittedVotes = parseInt(
                        localStorage.getItem(
                            `this-or-that:${this.$route.params.surveyCode}`,
                        )!,
                        10,
                    );
                }
            } catch (error) {
                if (
                    'response' in error &&
                    'status' in error.response &&
                    error.response.status === 404
                ) {
                    this.$router.push('/404');
                    return;
                }
                if (
                    'response' in error &&
                    'status' in error.response &&
                    error.response.status === 400
                ) {
                    this.isOver = true;
                    return;
                }
                throw error;
            } finally {
                this.isLoading = false;
            }
        }

        public async vote(selectedImage: number) {
            this.isLoading = true;
            try {
                if (this.voteResponse != null) {
                    const winner =
                        selectedImage === 1
                            ? this.voteResponse.id1
                            : this.voteResponse.id2;
                    const loser =
                        selectedImage === 1
                            ? this.voteResponse.id2
                            : this.voteResponse.id1;
                    const voteRequest: VoteRequest = {winner, loser};
                    await setVote(this.$route.params.surveyCode, voteRequest);
                    this.voteResponse = await getVote(
                        this.$route.params.surveyCode,
                    );
                    this.submittedVotes += 1;
                    localStorage.setItem(
                        `this-or-that:${this.$route.params.surveyCode}`,
                        this.submittedVotes.toString(),
                    );
                }
            } finally {
                this.isLoading = false;
            }
        }

        public manageSurvey() {
            this.$router.push('admin');
        }

        public back() {
            this.$router.push('/');
        }
    }
</script>

<style lang="scss" scoped>
    @import "~bulma/sass/utilities/all";

    .box {
        cursor: pointer;

        .element {
            border: none;
            box-shadow: none;
        }

        img {
            max-height: 400px;
        }

    }

    @include mobile() {
        .hero-body {
            padding: 1.5rem 1.5rem;
        }

        .box {
            padding: 0.8rem;

            img {
                max-height: 200px;
            }
        }

        .title {
            font-size: 1.2rem;
        }

        .subtitle {
            font-size: 1rem;
        }
    }

</style>

