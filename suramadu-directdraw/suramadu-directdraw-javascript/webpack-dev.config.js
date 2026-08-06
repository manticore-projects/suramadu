const path = require("path");
const { merge } = require("webpack-merge");
const HtmlWebpackPlugin = require("html-webpack-plugin");
const commonConfig = require("./webpack.config.js");

const ROOT = path.resolve(__dirname, "src/test/webapp");
const DESTINATION = path.resolve(__dirname, ".tmp");

module.exports = merge(commonConfig, {
  context: ROOT,

  devtool: "cheap-module-source-map",
  mode: "development",

  devServer: {
    static: {
      directory: path.join(__dirname, ".tmp"),
    },
    compress: true,
    port: 9000,
  },

  output: {
    path: DESTINATION,
    filename: "js/index.js"
  },

  plugins: [
    new HtmlWebpackPlugin({
      template: "index.html",
      inject: true,
      templateParameters: {
        __WEBSWING_URL: ".."
      }
    }),
  ]
});
