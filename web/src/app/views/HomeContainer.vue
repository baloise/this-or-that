<template>
    <div class="home">
        <section class="hero is-light is-bold is-fullheight">
            <div class="hero-header">
                <img src="../../assets/logo.png" alt="logo" width="120px" style="margin: 5px;"/>
            </div>
            <div class="hero-body" style="align-items: flex-start;">
                <div class="container">
                    <div class="columns">
                        <div class="column"></div>
                        <div class="column is-two-thirds-tablet is-half-desktop">
                            <!-- <div class="tile box has-background-light"> -->
                            <div class="content is-center">
                                <h2 class="title is-2">Make your prioritization</h2>
                                <h4 class="subtitle is-4">With This-or-That, prioritizing is fun and fast.</h4>
                                <br/>
                                <br/>
                                <button class="button is-primary is-medium">Create Poll</button>
                                <br/>
                                <br/>
                                <br/>
                                <br/>
                                <img alt="robot" src="../../assets/this_or_that_robot_tranparent.gif"/>
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
    import {Component, Vue} from 'vue-property-decorator';

    @Component({
        components: {},
    })
    export default class HomeContainer extends Vue {
    }
</script>
