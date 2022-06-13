// const { defineConfig } = require('@vue/cli-service')
// module.exports = defineConfig({
//   transpileDependencies: true
// })

const AutoImport = require('unplugin-auto-import/webpack')
const Components = require('unplugin-vue-components/webpack')
const { ElementPlusResolver } = require('unplugin-vue-components/resolvers')

module.exports = {
  pages: {
    index: {
      entry: './src/main.js',
      template: './public/index.html',
      filename: 'index.html',
      title: '静谧音乐社'
    },
    login: {
      entry: 'src/views/Login/Login.js',
      template: 'src/views/Login/login.html',
      filename: 'login.html',
      title: '欢迎登录/注册'
    }
  },
  configureWebpack: {
    // ...
    plugins: [
      AutoImport({
        resolvers: [ElementPlusResolver()],
      }),
      Components({
        resolvers: [ElementPlusResolver()],
      }),
    ],
  }

}

