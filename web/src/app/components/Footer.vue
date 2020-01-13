<template>
    <footer class="footer">
        <div class="content has-text-centered">
            <p>
                <strong>This-or-That</strong> The source code is licensed <a
                    href="https://www.apache.org/licenses/LICENSE-2.0">Apache License Version 2.0</a>.
                <router-link to="/tos">Terms of Service</router-link>
                and
                <router-link to="/privacy">Privacy Policy</router-link>
            </p>
        </div>
    </footer>
</template>

<script lang="ts">
    import {Component, Vue} from 'vue-property-decorator';

    @Component
    export default class Footer extends Vue {

    }
</script>
