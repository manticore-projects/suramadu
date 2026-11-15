const path = require("path");
const ROOT = path.resolve(__dirname, "src/main/webapp/javascript");
const webpack = require("webpack");
const DESTINATION = path.resolve(__dirname, "target/suramadu-server-frontend/javascript");
const TARGET = path.resolve(__dirname, "target/suramadu-server-frontend");
const NODE_PATH = path.resolve(__dirname, "node_modules");

/**
 * Webpack Plugins
 */
const MiniCssExtractPlugin = require("mini-css-extract-plugin");
const CopyPlugin = require('copy-webpack-plugin');
const CompressionPlugin = require('compression-webpack-plugin');
const zlib = require('zlib');

module.exports = {
  context: ROOT,

  resolve: {
    extensions: [".ts", ".js"]
  },
  output: {
    path: DESTINATION,
    filename: "[name].js"
  },

  module: {
    rules: [
      {
        test: /\.ts$/,
        use: 'ts-loader',
        exclude: /node_modules/
      },

      {
        test: /\.scss$/,
        use: [
          MiniCssExtractPlugin.loader,
          "css-loader",
          {
            loader: "sass-loader",
            options: {
              sassOptions: {
                silenceDeprecations: ["import"]
              }
            }
          }
        ]
      },

      {
        test: /\.(jpg|png|gif)$/,
        type: 'asset/resource'
      },

      {
        test: /\.(svg|woff|woff2|eot|ttf)$/,
        type: 'asset/resource',
        generator: {
          filename: 'fonts/[name][ext]'
        }
      },

      {
        test: /\.html$/,
        exclude: /index\.html$/,
        type: 'asset/source'
      }
    ]
  },

  plugins: [
    new MiniCssExtractPlugin({
      filename: "../css/style.css"
    }),
    new webpack.ProvidePlugin({
      $: "jquery",
      jQuery: "jquery",
      "window.jQuery": "jquery"
    }),
    // The bundled pdf.js print viewer (print/web, print/build) is gone:
    // print jobs open the PDF URL directly and the browser's native viewer
    // renders it (see fhPrint in suramadu-files.ts).
    new CopyPlugin({
      patterns: [
        { from: ROOT + '/suramadu-init.js', to: DESTINATION + '/suramadu-init.js' }
      ]
    }),
    // Pre-generate .gz files
    new CompressionPlugin({
      filename: '[path][base].gz',
      algorithm: 'gzip',
      test: /\.(js|css|html|svg)$/,
    }),
    // Pre-generate .br files
    new CompressionPlugin({
      filename: '[path][base].br',
      algorithm: 'brotliCompress',
      test: /\.(js|css|html|svg)$/,
      compressionOptions: {
        params: { [zlib.constants.BROTLI_PARAM_QUALITY]: 11 },
      },
    }),
  ],

  entry: {
    "suramadu-selector": "./suramadu-selector.ts",
    "suramadu-security": "./suramadu-security.ts",
    "suramadu-embed": "./suramadu-embed.ts",
    "suramadu-dd-worker": "./suramadu-dd-worker.ts",
  }
};