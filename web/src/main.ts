/* ============
 * Main File
 * ============
 *
 * Will initialize the application.
 */

import Vue from 'vue';
import './styles/styles.scss';

/* ============
 * Plugins
 * ============
 *
 * Import and bootstrap the plugins.
 * The order is important!
 */

import '@/plugins/fontawesome.plugin';
import '@/plugins/buefy.plugin';
import '@/plugins/logdown.plugin';
import '@/plugins/axios.plugin';
import '@/plugins/vuex.plugin';
import '@/plugins/vuelidate.plugin';
import {i18n} from '@/plugins/i18n.plugin';
import {router} from '@/plugins/vue-router.plugin';

/* ============
 * Main App
 * ============
 *
 * Last but not least, we import the main application.
 */

import App from './app/App.vue';
import {store} from '@/app/store';
import {appConfig} from '@/config/app.config';

Vue.config.productionTip = false;

new Vue({
    /**
     * The localization plugin.
     */
    i18n,

    /**
     * The router.
     */
    router,

    /**
     * The Vuex store.
     */
    store,

    /**
     * Will render the application.
     *
     * @param {Function} h Will create an element.
     */
    render: h => h(App),
})
/**
 * Bind the Vue instance to the HTML.
 */

    .$mount('#app');

const log = Vue.$createLogger('main');
log.info(`The environment is ${appConfig.env}.`);
log.info(`The language is set to ${i18n.locale}.`);
