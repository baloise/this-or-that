<template>
    <div id="app">
        <router-view/>
    </div>
</template>

<script lang="ts">
    import {Component, Vue} from 'vue-property-decorator';
    import Header from './components/Header.vue';

    @Component({
        components: {
            Header,
        },
    })
    export default class App extends Vue {
    }
</script>
