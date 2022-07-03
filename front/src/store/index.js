import { createStore } from 'vuex'

export default createStore({
  state: {
    searchSongs: []
  },
  getters: {

  },
  mutations: {
    getSearchSongs(state, songs) {
      state.searchSongs = songs
    }
  },
  actions: {
  },
  modules: {
  }
})
