const path = require('path')
const cleanPlugin = require('clean-webpack-plugin'); // install the plugin first

module.exports = {
    mode: 'production',
    entry: './src/app.js',
    output: {
        filename : '[contenthash].js', // dynamic hash so generates a new file everytime so server wont cash as cleaning will clean old files
        path: path.resolve(__dirname, 'assets', 'scripts'),
        publicPath: 'assets/scripts/'
    },
    plugins: [
        new cleanPlugin.CleanWebpackPlugin()
    ]
};