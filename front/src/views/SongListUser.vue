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
        <div class="action">
          <el-button type="primary" @click="addAllPlaylistSong">
            <span class="text">
              <i class="fa fa-play"></i>将歌曲添加至播放列表
            </span>
          </el-button>
          <el-button v-if="isUsers" type="danger" @click="clearDialogVisible = true">
            <span class="text">
              <el-icon>
                <DeleteFilled />
              </el-icon>
              <span>清空歌单</span>
            </span>
          </el-button>
        </div>
      </div>
      <div class="songs">
        <SongsList v-if="songList.length !== 0" :list="PageSongs" type="songlistUser" :isUsers="isUsers"
          :count="songList.length" @getAll="getPageSongs" @deleteSong="deleteSong" v-loading="loading">
        </SongsList>
      </div>
    </div>

    <el-dialog v-model="clearDialogVisible" title="提示" width="30%">
      <span>确定清空歌单吗</span>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="clearDialogVisible = false">考虑一下</el-button>
          <el-button type="danger" @click="clearSonglist">
            确认清空
          </el-button>
        </span>
      </template>
    </el-dialog>
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
      paramsId: -1,
      songListDetail: {},     //歌单详情信息
      songList: [],           //歌单中歌曲列表
      PageSongs: [],           //歌单中分页歌曲列表
      creatorName: '',        //歌单创建作者名
      updateTime: '',         //歌单更新时间
      duration: '',           //歌曲时长

      loading: false,

      clearDialogVisible: false,
    }
  },
  mounted() {
    this.paramsId = this.$route.params.id
    this.getSonglistDetail(this.paramsId, 0)

  },
  computed: {
    ...mapState(['playlistId']),
    isUsers() {
      return this.songListDetail.userId === JSON.parse(localStorage.getItem('userInfo')).id
    }
  },
  methods: {
    ...mapMutations(['setPlaylistId', 'setPlaylist']),
    ...mapActions(['play', 'getPlaylistSongs']),

    //获取歌单详情
    getSonglistDetail(id, page) {
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
          this.getPageSongs(page)
          this.loading = false
        })
        .catch(err => { console.log(err); })
    },

    //分页获取歌曲
    getPageSongs(offset) {
      this.PageSongs = this.songList.slice(offset, offset + 10)
    },

    //移除歌曲
    deleteSong(id, listId, page) {
      this.$http.delete('/songlistdetails/delete', {
        params: {
          id: id,
          listId: listId
        }
      }).then(res => {
        if (res.data.code === 200) {
          ElMessage.success('删除成功！')
        } else {
          ElMessage.danger('删除失败！')
        }
        this.getSonglistDetail(this.paramsId, page)
      })
    },

    //清空歌单
    clearSonglist() {
      this.$http.delete('/songlistdetails/delall', {
        params: {
          listId: this.songListDetail.id
        }
      }).then(res => {
        if (res.data.code === 200) {
          ElMessage.success('清空歌单成功！')
        } else {
          ElMessage.error('清空歌单失败！')
        }
        setTimeout(location.reload(), 1000)
      })
    },

    //添加歌单所有歌曲到歌曲列表
    async addAllPlaylistSong() {
      let msg = ElMessage({ message: '添加中...', duration: 0 })
      for (let i of this.songList) {
        const { data: res } = await getCheckMusic(i.musicId)
        if (res.success === true) {
          let songInfo = i
          await this.$http.post('/listsongs/add', {
            listId: this.playlistId,
            musicId: songInfo.musicId,
            musicName: songInfo.musicName,
            singerName: songInfo.singerName,
            time: songInfo.time
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