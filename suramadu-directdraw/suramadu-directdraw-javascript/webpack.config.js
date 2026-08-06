const CopyPlugin = require('copy-webpack-plugin');

module.exports = {

  resolve: {
    extensions: [".ts", ".js"]
  },

  module: {
    rules: [
      {
        test: /\.ts$/,
        use: 'ts-loader',
        exclude: /node_modules/
      },
      {
        test: /\.(jpg|png|gif)$/,
        type: 'asset/resource'
      },
      {
        test: /\.html$/,
        exclude: /index\.html$/,
        type: 'asset/source'
      }
    ]
  },

  plugins: [
    new CopyPlugin({
      patterns: [
        { from: 'proto/dd.d.ts', to: 'main/webapp/proto/dd.d.ts' },
      ]
    }),
  ],

  entry: ["./index.ts"]
};
