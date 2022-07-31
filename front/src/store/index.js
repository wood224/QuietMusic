import { createStore } from 'vuex'
import { getSearchApi, getCheckMusic } from "../http/api"
import { toRaw } from '@vue/reactivity'

export default createStore({
  state: {
    userInfo: {},         //用户信息
    searchSongs: [],      //搜索列表
    musicInfo: {},        //音乐信息
    musicUrl: '',         //音乐 url
    musicPlayerId: -1,    //当前播放的音乐 id
    searchKeywords: '',   //搜索的关键词
    banners: [],          //轮播图
    lyricCurrent: 0.00,   //歌词当前时间
    lastKey: 0.00,        //上一句歌词播放时间
    playlistId: 0,        //播放列表 id
    playlist: [],         //播放列表歌曲
    songListId: 0,        //歌单 id
  },
  getters: {
  },
  mutations: {
    setUserInfo(state, userInfo) {
      if (JSON.stringify(state.userInfo) === JSON.stringify(userInfo)) return
      state.userInfo = userInfo
    },
    setSearchSongs(state, songs) {
      if (JSON.stringify(state.songs) === JSON.stringify(songs)) return
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
    },
    setPlaylistId(state, id) {
      state.playlistId = id
    },
    setPlaylist(state, playlist) {
      if (JSON.stringify(state.playlist) === JSON.stringify(playlist)) return
      state.playlist = playlist
    },
    setSongListId(state, id) {
      state.songListId = id
    }
  },
  actions: {
    //播放歌曲
    play({ commit }, row) {
      const song = toRaw(row)
      getCheckMusic(song.id)
        .then(res => {
          if (res.data.success !== true) {
            return ElMessage.warning('抱歉, 该歌暂无版权')
          }
          // context.setMusicPlayerId(song.id)
          commit('setMusicPlayerId', song.id)
        })
    },
  },
  modules: {
  }
})
