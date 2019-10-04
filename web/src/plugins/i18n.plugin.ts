/* ============
 * Vue i18n
 * ============
 *
 * Internationalization plugin of Vue.js.
 *
 * https://kazupon.github.io/vue-i18n/
 */

import Vue from 'vue';
import VueI18n from 'vue-i18n';

import de from '@/i18n/de';
import en from '@/i18n/en';

Vue.use(VueI18n);

export const i18n = new VueI18n({
    locale: 'en', // set locale
    fallbackLocale: 'en',
    silentTranslationWarn: true,
    messages: {de, en}, // set locale messages
});
