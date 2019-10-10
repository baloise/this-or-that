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
              <div class="content is-center">
                <h2 class="title is-2">Your survey: {{this.$route.params.surveyCode}}</h2>
                <div class="content is-center">
                  <p>Perspective: {{this.score.perspective}}</p>
                  <p>Number of participants: {{this.score.numberOfUsers}}</p>
                  <p>Number of votes: {{this.score.numberOfVotes}}</p>
                  <div v-for="(chunk, idx) in this.getImageRows()"
                      v-bind:key="idx">
                    <div class="columns is-desktop">
                      <div
                        class="column"
                        v-for="score in chunk"
                        v-bind:key="score.imageId">
                        <div class="vote-result">
                          <img :src="getImageURL(score.imageId)" alt="img" class="element"/>
                          <div class="score">Score: {{score.score}}</div>
                        </div>
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
import {Component, Vue} from 'vue-property-decorator';
import {getImageURL, getScore, stopSurvey} from '@/app/api/survey.api';
import {ScoreResponse} from '@/app/models/score-response';
import QrcodeVue from 'qrcode.vue';

@Component({
  components: {QrcodeVue},
})
export default class AdminContainer extends Vue {
  public isLoading = false;
  public score: ScoreResponse | null = null;
    public isSurveyRunning: boolean = false;

  public getImageURL(imageId: string) {
    if (this.score != null) {
      return getImageURL(
        this.$route.params.surveyCode,
        imageId,
      );
    }
    return null;
  }

  public async mounted() {
    this.isLoading = true;
    try {
      await stopSurvey(this.$route.params.surveyCode);
      this.score = await getScore(this.$route.params.surveyCode);
    } finally {
      this.isLoading = false;
    }
  }

  public getImageRows() {
    const chunkSize = 2;
    if (this.score != null) {
      const chunkCount = Math.ceil(this.score.scores.length / chunkSize);
      return Array.from(Array(chunkCount).keys()).map(n =>
        this.score != null ? this.score.scores.slice(n * chunkSize, (n + 1) * chunkSize) : [],
      );
    }
    return [];
  }

  public async finishSurvey() {
    this.isLoading = true;
    try {
      await stopSurvey(this.$route.params.surveyCode);
      this.score = await getScore(this.$route.params.surveyCode);
      this.isSurveyRunning = false;
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
