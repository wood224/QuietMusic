import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
// import store from './store'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import axios from 'axios'

const app = createApp(App)

// app.use(store)
app.use(router)

axios.defaults.baseURL = 'http://180.76.136.85:8080/user'
app.config.globalProperties.$http = axios

for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component)
}

app.mount('#app')
