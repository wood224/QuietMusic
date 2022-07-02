import { createApp } from 'vue'
import Login from './Login.vue'
import router from './router'
import store from './store'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import axios from 'axios'

const app = createApp(Login)

app.use(store)
app.use(router)

axios.defaults.baseURL = 'http://180.76.136.85:8080/user'
app.config.globalProperties.$http = axios

for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component)
}

app.mount('#app')
