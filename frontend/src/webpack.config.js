module.exports = {
    module: {
        loaders: [
            {
                test: /\.(jpe?g|gif|png|svg|wav|mp3|txt|dat|idx)$/,
                loader: "file"
            }, {
                test: /\/App\.js$/, // regex to match files to receive react-hot-loader functionality
                loader: require.resolve('react-hot-loader-loader'),
            }
        ],
        rules: [
            {
                test: /\.js$/,
                loader: 'babel-loader',
                options: { presets: ['env', 'react'] }
            }
        ]
    }
};
