<template>
    <section id="create">
        <Header></Header>

        <section class="container padded-container" v-if="!this.surveyCode">
            <h1 class="title is-3">Create A Survey</h1>
            <b-field label="Choose a perspective for your survey!">
                <b-input v-model="perspective"
                         :disabled="isLoading"
                         placeholder="Enter a survey name"></b-input>
            </b-field>

            <label class="label">Upload your Images! (Only PNG & JPG are supported)</label>
            <div class="field" style="max-height: 200px">
                <b-field class="custom-upload-expanded" :expanded="true">
                    <b-upload accept="image/x-png,image/png,image/jpeg"
                              :multiple="true"
                              drag-drop v-model="imageFiles">
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
            <p class="label" v-if="images && images.length > 0">Images</p>
            <div class="columns is-mobile is-multiline">
                <div class="column is-half is-one-quarter-desktop" v-for="(item, index) in images"
                     :key="index">
                    <figure class="image is-square" style="position: relative">
                        <div style="position: absolute; z-index: 100; top: 15px; width: 100%; text-align: center; font-size: 3em;">
                            <font-awesome-icon v-if="item.isProcessing" size="lg" icon="spinner" spin/>
                        </div>
                        <button v-if="!item.isProcessing"
                                class="delete is-large"
                                @click="deleteImage(index)"
                                style="position: absolute; z-index: 100; top: 5px; right: 5px"></button>
                        <img v-if="item.source"
                             :src="item.source"
                             :alt="'Preview ' + (index+1)">
                    </figure>
                </div>
            </div>

            <hr>

            <b-notification
                    :closable="false"
                    role="alert"
                    type="is-warning">
                Please be advised that your survey and all connected data will be deleted automatically after 24 hours.
            </b-notification>

            <div class="field">
                <b-checkbox v-model="checkBox">I agree to the
                    <router-link to="/tos">Terms of Service</router-link>
                    and
                    <router-link to="/privacy">Privacy Policy</router-link>
                    .
                </b-checkbox>
            </div>

            <div class="buttons">
                <b-button type="is-primary"
                          :disabled="!isFormValid"
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

        </section>

        <section class="hero is-info is-fullheight-with-navbar" v-if="this.surveyCode && !this.isLoading">
            <div class="hero-body" style="flex-direction: column;">
                <h3 class="title is-2">Your Survey is ready!</h3>
                <br>
                <h3 class="subtitle is-3">SurveyCode = {{this.surveyCode}}</h3>
                <br>
                <div class="box" style="display: inline-block;">
                    <qrcode-vue :value="this.qrCodeUrl" size="200" level="H"></qrcode-vue>
                </div>
                <br/>
                <div class="field is-grouped is-grouped-centered">
                    <div class="buttons">
                        <b-button type="is-primary" @click="vote()" size="is-medium">
                            Let's Vote
                        </b-button>
                    </div>
                </div>
            </div>
        </section>
        <b-loading :active.sync="isLoading" :is-full-page="true"></b-loading>
    </section>
</template>

<script lang="ts">
    import {Component, Vue, Watch} from 'vue-property-decorator';
    import {createImage, createSurvey, startSurvey} from '@/app/api/survey.api';
    import {CreateSurveyRequest} from '@/app/models/create-survey-request';
    import {CreateImageRequest} from '@/app/models/create-image-request';
    import QrcodeVue from 'qrcode.vue';
    import Header from '@/app/components/Header.vue';

    interface ImageObject {
        originalFile: File;
        isProcessing: boolean;
        index?: number;
        source?: string;
    }

    @Component({
        components: {Header, QrcodeVue},
    })
    export default class CreateSurveyContainer extends Vue {
        public perspective: string = '';
        public surveyCode: string = '';
        public qrCodeUrl: string = '';
        public isLoading = false;
        public checkBox = false;
        public imageFiles: File[] | null = null;
        public images: ImageObject[] = [];

        get isFormValid(): boolean {
            return !!this.perspective && this.images.filter(image => !image.isProcessing).length > 2 && this.checkBox;
        }

        @Watch('imageFiles')
        public fileInput() {
            if (this.imageFiles) {
                const newImages: ImageObject[] = this.imageFiles
                    .map(file => ({
                        originalFile: file,
                        isProcessing: true,
                    }));
                this.imageFiles = null;
                this.images = this.images.concat(newImages).map((image, index) => ({
                    ...image,
                    index,
                }));
                setTimeout(() => {
                    this.processNextImage();
                }, 100);
            }
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
                    this.images.map(async image => {
                        const createImageRequest: CreateImageRequest = {file: image.source as string};
                        await createImage(createImageRequest, response.code);
                    }),
                );
                await startSurvey(response.code);
            } finally {
                this.isLoading = false;
            }
        }

        public deleteImage(index: number) {
            this.images.splice(index, 1);
        }

        public back() {
            this.$router.push('/');
        }

        public vote() {
            this.$router.push(this.surveyCode + '/vote');
        }

        private async processNextImage() {
            const newImages = this.images.filter(image => image.isProcessing);
            if (newImages.length > 0) {
                const newImage = newImages[0];
                newImage.source = await this.readImage(newImage.originalFile);
                const imageElement = await this.createImage(newImage.source);
                newImage.source = await this.resizeImage(imageElement);
                this.images[newImage.index as number] = {...newImage, isProcessing: false};
                this.images = [...this.images];

                setTimeout(() => {
                    this.processNextImage();
                }, 100);
            }
        }

        private readImage(file: File): Promise<string> {
            return new Promise((resolve, reject) => {
                if (typeof FileReader === 'function') {
                    const reader = new FileReader();
                    reader.onload = (event) => {
                        if (event && event.target) {
                            resolve(event.target.result as any);
                        }
                    };
                    reader.readAsDataURL(file);
                } else {
                    alert('Sorry, FileReader API not supported');
                    reject();
                }
            });
        }

        private async resizeImage(image: HTMLImageElement) {
            const mainCanvas = document.createElement('canvas');
            const width = 480;
            const height = 480;
            mainCanvas.width = width;
            mainCanvas.height = height;
            const ctx = mainCanvas.getContext('2d');
            if (ctx) {
                let xStart = 0;
                let yStart = 0;
                let newWidth;
                let newHeight;
                let aspectRadio = image.height / image.width;
                if (image.height < image.width) {
                    // horizontal
                    aspectRadio = image.width / image.height;
                    newHeight = height;
                    newWidth = aspectRadio * height;
                    xStart = -(newWidth - width) / 2;
                } else {
                    // vertical
                    newWidth = width;
                    newHeight = aspectRadio * width;
                    yStart = -(newHeight - height) / 2;
                }

                ctx.fillStyle = '#FFFFFF';
                ctx.fillRect(0, 0, 480, 480);
                ctx.drawImage(image, xStart, yStart, newWidth, newHeight);
            }
            return mainCanvas.toDataURL('image/jpeg', 1);
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

    }
</script>
