<template>
  <div>
    <slot
      v-if="err"
      name="error"
      v-bind:err="err"
      v-bind:vm="vm"
      v-bind:info="info"
    >Something went wrong</slot>
    <slot v-else></slot>
  </div>
</template>

<script lang="ts">
import { Component, Prop, Vue } from 'vue-property-decorator';

@Component
export default class ErrorBoundary extends Vue {
  public err: boolean = false;
  public vm: any;
  public info: any;

  @Prop()
  public stopPropagation!: boolean;

  public errorCaptured(err: boolean, vm: any, info: any) {
    this.err = err;
    this.vm = vm;
    this.info = info;
    return !this.stopPropagation;
  }
}
</script>

<style lang="scss" scoped>
</style>