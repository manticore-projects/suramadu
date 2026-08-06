const { merge } = require("webpack-merge");
const commonConfig = require("./webpack.config.js");

module.exports = merge(commonConfig, {
  mode: "development",
  devtool: "cheap-source-map"
});