import { createStore } from 'vuex'

export default createStore({
  state: {
    searchSongs: [],      //搜索列表
    musicInfo: {},        //音乐信息
    musicUrl: '',         //音乐 url
    musicPlayerId: -1,    //传给 MusicPlayer 组件的 id
    searchKeywords: '',   //搜索的关键词
    banners: [],          //轮播图
    lyricCurrent: 0,       //歌词当前时间
    lastKey: -1           //上一句歌词破防时间
  },
  getters: {
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
    },
    setLyricCurrent(state, current) {
      state.lyricCurrent = current
    },
    setLastKey(state, key) {
      state.lastKey = key
    }
  },
  actions: {
  },
  modules: {
  }
})
