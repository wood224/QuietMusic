import { createRouter, createWebHashHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    redirect: '/home'
  },
  {
    path: '/home',
    component: () => import('../views/Home.vue')
  },
  {
    path: '/rank',
    component: () => import('../views/Rank.vue'),
  },
  {
    path: '/song',
    component: () => import('../views/Song.vue')
  },
  {
    path: '/singer',
    component: () => import('../views/Singer.vue')
  },
  {
    path: '/search',
    component: () => import('../views/Search.vue')
  },
  {
    path: '/songDetails',
    component: () => import('../views/SongDetails.vue')
  },
  {
    path: '/songList/:id',
    component: () => import('../views/SongList.vue')
  },
  {
    path: '/songListUser/:id',
    component: () => import('../views/SongListUser.vue')
  },
  {
    path: '/albumDetail/:id',
    component: () => import('../views/AlbumDetail.vue')
  },
  {
    path: '/userHome',
    component: () => import('../views/UserHome.vue'),
    beforeEnter: (to, from, next) => {
      if (localStorage.getItem('userInfo') !== null) {
        next()
      } else {
        return location.href = 'login.html'
      }
    }
  },
  {
    path: '/singerDetail/:id',
    component: () => import('../views/SingerDetail.vue')
  },
]

const router = createRouter({
  history: createWebHashHistory(),
  routes
})

export default router
