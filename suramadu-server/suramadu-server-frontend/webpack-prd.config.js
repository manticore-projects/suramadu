const { merge } = require("webpack-merge");
const commonConfig = require("./webpack.config.js");
const CompressionPlugin = require('compression-webpack-plugin');

module.exports = merge(commonConfig, {
  mode: "production",
  optimization: {
    minimize: true
  },
  plugins: [new CompressionPlugin()]
});