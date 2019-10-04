import Vue, {ComponentOptions, PluginFunction, AsyncComponent} from 'vue';

type LogdownBuilder = (scope: string) => LogdownInstance;

interface LogdownInstance {
    debug(...x: any[]): void;

    info(...x: any[]): void;

    log(...x: any[]): void;

    warn(...x: any[]): void;

    error(...x: any[]): void;
}

interface LogdownOptions {
    prefix: string;
    markdown: boolean;
    isEnabled: boolean;
}

export declare class VueLogdown {
    constructor(options?: LogdownOptions);

    static install: PluginFunction<never>;
}
