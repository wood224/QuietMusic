import { createStore } from 'vuex'

export default createStore({
  state: {
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
    },
    setPlaylistId(state, id) {
      state.playlistId = id
    },
    setPlaylist(state, playlist) {
      state.playlist = playlist
    },
    setSongListId(state, id) {
      state.songListId = id
    }
  },
  actions: {
  },
  modules: {
  }
})
