<template>
    <div id="app">
        <div style="text-align: center;" v-if="outOfService">
            <img width="200" alt="robot" src="../assets/error.png" />
            <p>Currently out of service</p>
        </div>
        <router-view v-if="!outOfService"/>
    </div>
</template>

<script lang="ts">
    import {Component, Vue} from 'vue-property-decorator';
    import Header from './components/Header.vue';
    import {appConfig} from '@/config/app.config';

    @Component({
        components: {
            Header,
        },
    })
    export default class App extends Vue {
        public outOfService = appConfig.outOfService;
    }
</script>
