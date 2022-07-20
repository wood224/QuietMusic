import axios from "axios"
import env from "./env"

const service = axios.create({
    baseURL: env.prod.baseUrl
})

// 配置全局axios请求时加载
let loading
function startLoading() {
    loading = ElLoading.service({
        lock: true,
        text: '加载中...(如果长时间未响应, 请刷新页面后重试)',
        background: 'rgba(122, 122, 122, 0.2)',
    })
}
function endLoading() {
    loading.close()
}

// 添加请求拦截器
service.interceptors.request.use(request => {
    startLoading()
    return request
}, error => {
    return Promise.reject("出错", error)
}
)

// 添加响应拦截器
service.interceptors.response.use(response => {
    endLoading()
    return response
}, error => {
    return Promise.reject(error)
}
)
export default service