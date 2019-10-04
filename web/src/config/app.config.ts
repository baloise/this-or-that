export const appConfig = {
  env: process.env.VUE_APP_ENV_NAME as string,
  logLevel: process.env.VUE_APP_LOG_LEVEL as string,
  apiPath: process.env.VUE_APP_API_PATH as string,
};
