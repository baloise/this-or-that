<template>
    <section id="not-found">
        <Header></Header>
        <section class="hero is-primary is-bold is-fullheight-with-navbar">
            <div class="hero-body">
                <div class="container has-text-centered">
                    <h1 class="title">404</h1>
                    <h2 class="subtitle">Page not found</h2>
                    <br>
                    <br>
                    <img width="200" alt="robot" src="../../assets/error.png" />
                </div>
            </div>
        </section>
    </section>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import Header from '@/app/components/Header.vue';

@Component({
    components: {Header},
})
export default class NotFound extends Vue {}
</script>
