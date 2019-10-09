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
              <div class="content is-center" v-if="!this.surveyCode">
                <h2 class="title is-2">Create A Survey</h2>
                <b-field label="Choose a perspective for your survey!">
                  <b-input v-model="perspective"></b-input>
                </b-field>
                <label class="label">Upload your Images!</label>
                <b-field>
                  <b-upload accept="image/*" drag-drop multiple v-model="droppedFiles">
                    <section class="section">
                      <div class="content has-text-centered">
                        <p>
                          <b-icon icon="upload" size="is-large"></b-icon>
                        </p>
                        <p>Drop your files here or click to upload</p>
                      </div>
                    </section>
                  </b-upload>
                </b-field>
                <h1 v-if="this.droppedFiles.length > 0">
                  You have selected {{this.droppedFiles.length}}
                  images to upload!
                </h1>
                <button @click="deleteImages()" class="button is-text">Remove Selected Images</button>
                <br />
                <br />
                <div class="columns is-desktop">
                  <div class="column">
                    <button @click="back()" class="button is-primary is-medium">Back</button>
                  </div>
                  <div class="column">
                    <button
                      :disabled="this.droppedFiles.length === 0"
                      @click="create()"
                      class="button is-primary is-medium"
                    >Create Survey</button>
                  </div>
                </div>
              </div>
              <div class="content is-center" v-if="this.surveyCode">
                <h1>Your surveyCode: {{this.surveyCode}}</h1>
                <div class="columns is-desktop">
                  <div class="column">
                    <button
                      @click="manageSurvey()"
                      class="button is-primary is-medium"
                    >Manage Survey</button>
                  </div>
                  <div class="column">
                    <button @click="createAnother()" class="button is-primary is-medium">
                      Create another
                      Survey
                    </button>
                  </div>
                  <div class="column">
                    <button @click="vote()" class="button is-primary is-medium">
                      Vote
                    </button>
                  </div>
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
import { createImage, createSurvey, startSurvey } from '@/app/api/survey.api';
import { CreateSurveyRequest } from '@/app/models/create-survey-request';
import { CreateImageRequest } from '@/app/models/create-image-request';

@Component({
  components: {},
})
export default class CreateSurveyContainer extends Vue {
  public perspective: string = '';
  public droppedFiles: File[] = [];
  public surveyCode = '';
  public isLoading = false;

  public async create() {
    this.isLoading = true;
    const createSurveyRequest: CreateSurveyRequest = new CreateSurveyRequest();
    createSurveyRequest.perspective = this.perspective;
    try {
      const response = await createSurvey(createSurveyRequest);
      this.surveyCode = response.code;
      const allBase64 = await Promise.all(
        this.droppedFiles.map(f => this.toBase64(f)),
      );
      await Promise.all(
        allBase64.map(async file => {
          const createImageRequest: CreateImageRequest = {
            file,
          };
          await createImage(createImageRequest, response.code);
        }),
      );
      await startSurvey(response.code);
      this.isLoading = false;
    } catch (error) {
      this.isLoading = false;
    }
  }

  public createAnother() {
    location.reload();
  }

  public deleteImages() {
    this.droppedFiles = [];
  }

  private toBase64(file: File): Promise<string> {
    return new Promise((resolve, reject) => {
      const reader = new FileReader();
      reader.readAsDataURL(file);
      reader.onload = () => resolve(reader.result as string);
      reader.onerror = error => reject(error);
    });
  }

  public back() {
    this.$router.push('/');
  }

  public manageSurvey() {
    this.$router.push(his.surveyCode + '/admin');
  }

  public vote() {
    this.$router.push(this.surveyCode + '/vote');
  }
}
</script>
