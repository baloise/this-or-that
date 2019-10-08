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
                <h2 class="title is-2">Your survey: {{this.score.perspective}}</h2>
                <div class="content is-center" v-if="this.score.surveyIsRunning">
                  <h1>The servey hasn't finished yet.</h1>
                  <button @click="finishSurvey()" class="button is-primary is-medium">Finish survey</button>
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
import { Component, Vue } from "vue-property-decorator";
import { createImage, stopSurvey, getScore } from "@/app/api/survey.api";
import { ErrorCode, getErrorCode } from "@/app/api/error";
import { CreateSurveyRequest } from "@/app/models/create-survey-request";
import { CreateImageRequest } from "@/app/models/create-image-request";
import { ScoreResponse } from '@/app/models/score-response';

@Component({
  components: {}
})
export default class AdminContainer extends Vue {
  public isLoading = false;
  public score: ScoreResponse | null   = null;

  public async mounted() {
    this.isLoading = true;
    try {
      this.score = await getScore(this.$route.params.surveyCode);
      console.log(this.score);
      this.isLoading = false;
    } catch (error) {
      this.isLoading = false;
    }
  }

  public async finishSurvey() {
    this.isLoading = true;
    try {
      await stopSurvey(this.$route.params.surveyCode);
      this.score = await getScore(this.$route.params.surveyCode);
      this.isLoading = false;
    } catch (error) {
      this.isLoading = false;
    }
  }

  public back() {
    this.$router.push("/");
  }
}
</script>
