<template>
    <div class="home">
        <section class="hero is-light is-bold is-fullheight">
            <div class="hero-header">
                <img alt="logo" src="../../assets/logo.png" style="margin: 5px;" width="120px"/>
            </div>
            <div class="hero-body" style="align-items: flex-start;">
                <div class="container">
                    <div class="columns">
                        <div class="column"></div>
                        <div class="column is-two-thirds-tablet is-half-desktop">
                            <!-- <div class="tile box has-background-light"> -->
                            <div class="content is-center">

                            </div>
                            <!-- </div> -->
                        </div>
                        <div class="column"></div>
                    </div>
                </div>
            </div>
        </section>
    </div>
</template>

<script lang="ts">
    import {Component, Vue} from "vue-property-decorator";

    @Component({
        components: {},
    })
    export default class VoteContainer extends Vue {


    }
</script>
