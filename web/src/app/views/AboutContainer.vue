<template>
    <section id="about">
        <Header></Header>
        <section class="hero is-light is-bold is-fullheight-with-navbar">
            <div class="hero-body">
                <div class="container">
                    <h1 class="title">About This-or-That</h1>
                    <h2 class="subtitle">... is built with cutting-edge technology, dedication and a lot of cheese.</h2>
                    <div class="columns is-multiline">
                        <div class="column is-one-third"
                             v-for="user in contributers" v-bind:key="user.name">
                            <div class="box has-text-centered" style="min-height: 245px">
                                <p class="subtitle has-text-grey">{{ user.name }}</p>
                                <a :href="user.link">
                                    <figure class="image is-128x128">
                                        <img :src="user.avatar"/>
                                    </figure>
                                </a>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </section>
        <Footer></Footer>
    </section>
</template>

<script lang="ts">
    import {Component, Vue} from 'vue-property-decorator';
    import Header from '@/app/components/Header.vue';
    import Footer from '@/app/components/Footer.vue';

    @Component({
        components: {Footer, Header},
    })
    export default class About extends Vue {

        contributers = [
            {
                name: 'Philipp Steiner',
                avatar: 'https://avatars.githubusercontent.com/u/9675234?s=128',
                link: 'https://github.com/jordilaforge',
            }, {
                name: 'Gery Hirschfeld',
                avatar: 'https://avatars.githubusercontent.com/u/6384499?s=128',
                link: 'https://github.com/hirsch88',
            }, {
                name: 'Markus Tiede',
                avatar: 'https://avatars.githubusercontent.com/u/1764012?s=128',
                link: 'https://github.com/MarkusTiede',
            }, {
                name: 'Rudolf Biczok',
                avatar: 'https://avatars.githubusercontent.com/u/538973?s=128',
                link: 'https://github.com/RCBiczok',
            }, {
                name: 'Yannick Holzenkamp',
                avatar: 'https://avatars.githubusercontent.com/u/9351023?s=128',
                link: 'https://github.com/yannickholzenkamp',
            },


        ];

    }
</script>

<style lang="scss" scoped>
    .image {
        margin-left: auto;
        margin-right: auto;
    }
</style>
