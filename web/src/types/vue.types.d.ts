import Vue from 'vue';
import {AxiosInstance} from 'axios';
import {LogdownBuilder} from './logdown';

declare module 'vue/types/vue' {
    interface VueConstructor {
        $createLogger: LogdownBuilder;
        $http: AxiosInstance;
    }

    interface Vue {
        $createLogger: LogdownBuilder;
        $http: AxiosInstance;
    }
}
