import { createStore } from 'vuex'

export default createStore({
  state: {
    searchSongs: [],
    musicInfo: {}
  },
  getters: {

  },
  mutations: {
    setSearchSongs(state, songs) {
      state.searchSongs = songs
    },
    setMusicInfo(state, musicInfo) {
      state.musicInfo = musicInfo
    }
  },
  actions: {
  },
  modules: {
  }
})
