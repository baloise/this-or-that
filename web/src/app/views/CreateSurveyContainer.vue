<template>
    <section id="about">
        <section class="hero is-light is-bold is-fullheight-with-navbar">
            <div class="hero-body">
                <div class="container">
                    <div class="columns">
                        <div class="column">
                            <div class="box" v-if="!this.surveyCode">
                                <h1 class="title is-3">Create A Survey</h1>
                                <b-field label="Choose a perspective for your survey!">
                                    <b-input v-model="perspective"
                                             :disabled="isLoading"
                                             placeholder="Enter a survey name"></b-input>
                                </b-field>

                                <div class="field">
                                    <b-field class="custom-upload-expanded" label="Upload your Images!"
                                             :expanded="true">
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
                                </div>

                                <div class="columns">
                                    <div class="column">
                                        <label class="has-text-grey" v-if="this.droppedFiles.length > 0">
                                            You have selected {{this.droppedFiles.length}}
                                            images to upload!
                                        </label>
                                    </div>
                                    <div class="column has-text-right">
                                        <button v-if="droppedFiles.length > 0"
                                                :disabled="isLoading"
                                                @click="deleteDroppedFiles()"
                                                class="button is-text"
                                        >Remove All Images
                                        </button>
                                    </div>
                                </div>

                                <div class="tags">
                                                <span v-for="(file, index) in droppedFiles" :key="index"
                                                      class="tag is-primary">
                                                    {{file.name}}
                                                    <button
                                                            class="delete is-small"
                                                            type="button"
                                                            @click="deleteDroppedFile(index)"
                                                    ></button>
                                                </span>
                                </div>

                                <div class="buttons">
                                    <b-button type="is-primary"
                                              :disabled="this.droppedFiles.length === 0 || isLoading"
                                              @click="create()"
                                              size="is-medium">Create Survey
                                    </b-button>

                                    <b-button type="is-info"
                                              :disabled="isLoading"
                                              @click="back()"
                                              size="is-medium">Cancel
                                    </b-button>
                                </div>

                            </div>
                            <div class="box has-text-centered" v-if="this.surveyCode && !this.isLoading">
                                <h3 class="title is-3">Survey is created</h3>
                                <h3 class="subtitle is-3">Your surveyCode: {{this.surveyCode}}</h3>
                                <qrcode-vue :value="this.qrCodeUrl" size="200" level="H"></qrcode-vue>
                                <br/>
                                <div class="field is-grouped is-grouped-centered">
                                    <div class="buttons">
                                        <b-button type="is-primary" @click="vote()" size="is-medium">
                                            Let's Vote
                                        </b-button>
                                        <b-button type="is-info" @click="createAnother()" size="is-medium">
                                            Create another
                                        </b-button>
                                        <b-button type="is-danger" @click="manageSurvey()" size="is-medium">
                                            Close Survey
                                        </b-button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </section>
        <b-loading :active.sync="isLoading" :can-cancel="true" :is-full-page="true"></b-loading>
    </section>
</template>

<script lang="ts">
    import {Component, Vue} from 'vue-property-decorator';
    import Layout from '@/app/components/layout.vue';
    import {createImage, createSurvey, startSurvey} from '@/app/api/survey.api';
    import {CreateSurveyRequest} from '@/app/models/create-survey-request';
    import {CreateImageRequest} from '@/app/models/create-image-request';
    import QrcodeVue from 'qrcode.vue';

    @Component({
        components: {QrcodeVue, Layout},
    })
    export default class CreateSurveyContainer extends Vue {
        public perspective: string = '';
        public droppedFiles: File[] = [];
        public surveyCode: string = '';
        public qrCodeUrl: string = '';
        public isLoading = false;

        public async create() {
            this.isLoading = true;
            const createSurveyRequest: CreateSurveyRequest = new CreateSurveyRequest();
            createSurveyRequest.perspective = this.perspective;
            try {
                const response = await createSurvey(createSurveyRequest);
                this.surveyCode = response.code;
                const href = window.location.href;
                this.qrCodeUrl =
                    href.split('#')[0] + '#/' + this.surveyCode + '/vote';
                const allBase64 = await Promise.all(
                    this.droppedFiles.map(f => this.toCroppedBase64(f)),
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
            } finally {
                this.isLoading = false;
            }
        }

        public createAnother() {
            location.reload();
        }

        public deleteDroppedFiles() {
            this.droppedFiles = [];
        }

        public deleteDroppedFile(index: number) {
            this.droppedFiles.splice(index, 1);
        }

        private toCroppedBase64(file: File): Promise<string> {
            const maxHeight = 512;
            return new Promise((resolve, reject) => {
                const reader = new FileReader();
                reader.readAsDataURL(file);
                reader.onload = event => {
                    const img = new Image();
                    img.src = event.target!.result as string;
                    img.onload = () => {
                        const height = Math.min(maxHeight, img.height);
                        const width = (img.width * height) / img.height;
                        const xPadding = Math.max(0, (img.width - width) / 2);
                        const yPadding = Math.max(0, (img.height - height) / 2);
                        const elem = document.createElement('canvas');
                        elem.width = width + 2 * xPadding;
                        elem.height = height + 2 * yPadding;
                        const ctx = elem.getContext('2d')!;
                        ctx.fillStyle = 'white';
                        ctx.fillRect(
                            0,
                            0,
                            width + 2 * xPadding,
                            height + 2 * yPadding,
                        );
                        ctx.drawImage(img, xPadding, yPadding, width, height);
                        resolve(elem.toDataURL('image/jpg', 1));
                    };
                };
                reader.onerror = error => reject(error);
            });
        }

        public back() {
            this.$router.push('/');
        }

        public manageSurvey() {
            this.$router.push(this.surveyCode + '/admin');
        }

        public vote() {
            this.$router.push(this.surveyCode + '/vote');
        }
    }
</script>
