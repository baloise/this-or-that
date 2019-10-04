/* ============
 * Logdown
 * ============
 *
 * Lightweight, unobtrusive, configurable JavaScript logger.
 *
 * https://caiogondim.github.io/logdown.js/
 */

import Vue from 'vue';
import {PluginObject} from 'vue';
import logdown from 'logdown';

export const Logger: PluginObject<any> = {
    install(VueInstance, options) {
        const LOCAL_STORAGE_KEY = 'debug';
        const PREFIX = 'app';

        const defaultOptions = {
            markdown: true,
            isEnabled: true,
        };

        const logdownOptions = Object.assign({}, defaultOptions, options);

        if (localStorage.getItem(LOCAL_STORAGE_KEY) === null) {
            localStorage.setItem(LOCAL_STORAGE_KEY, `${PREFIX}:*`);
        }

        const createLogger = (prefix: string) => logdown(`${PREFIX}:${prefix}`, logdownOptions);

        VueInstance.$createLogger = createLogger;

        VueInstance.prototype.$createLogger = createLogger;
    },
};

Vue.use(Logger);
