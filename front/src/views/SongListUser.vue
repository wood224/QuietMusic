<template>
  <div class="song-list-container">
    <div class="left">
      <div class="pic">
        <img :src="songListDetail.coverImg" alt="">
      </div>
      <div class="detail">
        <span>名称：</span>{{ songListDetail.name }}
        <br>
        <span>创建人：</span>{{ songListDetail.userName }}
        <br>
        <span>更新时间：</span>{{ songListDetail.updateTime }}
      </div>
      <div class="intro scrollbar">
        <span>简介：</span>
        <span class="description">{{ songListDetail.description }}</span>
      </div>
    </div>
    <div class="right">
      <div class="title">
        <h6>&lt;{{ songListDetail.name }}&gt; －歌曲列表</h6>
        <el-button type="primary" @click="addAllPlaylistSong">
          <span class="text">
            <i class="fa fa-play"></i>将歌曲添加至播放列表
          </span>
        </el-button>
      </div>
      <div class="songs">
        <SongsList :list="PageSongs" type="songlistUser" :count="songList.length" @getAll="getPageSongs"
          v-loading="loading">
        </SongsList>
      </div>
    </div>
  </div>
</template>

<script>
import { mapState, mapMutations, mapActions } from "vuex"
import { getPlaylistDetail, getCheckMusic } from "../http/api"
import SongsList from "../components/SongsList.vue"
import { ElMessage } from 'element-plus'

export default {
  name: 'SongList',
  data() {
    return {
      songListDetail: {},     //歌单详情信息
      songList: [],           //歌单中歌曲列表
      PageSongs: [],           //歌单中分页歌曲列表
      creatorName: '',        //歌单创建作者名
      updateTime: '',         //歌单更新时间
      duration: '',           //歌曲时长

      loading: false,
    }
  },
  mounted() {
    // this.setUserSongListId(sessionStorage.getItem('userSongListId'))
    const id = this.$route.params.id
    this.loading = true
    this.$http.get('/songlist/getdetails', {
      params: {
        id: id,
      }
    })
      .then(res => {
        const data = res.data.data
        this.songListDetail = data
        this.songList = data.songs
        this.getPageSongs(0)
        this.loading = false
      })
      .catch(err => { console.log(err); })

  },
  computed: {
    ...mapState(['userSongListId', 'playlistId'])
  },
  methods: {
    ...mapMutations(['setUserSongListId', 'setPlaylistId', 'setPlaylist']),
    ...mapActions(['play', 'getPlaylistSongs']),

    ////分页获取歌曲
    getPageSongs(offset) {
      this.PageSongs = this.songList.slice(offset, offset + 10)
    },

    //添加歌单所有歌曲到歌曲列表
    async addAllPlaylistSong() {
      let msg = ElMessage('添加中...')
      for (let i of this.songList) {
        const { data: res } = await getCheckMusic(i.id)
        if (res.success === true) {
          let songInfo = i
          let artists = []
          songInfo.ar.forEach(e => {
            artists.push(e.name)
          })
          await this.$http.post('/listsongs/add', {
            listId: this.playlistId,
            musicId: songInfo.id,
            musicName: songInfo.name,
            singerName: artists,
            time: songInfo.dt
          })
        }
      }
      msg.close()
      ElMessage.success('已将所有有版权的歌曲添加至播放列表')
      this.getPlaylistSongs()
    }
  },
  components: {
    SongsList,
  }
}
</script>

<style lang="less" >
.song-list-container {
  display: flex;
  width: 1000px;
  height: calc(100% - 100px);
  margin: 0 auto;
  padding: 10px 0;
  font-size: 14px;
  box-sizing: border-box;

  .left {
    width: 20%;
    height: 100%;
    margin-right: 20px;

    span {
      font-size: 12px;
      color: #7d7d7d;
    }

    .pic {
      width: 180px;
      height: 180px;

      img {
        width: 100%;
        height: 100%;
        object-fit: contain;
      }
    }

    .intro {
      height: 340px;
      overflow: auto;

      .description {
        font-size: 14px;
        color: black;
      }
    }
  }

  .right {
    width: 80%;
    padding: 0 20px;
    border-left: 1px solid gray;

    .title {
      padding-bottom: 10px;
      border-bottom: 1px solid gray;

      h6 {
        font-weight: 800;
      }

      .el-button {
        .text {
          margin: auto;
          font-size: 14px;

          .fa {
            height: auto;
          }
        }
      }
    }
  }
}

tr {
  cursor: pointer;
}
</style>