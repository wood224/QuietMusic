import { createStore } from 'vuex'

export default createStore({
  state: {
    searchSongs: [],      //搜索列表
    musicInfo: {},        //音乐信息
    musicUrl: '',         //音乐 url
    musicPlayerId: -1,    //传给 MusicPlayer 组件的 id
    searchKeywords: '',   //搜索的关键词
    banners: [],           //轮播图
    baseURLCloudMusic: 'http://180.76.136.85:3000'   //网易云API默认路径
  },
  getters: {
    getBaseURLCloudMusic(state) {
      return state.baseURLCloudMusic
    }
  },
  mutations: {
    setSearchSongs(state, songs) {
      state.searchSongs = songs
    },
    setMusicInfo(state, musicInfo) {
      state.musicInfo = musicInfo
    },
    setMusicUrl(state, url) {
      state.musicUrl = url
    },
    setMusicPlayerId(state, id) {
      state.musicPlayerId = id
    },
    setSearchKeywords(state, keywords) {
      state.searchKeywords = keywords
    },
    setBanners(state, banners) {
      state.banners = banners
    }
  },
  actions: {
  },
  modules: {
  }
})
