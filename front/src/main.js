import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import axios from 'axios'
import './css/index.css'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import bootstrapcss from './assets/bootstrap/css/bootstrap.min.css'
import bootstrapjs from './assets/bootstrap/js/bootstrap.bundle.min'

const app = createApp(App)

for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component)
}

axios.defaults.baseURL = 'http://180.76.136.85:8080'
app.config.globalProperties.$http = axios
window.axios = require('axios')
window.axios.defaults.headers.common['X-Requested-With'] = 'XMLHttpRequest'

app.use(store).use(router)

app.mount('#app')
