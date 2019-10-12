<template>
    <layout>
        <h2 class="title is-2">(404) page not found</h2>
        <img alt="robot" src="../../assets/error.png" />
    </layout>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import Layout from '@/app/components/layout.vue';

@Component({
    components: { Layout },
})
export default class NotFound extends Vue {}
</script>