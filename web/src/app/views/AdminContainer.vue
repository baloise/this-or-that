<template>
  <div class="home">
    <section class="hero is-light is-bold is-fullheight">
      <div class="hero-header">
        <img alt="logo" src="../../assets/logo.png" style="margin: 5px;" width="120px" />
      </div>
      <div class="hero-body" style="align-items: flex-start;">
        <div class="container">
          <div class="columns">
            <div class="column"></div>
            <div class="column is-two-thirds-tablet is-half-desktop">
              <div class="content is-center" v-if="this.score != null">
                <h2
                  class="title is-2"
                  v-if="this.score.surveyIsRunning"
                >Your survey: {{this.voteResponse.perspective}}</h2>
                <h2
                  class="title is-2"
                  v-if="!this.score.surveyIsRunning"
                >Your survey: {{this.voteResponse.perspective}}</h2>
                <div class="content is-center" v-if="this.score.surveyIsRunning">
                  <h1>The survey hasn't finished yet.</h1>
                  <qrcode-vue :value="$route.params.surveyCode" level="H"></qrcode-vue>
                  <br />
                  <button @click="finishSurvey()" class="button is-primary is-medium">Finish survey</button>
                </div>
                <div class="content is-center" v-if="!this.score.surveyIsRunning">
                  <p>Number of participants: {{this.score.numberOfUsers}}</p>
                  <p>Number of votes: {{this.score.numberOfVotes}}</p>
                  <div class="columns is-desktop">
                    <div
                      class="column"
                      v-for="(score, idx) in this.score.scores"
                      v-bind:key="score.imageId"
                    >
                      <div class="vote-result">
                        <img class="element" :src="getImageURL(idx)" />
                        <div class="score">Score: {{score.score}}</div>
                      </div>
                    </div>
                  </div>
                </div>
                <br />
                <br />
                <div class="column">
                  <button @click="back()" class="button is-primary is-medium">Back</button>
                </div>
              </div>
            </div>
            <div class="column"></div>
          </div>
        </div>
      </div>
    </section>
    <b-loading :active.sync="isLoading" :can-cancel="true" :is-full-page="true"></b-loading>
  </div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import {
  getImageURL,
  createImage,
  stopSurvey,
  getScore,
  getVote,
} from '@/app/api/survey.api';
import { ErrorCode, getErrorCode } from '@/app/api/error';
import { CreateSurveyRequest } from '@/app/models/create-survey-request';
import { CreateImageRequest } from '@/app/models/create-image-request';
import { ScoreResponse } from '@/app/models/score-response';
import { VoteResponse } from '@/app/models/vote-response';
import QrcodeVue from 'qrcode.vue'

@Component({
  components: {QrcodeVue},
})
export default class AdminContainer extends Vue {
  public isLoading = false;
  public score: ScoreResponse | null = null;
  public voteResponse: VoteResponse | null = null;
  public voteURL: string | null = null;

  public getImageURL(idx: number) {
    if (this.score != null) {
      return getImageURL(
        this.$route.params.surveyCode,
        this.score.scores[idx].imageId,
      );
    }
    return null;
  }

  public async mounted() {
    this.isLoading = true;
    try {
      this.voteResponse = await getVote(this.$route.params.surveyCode);
      this.score = await getScore(this.$route.params.surveyCode);
    } catch (error) {
      if (error.response.status === 404) {
            this.$router.push('/404');
      }
    } finally {
      this.isLoading = false;
    }
  }

  public async finishSurvey() {
    this.isLoading = true;
    try {
      await stopSurvey(this.$route.params.surveyCode);
      this.score = await getScore(this.$route.params.surveyCode);
    } finally {
      this.isLoading = false;
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

  .score {
    position: absolute;
    z-index: 5;
    top: 3px;
    left: 3px;
    padding: 2px;
    background: rgba(255, 255, 255, 0.5);
  }
}
</style>
