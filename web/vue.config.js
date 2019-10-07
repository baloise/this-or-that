const webpack = require('webpack');
var node_env = process.env.VUE_APP_MODE !== 'development' ? 'production' : 'development'
module.exports = {
  configureWebpack: {
    plugins: [
      new webpack.DefinePlugin({
        'process.env': {
          PACKAGE_JSON: '"' + encodeURI(JSON.stringify(require('./package.json'))) + '"',
        },
      }),
      new webpack.ProvidePlugin({
        $: 'jquery',
        jquery: 'jquery',
        'window.jQuery': 'jquery',
        jQuery: 'jquery',
      }),
    ],
    mode: node_env

  },
  devServer: node_env
      ? {
        proxy: {
          '^/api': {
            target: 'http://localhost:8080',
            ws: true,
            changeOrigin: true
          }
        }
      }
      : {}
};
