import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import axios from 'axios'
import './css/index.css'
import * as fun from './fun'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import bootstrapcss from './assets/bootstrap/css/bootstrap.min.css'
import bootstrapjs from './assets/bootstrap/js/bootstrap.bundle.min'

const app = createApp(App)

for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component)
}

axios.defaults.baseURL = window.BASEURL.baseURL;
app.config.globalProperties.$http = axios;
app.config.globalProperties.$fun = fun;
window.axios = require('axios')
window.axios.defaults.headers.common['X-Requested-With'] = 'XMLHttpRequest'

app.use(store).use(router)

app.mount('#app')
