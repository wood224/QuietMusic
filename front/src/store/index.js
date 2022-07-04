import { createStore } from 'vuex'

export default createStore({
  state: {
    searchSongs: [],
    musicInfo: {},
    musicUrl: ''
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
    }
  },
  actions: {
  },
  modules: {
  }
})
