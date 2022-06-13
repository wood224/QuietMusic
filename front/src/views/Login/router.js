import { createRouter, createWebHashHistory } from "vue-router"

import Login from './Login.vue'
import Register from './Register.vue'

const routes = [
    {
        path: '/',
        redirect: '/login'
    },
    {
        path: '/login',
        component: Login
    },
    {
        path: '/register',
        component: Register
    }
]

const router = createRouter({
    history: createWebHashHistory(),
    routes
})

export default router