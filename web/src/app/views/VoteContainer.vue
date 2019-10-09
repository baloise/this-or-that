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
            <div
              class="column is-two-thirds-tablet is-half-desktop"
              v-if="this.voteResponse != null"
            >
              <!-- <div class="tile box has-background-light"> -->
              <div class="content is-center">
                <h2 class="title is-2">{{this.voteResponse.perspective}}</h2>
                <div class="columns is-desktop" v-if="this.voteResponse.id1 != null">
                  <div class="column">
                    <img class="element" @click="vote(1)" :src="this.getImageURL1()" />
                  </div>
                  <div class="column">
                    <img class="element" @click="vote(2)" :src="this.getImageURL2()" />
                  </div>
                </div>
                <div class="columns is-desktop" v-if="this.voteResponse.id1 == null">
                  <div class="column">
                    <h2 class="title is-2">Its over ...</h2>
                    <p>... survay has already finished, but you can view the results</p>
                    <button @click="manageSurvey()" class="button is-primary is-medium">View survey results</button>
                  </div>
                </div>
              </div>
              <div class="modal">
                <div class="modal-background"></div>
                <div class="modal-content">
                  <p class="image is-4by3">
                    <img src="../../assets/error.png" alt />
                  </p>
                </div>
                <button class="modal-close is-large" aria-label="close"></button>
              </div>
              <!-- </div> -->
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
import { getImageURL, getVote, setVote } from '@/app/api/survey.api';
import { VoteRequest } from '@/app/models/vote-request';
import { VoteResponse } from '@/app/models/vote-response';

@Component
export default class VoteContainer extends Vue {
  public isLoading = false;
  public voteResponse: VoteResponse | null = null;

  public getImageURL1() {
    if (this.voteResponse != null) {
      return getImageURL(this.$route.params.surveyCode, this.voteResponse.id1);
    }
    return null;
  }

  public getImageURL2() {
    if (this.voteResponse != null) {
      return getImageURL(this.$route.params.surveyCode, this.voteResponse.id2);
    }
    return null;
  }

  public async mounted() {
    this.isLoading = true;
    try {
      this.voteResponse = await getVote(this.$route.params.surveyCode);
    } catch (error) {
      if (error.response.status === 404) {
            this.$router.push('/404');
      }
    } finally {
      this.isLoading = false;
    }
  }

  public async vote(selectedImage: number) {
    this.isLoading = true;
    try {
      if (this.voteResponse != null) {
        const winner =
          selectedImage === 1 ? this.voteResponse.id1 : this.voteResponse.id2;
        const loser =
          selectedImage === 1 ? this.voteResponse.id2 : this.voteResponse.id1;
        const voteRequest: VoteRequest = { winner, loser };
        await setVote(this.$route.params.surveyCode, voteRequest);
        this.voteResponse = await getVote(this.$route.params.surveyCode);
      }
    } finally {
      this.isLoading = false;
    }
  }

  public manageSurvey() {
    this.$router.push('admin');
  }
}
</script>
