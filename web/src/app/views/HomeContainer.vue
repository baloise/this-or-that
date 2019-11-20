<template>
    <section id="home">
        <Header></Header>

        <section class="hero is-info is-medium is-bold">
            <div class="hero-body">
                <div class="container has-text-centered">
                    <h1 class="title is-2">Joining a Survey?</h1>
                    <br>
                    <div class="columns is-centered">
                        <div class="column is-half">
                            <canvas ref="canvas" hidden></canvas>
                            <video v-show="isScanEnabled" ref="preview"></video>
                            <b-field expanded>
                                <b-input placeholder="Enter Survey Code"
                                         v-model="surveyCode"
                                         expanded
                                         size="is-medium">
                                </b-input>
                                <p class="control">
                                    <button class="button is-info is-inverted is-medium"
                                            :disabled="isScanEnabled"
                                            :loading="isScanEnabled"
                                            @click="scan()">
                                        Scan QR-Code
                                    </button>
                                </p>
                            </b-field>
                            <br>
                            <div class="columns is-centered ">
                                <div class="column is-half">
                                    <button :disabled="surveyCode.length === 0 && isScanEnabled"
                                            @click="vote()"
                                            class="button is-primary is-medium is-fullwidth">
                                        Let's Vote
                                    </button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </section>

        <section class="hero is-medium ">
            <div class="hero-body">
                <div class="container has-text-centered">
                    <h1 class="title">Make your prioritization</h1>
                    <h2 class="subtitle">With This-or-That, prioritizing is fun and fast.</h2>
                    <br>
                    <div class="content is-center">
                        <img alt="robot" width="160px"
                             src="../../assets/this_or_that_robot_tranparent.gif"/>
                        <h4 class="title is-5 has-text-grey">Ready to get Started?</h4>
                    </div>
                    <div class="columns is-centered ">
                        <div class="column is-one-quarter">
                            <button @click="create()" class="button is-info is-medium is-fullwidth">Create a Survey
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </section>

        <section class="hero is-light is-medium is-bold">
            <div class="hero-body">
                <div class="container has-text-centered">
                    <h1 class="title">Manage your Survey</h1>
                    <h2 class="subtitle">This will close a survey for all the participants.</h2>
                    <br>
                    <div class="columns is-centered">
                        <div class="column is-one-quarter">
                            <b-field expanded>
                                <b-input placeholder="Enter Survey Code"
                                         v-model="surveyCode"
                                         expanded
                                         size="is-medium">
                                </b-input>
                            </b-field>
                            <br>
                            <button :disabled="surveyCode.length === 0 && isScanEnabled"
                                    @click="manageSurvey()"
                                    class="button is-info is-medium is-fullwidth">
                                Close & Show Results
                            </button>

                        </div>
                    </div>
                </div>
            </div>
        </section>

    </section>
</template>

<script lang="ts">
    import {Component, Ref, Vue} from 'vue-property-decorator';
    import Header from '@/app/components/Header.vue';
    import jsQR from 'jsqr';

    @Component({
        components: {Header},
    })
    export default class HomeContainer extends Vue {
        public surveyCode = '';
        public isScanEnabled = false;
        public stream: MediaStream | null = null;

        @Ref('preview')
        public video!: HTMLVideoElement;

        @Ref('canvas')
        public canvasElement!: HTMLCanvasElement;

public async scan() {
    this.isScanEnabled = true;
    this.stream = await navigator.mediaDevices.getUserMedia({audio: false, video: {facingMode: 'environment'}});
    this.video.srcObject = this.stream;
    this.video.setAttribute('playsinline', true as any); // required to tell iOS safari we don't want fullscreen
    await this.video.play();
    requestAnimationFrame(this.tick);
}

        private tick() {
            if (this.video.readyState === this.video.HAVE_ENOUGH_DATA) {
                const ctx = this.canvasElement.getContext('2d');
                if (ctx) {
                    this.canvasElement.height = this.video.videoHeight;
                    this.canvasElement.width = this.video.videoWidth;
                    ctx.drawImage(this.video, 0, 0, this.canvasElement.width, this.canvasElement.height);
                    const imageData = ctx.getImageData(0, 0, this.canvasElement.width, this.canvasElement.height);
                    const code = jsQR(imageData.data, imageData.width, imageData.height, {
                        inversionAttempts: 'dontInvert',
                    });
                    if (code) {
                        this.surveyCode = code.data
                            .replace(location.origin, '')
                            .replace('https://baloise.github.io', '')
                            .replace('/this-or-that', '')
                            .replace('/index.html', '')
                            .replace('#/', '')
                            .replace('/vote', '')
                            .replace(/\//g, '');
                        if (this.stream) {
                            this.stream.getTracks().forEach(value => value.stop());
                        }
                        this.isScanEnabled = false;
                    }
                }
            }
            requestAnimationFrame(this.tick);
        }

        public create() {
            this.$router.push('create');
        }

        public vote() {
            this.$router.push(this.surveyCode + '/vote');
        }

        public manageSurvey() {
            this.$router.push(this.surveyCode + '/admin');
        }
    }
</script>
