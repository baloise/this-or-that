<template>
    <layout>
        <div class="about">
            <h1>This-or-That</h1>
            <p>... is built with cutting-edge technology, dedication and a lot of chees.</p>
            <div class="columns">
                <div class="column">
                    <div class="box has-text-centered">
                        <p>Rudolf Biczok</p>
                        <a href="https://github.com/RCBiczok">
                            <figure class="image is-128x128">
                                <img
                                    src="https://avatars.githubusercontent.com/u/538973?s=128"
                                    alt="img_rudolf_biczok"
                                />
                            </figure>
                        </a>
                    </div>
                </div>
                <div class="column">
                    <div class="box">
                        <p>Gery Hirschfeld</p>
                        <a href="https://github.com/hirsch88">
                            <figure class="image is-128x128">
                                <img
                                    src="https://avatars.githubusercontent.com/u/6384499?s=128"
                                    alt="img_gery_hirschfeld"
                                />
                            </figure>
                        </a>
                    </div>
                </div>
            </div>
            <div class="columns">
                <div class="column">
                    <div class="box">
                        <p>Yannick Holzenkamp</p>
                        <a href="https://github.com/yannickholzenkamp">
                            <figure class="image is-128x128">
                                <img
                                    src="https://avatars.githubusercontent.com/u/9351023?s=128"
                                    alt="img_yannick_holzenkamp"
                                />
                            </figure>
                        </a>
                    </div>
                </div>
                <div class="column">
                    <div class="box">
                        <p>Philipp Steiner</p>
                        <a href="https://github.com/jordilaforge">
                            <figure class="image is-128x128">
                                <img
                                    src="https://avatars.githubusercontent.com/u/9675234?s=128"
                                    alt="img_philipp_steiner"
                                />
                            </figure>
                        </a>
                    </div>
                </div>
            </div>
            <div class="box">
                <p>Markus Tiede</p>
                <a href="https://github.com/MarkusTiede">
                    <figure class="image is-128x128">
                        <img
                            src="https://avatars.githubusercontent.com/u/1764012?s=128"
                            alt="img_markus_tiede"
                        />
                    </figure>
                </a>
            </div>
        </div>
    </layout>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import Layout from '@/app/components/layout.vue';

@Component({
    components: { Layout },
})
export default class About extends Vue {}
</script>

<style lang="scss" scoped>
.image {
    margin-left: auto;
    margin-right: auto;
}

.image img {
    border-radius: 5px;
}
</style>