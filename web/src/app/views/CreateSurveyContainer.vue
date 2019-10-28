<template>
    <section id="create">
        <Header></Header>
        <section class="hero is-light is-bold is-fullheight-with-navbar">
            <div class="hero-body">
                <div class="container">
                    <div class="box" v-if="!this.surveyCode">
                        <h1 class="title is-3">Create A Survey</h1>
                        <b-field label="Choose a perspective for your survey!">
                            <b-input v-model="perspective"
                                     :disabled="isLoading"
                                     placeholder="Enter a survey name"></b-input>
                        </b-field>

                        <label class="label">Upload your Images! (Only PNG & JPG are supported)</label>
                        <div class="field" v-if="!imageSource" style="max-height: 200px">
                            <b-field class="custom-upload-expanded" :expanded="true">
                                <b-upload accept="image/x-png,image/png,image/jpeg" drag-drop v-model="imageFile">
                                    <section class="section">
                                        <div class="content has-text-centered">
                                            <p>
                                                <b-icon icon="upload" size="is-large"></b-icon>
                                            </p>
                                            <p>Drop your file here or click to upload</p>
                                        </div>
                                    </section>
                                </b-upload>
                            </b-field>
                        </div>
                        <div class="columns is-centered" v-show="imageSource && imageSource.length > 0">
                            <div class="column is-half" style="max-width: 512px">
                                <vue-cropper ref="cropper"
                                             :src="imageSource"
                                             :aspect-ratio="1 / 1"
                                             :view-mode="3"
                                             alt="">
                                </vue-cropper>

                                <br>
                                <div class="buttons">
                                    <b-button type="is-info"
                                              :disabled="isImageProcessing"
                                              :loading="isImageProcessing"
                                              @click="cropImage()">Add Image to Survey
                                    </b-button>
                                    <b-button type="is-danger"
                                              :disabled="isImageProcessing"
                                              :loading="isImageProcessing"
                                              @click="cancelCrop()">Cancel
                                    </b-button>
                                </div>
                            </div>
                        </div>

                        <p class="label" v-if="imageSources && imageSources.length > 0">Images</p>
                        <div class="columns is-mobile is-multiline">
                            <div class="column is-one-quarter" v-for="(imageSource, index) in imageSources"
                                 :key="imageSource">
                                <figure class="image is-square" style="position: relative">
                                    <button class="delete is-large"
                                            @click="deleteImage(index)"
                                            style="position: absolute; z-index: 100; top: 5px; right: 5px"></button>
                                    <img :src="imageSource" :alt="'Preview ' + (index+1)">
                                </figure>
                            </div>
                        </div>

                        <hr>

                        <div class="buttons">
                            <b-button type="is-primary"
                                      :disabled="this.imageSources.length < 2 || isLoading"
                                      :loading="isLoading"
                                      @click="create()"
                                      size="is-medium">Create Survey
                            </b-button>

                            <b-button type="is-info"
                                      :disabled="isLoading"
                                      :loading="isLoading"
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
                                <b-button type="is-danger" @click="manageSurvey()" size="is-medium">
                                    Close Survey
                                </b-button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <b-loading :active.sync="isLoading" :is-full-page="true"></b-loading>
            <b-loading :active.sync="isImageProcessing" :is-full-page="true"></b-loading>
        </section>
    </section>
</template>

<script lang="ts">
    import {Component, Ref, Vue, Watch} from 'vue-property-decorator';
    import {createImage, createSurvey, startSurvey} from '@/app/api/survey.api';
    import {CreateSurveyRequest} from '@/app/models/create-survey-request';
    import {CreateImageRequest} from '@/app/models/create-image-request';
    import QrcodeVue from 'qrcode.vue';
    import VueCropper from 'vue-cropperjs';
    import 'cropperjs/dist/cropper.css';
    import Header from '@/app/components/Header.vue';

    @Component({
        components: {Header, QrcodeVue, VueCropper},
    })
    export default class CreateSurveyContainer extends Vue {
        public perspective: string = '';
        public surveyCode: string = '';
        public qrCodeUrl: string = '';
        public isLoading = false;
        public imageFile: File | null = null;
        public imageSource = '';
        public imageSources: string[] = [];
        public isImageProcessing = false;

        @Ref('cropper')
        public cropperElement!: any;

        @Watch('imageFile')
        public fileInput() {
            if (this.imageFile) {
                const file = this.imageFile;
                if (file.type.indexOf('image/') === -1) {
                    alert('Please select an image file');
                    return;
                }
                if (typeof FileReader === 'function') {
                    const reader = new FileReader();
                    reader.onload = (event) => {
                        if (event && event.target) {
                            this.imageSource = event.target.result as any;
                            this.cropperElement.replace(event.target.result);
                        }
                    };
                    reader.readAsDataURL(file);
                } else {
                    alert('Sorry, FileReader API not supported');
                }
            }
        }

        public cancelCrop() {
            this.imageFile = null;
            this.imageSource = '';
        }

        public async cropImage() {
            this.isImageProcessing = true;
            setTimeout(async () => {
                const croppedImageSource = this.cropperElement.getCroppedCanvas().toDataURL();
                const image = await this.createImage(croppedImageSource);
                const resizedImageSource = await this.resizeImage(image);
                this.cancelCrop();
                this.imageSources.push(resizedImageSource);
                this.isImageProcessing = false;
            }, 50);
        }

        public async create() {
            this.isLoading = true;
            const createSurveyRequest: CreateSurveyRequest = {
                perspective: this.perspective,
            };
            try {
                const response = await createSurvey(createSurveyRequest);
                this.surveyCode = response.code;
                const href = window.location.href;
                this.qrCodeUrl = href.split('#')[0] + '#/' + this.surveyCode + '/vote';
                await Promise.all(
                    this.imageSources.map(async file => {
                        const createImageRequest: CreateImageRequest = {file};
                        await createImage(createImageRequest, response.code);
                    }),
                );
                await startSurvey(response.code);
            } finally {
                this.isLoading = false;
            }
        }

        public deleteImage(index: number) {
            this.imageSources.splice(index, 1);
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

        private createImage(imageSource: string): Promise<HTMLImageElement> {
            return new Promise((resolve, reject) => {
                const img = new Image();
                // tslint:disable-next-line
                img.onload = function() {
                    resolve(img);
                };
                img.src = imageSource;
            });
        }

        private resizeImage(image: HTMLImageElement): string {
            const mainCanvas = document.createElement('canvas');
            mainCanvas.width = 480;
            mainCanvas.height = 480;
            const ctx = mainCanvas.getContext('2d');
            if (ctx) {
                ctx.fillStyle = '#FFFFFF';
                ctx.fillRect(0, 0, 480, 480);
                ctx.drawImage(image, 0, 0, mainCanvas.width, mainCanvas.height);
            }
            return mainCanvas.toDataURL('image/jpeg', 1);
        }
    }
</script>
