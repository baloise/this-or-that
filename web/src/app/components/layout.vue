<template>
    <div class="home">
        <section class="hero is-light is-bold is-fullheight">
            <div class="hero-header has-text-centered">
                <img
                    @click="back()"
                    alt="logo"
                    src="../../assets/logo.png"
                    style="margin: 5px;"
                    width="120px"
                />
            </div>
            <div class="hero-body" style="align-items: flex-start;">
                <div class="container">
                    <div class="columns">
                        <div class="column"></div>
                        <div class="column is-two-thirds-tablet is-half-desktop">
                            <div class="content is-center">
                                <slot></slot>
                            </div>
                        </div>
                        <div class="column"></div>
                    </div>
                </div>
            </div>
        </section>
    </div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';

@Component
export default class Layout extends Vue {
    public back() {
        this.$router.push('/');
    }
}
</script>
