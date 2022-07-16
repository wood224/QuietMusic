import { createApp } from 'vue'
import Login from './Login.vue'
import router from './router'
import store from './store'
import { ElLoading } from "element-plus"
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import axios from 'axios'

const app = createApp(Login)

app.use(store)
app.use(router)

axios.defaults.baseURL = 'http://180.76.136.85:8080/user'
app.config.globalProperties.$http = axios

//配置全局axios请求时加载
// let loading
// function startLoading() {
//     loading = ElLoading.service({
//         lock: true
//     })
// }
// function endLoading() {
//     loading.close()
// }
// //请求数据拦截器
// axios.interceptors.request.use(request => {
//     startLoading()
//     return request
// }, error => {
//     return Promise.reject(error)
// })
// //接收响应拦截器
// axios.interceptors.response.use(response => {
//     endLoading()
//     return response
// }, error => {
//     return Promise.reject(error)
// })

for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component)
}

app.mount('#app')
