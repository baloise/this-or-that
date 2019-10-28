<template>
    <div class="inner">
        <figure class="image is-square">
            <img :src="src" alt="image to vote">
        </figure>
    </div>
</template>

<script lang="ts">
    import {Component, Prop, Ref, Vue} from 'vue-property-decorator';

    @Component
    export default class VoteImage extends Vue {
        @Prop({default: ''}) src!: string;
    }
</script>

<style scoped lang="scss">
    @import "~bulma/sass/utilities/all";

    .inner {
        cursor: pointer;
        width: 100%;
        min-width: 240px;
        max-width: 480px;
    }

    @include mobile() {
        .inner {
            max-width: 240px;
        }
    }
</style>
