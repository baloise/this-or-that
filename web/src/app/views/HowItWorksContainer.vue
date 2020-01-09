<template>
    <section id="howitworks">
        <Header></Header>
        <section class="hero is-info is-bold">
            <div class="hero-body">
                <div class="container">
                    <h1 class="title">Decision-making</h1>
                    <p>We make about hundreds, maybe thousands of decisions every day. From minuscule to huge. Every
                        time we have to choose no matter how small the choice may seem we are depleting our energy and
                        creating stress.</p>
                    <p>Making a good decision is hard work and that is where This Or That comes into play. This or That
                        makes deciding easy on whatever your decision is about. With the help of more people of
                        interest, you can offload this energy depletion to the group. Together you can make a better
                        decision. Is it at work about which logo is best? Or is it about which restaurant you and your
                        friends want to go? It doesn’t matter This or That can help you out.</p>
                </div>
            </div>
        </section>
        <section class="hero is-info is-bold">
            <div class="hero-body">
                <div class="container">
                    <h1 class="title">How does it work?</h1>
                    <div class="columns is-vcentered">
                        <div class="column has-text-centered">
                            <p>1. Click on create a survey</p>
                        </div>
                        <div class="column">
                            <figure class="image is-medium">
                                <img alt="create survey" src="../../assets/create_survey.png">
                            </figure>
                        </div>
                    </div>
                    <div class="columns is-vcentered">
                        <div class="column has-text-centered">
                            <p>2. Give your survey a topic</p>
                            <p>3. Upload your images you want to vote about (I have no image? No Problem just create new
                                images with your smartphone)</p>
                            <p>4. Accept the terms and click create</p>
                        </div>
                        <div class="column">
                            <figure class="image is-medium">
                                <img alt="create survey" src="../../assets/create_survey_page.png">
                            </figure>
                        </div>
                    </div>
                    <div class="columns is-vcentered">
                        <div class="column has-text-centered">
                            <p>5. You get a Code and a QR-Code</p>
                        </div>
                        <div class="column">
                            <figure class="image is-medium">
                                <img alt="create survey" src="../../assets/survey_created.png">
                            </figure>
                        </div>
                    </div>
                    <div class="columns is-vcentered">
                        <div class="column has-text-centered">
                            <p>6. Share the code or the QR-Code with your friends so they can vote on your survey</p>
                        </div>
                        <div class="column">
                            <figure class="image is-medium">
                                <img alt="create survey" src="../../assets/participate.png">
                            </figure>
                        </div>
                    </div>
                    <div class="columns is-vcentered">
                        <div class="column has-text-centered">
                            <p>7. Stop your survey and show the result by clicking on close & show results on the main
                                page</p>
                        </div>
                        <div class="column">
                            <figure class="image is-medium">
                                <img alt="create survey" src="../../assets/close_survey.png">
                            </figure>
                        </div>
                    </div>
                    <div class="columns is-vcentered">
                        <div class="column has-text-centered">
                            <p>8. You get the result. A ranking of all your images</p>
                        </div>
                        <div class="column">
                            <figure class="image is-medium">
                                <img alt="create survey" src="../../assets/result.png">
                            </figure>
                        </div>
                    </div>
                </div>
            </div>
        </section>
    </section>
</template>

<script lang="ts">
    import {Component, Vue} from 'vue-property-decorator';
    import Header from '@/app/components/Header.vue';

    @Component({
        components: {Header},
    })

    export default class HowItWorksContainer extends Vue {

    }


</script>

<style lang="scss" scoped>
    p {
        font-size: 25px;
    }
</style>