import { createRouter, createWebHashHistory } from "vue-router"

import Login from './Login.vue'

const routes = [
    {
        path: '/',
        redirect: '/login'
    },
    {
        path: '/login',
        component: Login,
    },
]

const router = createRouter({
    history: createWebHashHistory(),
    routes
})

export default router