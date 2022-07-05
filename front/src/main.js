import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import axios from 'axios'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'

const app = createApp(App)

for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component)
}

axios.defaults.baseURL = 'http://180.76.136.85:8080'
app.config.globalProperties.$http = axios

//配置全局axios请求时加载
let loading
function startLoading() {
    loading = ElLoading.service({
        lock: true,
        text: '加载中...(如果长时间未响应, 请刷新页面后重试)',
        background: 'rgba(0, 0, 0, 0.7)',
    })
}
function endLoading() {
    loading.close()
}
//请求数据拦截器
axios.interceptors.request.use(request => {
    startLoading()
    return request
}, error => {
    return Promise.reject(error)
})
//接收响应拦截器
axios.interceptors.response.use(response => {
    endLoading()
    return response
}, error => {
    return Promise.reject(error)
})

app.use(store)
app.use(router)

app.mount('#app')
