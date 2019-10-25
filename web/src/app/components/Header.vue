<template>
    <b-navbar>
        <template slot="brand">
            <b-navbar-item tag="router-link" :to="{ path: '/' }">
                <img src="../../assets/logo-landscape.png" height="28"/>
            </b-navbar-item>
        </template>
        <template slot="start">
            <b-navbar-item @click.native="home()">
                Home
            </b-navbar-item>
            <b-navbar-item @click.native="about()">
                About
            </b-navbar-item>
        </template>
    </b-navbar>
</template>

<script lang="ts">
    import {Component, Vue} from 'vue-property-decorator';

    @Component
    export default class Header extends Vue {
        public home() {
            this.$router.push('/');
        }

        public about() {
            this.$router.push('/about');
        }
    }
</script>
