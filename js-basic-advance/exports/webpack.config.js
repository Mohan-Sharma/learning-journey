const path = require('path');  // one of nodejs package which resolves path
const cleanPlugin = require('clean-webpack-plugin'); // install the plugin first

module.exports = {
    mode: 'development',
    entry: './src/app.js',
    output : {
        filename: 'app.js',
        path: path.join(__dirname, 'assets', 'scripts'),
        publicPath: 'assets/scripts/' // lookup folder
    },
    devServer: {
        static: {
            directory: path.join(__dirname, './'), // current dir where this file resides 
          },
        compress: true,
        port: 9000,
        devMiddleware: {
            writeToDisk: true,
        }
    },
    plugins: [
        new cleanPlugin.CleanWebpackPlugin()
    ]
};