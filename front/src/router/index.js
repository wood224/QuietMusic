import { createRouter, createWebHashHistory } from 'vue-router'
import Home from '../views/Home.vue'
import Rank from '../views/Rank.vue'
import Song from '../views/Song.vue'
import Singer from '../views/Singer.vue'
import Search from '../views/Search.vue'

const routes = [
  {
    path: '/',
    redirect: '/home'
  },
  {
    path: '/home',
    component: Home
  },
  {
    path: '/rank',
    component: Rank
  },
  {
    path: '/song',
    component: Song
  },
  {
    path: '/singer',
    component: Singer
  },
  {
    path: '/search',
    component: Search
  }
]

const router = createRouter({
  history: createWebHashHistory(),
  routes
})

export default router
