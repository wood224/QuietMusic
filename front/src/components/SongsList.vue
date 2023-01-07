<template>
  <div class="songs-list-wrapper">
    <el-table :data="songsList" height="100%" style="width: 100%" @cell-click="playSong" :row-style="rowStyle" stripe>
      <el-table-column type="index" width="50" v-if="type === 'top'" />
      <el-table-column label="歌曲名" :width="type === 'top' ? 450 : 380">
        <template #default="scope">
          <div class="song-name">
            <div class="name">
              <span>{{ scope.row.name }}</span>
            </div>
            <div class="btn-list">
              <el-tooltip content="收藏" :show-after="400" :hide-after="0">
                <div class="collect" @click.stop="selectList(scope.row.id)">
                  <el-icon>
                    <FolderAdd />
                  </el-icon>
                </div>
              </el-tooltip>
            </div>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="歌手" :width="type === 'top' ? 300 : 200">
        <template #default="scope">
          <router-link :to="'/singerDetail/' + item.id" v-for="item in scope.row.ar" :key="item.name">
            {{ item.name }}&nbsp;
          </router-link>
        </template>
      </el-table-column>
      <el-table-column prop="al.name" label="专辑" v-if="type !== 'top'" />
      <el-table-column prop="dt" label="时长" />
    </el-table>
    <el-pagination v-model:current-page="currentPage" @current-change="setPage" :hide-on-single-page="true" background
      layout="prev, pager, next" :total="count" />

    <el-dialog v-model="loginDialogVisible" title="提示" width="30%" :align-center="true">
      <span>请先登录</span>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="loginDialogVisible = false">稍后再说</el-button>
          <el-button type="primary" @click="login">现在登录</el-button>
        </span>
      </template>
    </el-dialog>

    <el-dialog v-model="collectDialogVisible" title="选择歌单" width="30%" :align-center="true">
      <el-scrollbar>
        <ul class="dialog-wrapper">
          <li v-for="item in userSongList" :key="item.id" @click="collect(item.id)">
            <div class="songlist">
              <div class="pic">
                <img :src="item.coverImg" alt="">
              </div>
              <div class="name">{{ item.name }}</div>
            </div>
          </li>
        </ul>
      </el-scrollbar>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, toRefs, getCurrentInstance, computed } from 'vue';
import { useStore } from "vuex";
import { getTime, checkMusic } from "../fun"
import { ElMessage } from 'element-plus'

const { proxy } = getCurrentInstance()
const store = useStore()

const props = defineProps({
  list: {
    type: Array,
    default: [],
    required: true
  },
  type: {
    type: String,
    default: '',
  },
  count: {
    type: Number,
    default: 0,
    required: true,
  }
})
const emit = defineEmits(['getAll'])

const { list, type, count } = toRefs(props);

const currentPage = ref(1)
const setPage = () => {
  emit('getAll', (currentPage.value - 1) * 10)
}

const songsList = computed(() => {
  return list.value.map(item => {
    if (item.hasOwnProperty("musicId")) {
      return {
        id: item.musicId,
        name: item.musicName,
        ar: item.singerName.map(arName => {
          return {
            name: arName,
          }
        }),
        al: { name: item.album },
        dt: item.time,
      }
    }
    else if (item.hasOwnProperty("artists")) {
      return {
        id: item.id,
        name: item.name,
        ar: item.artists,
        al: item.album,
        dt: item.duration,
      }
    }
    else if (item.hasOwnProperty("views")) {
      return {
        id: item.id,
        name: item.name,
        ar: item.singerName === null ? [''] : item.singerName.map(arName => {
          return {
            name: arName,
          }
        }),
        al: { name: item.album },
        dt: item.time,
      }
    }
    else {
      return { ...item }
    }
  })
})

const userSongList = ref([])  //用户歌单列表
const collectDialogVisible = ref(false)
const loginDialogVisible = ref(false)
const collectMusicInfo = ref({})  //收藏的歌曲详情

//表格行样式
const rowStyle = ({ row, rowIndex }) => {
  if (!row.hasOwnProperty("noCopyrightRcmd")) return
  if (row.noCopyrightRcmd !== null) {
    return {
      color: "#bbb",
    }
  }
}

//播放
const playSong = (row) => {
  store.dispatch('play', row)
}

//选择收藏歌单
const selectList = (id) => {
  const userInfo = JSON.parse(localStorage.getItem('userInfo'))
  if (userInfo !== null) {
    checkMusic(id).then(res => {
      if (res.result) {
        collectMusicInfo.value = res.info
        collectDialogVisible.value = true
        proxy.$http.get('/songlist/lists', {
          params: {
            id: userInfo.id,
          }
        }).then(res => {
          const data = res.data.data
          userSongList.value = data
        })
      }
    });
  } else {
    loginDialogVisible.value = true
  }
}

//收藏
const collect = (id) => {
  if (collectMusicInfo === null) return collectDialogVisible = false
  proxy.$http.post('/songlistdetails/add', {
    listId: id,
    musicId: collectMusicInfo.value.id,
    musicName: collectMusicInfo.value.name,
    singerName: collectMusicInfo.value.ar.map(item => {
      return item.name;
    }),
    album: collectMusicInfo.value.al.name,
    time: getTime(collectMusicInfo.value.dt)
  }).then(res => {
    const data = res.data
    if (data.code === 200) {
      ElMessage.success('收藏成功')
    }
    else {
      ElMessage.warning(data.msg)
    }
    collectDialogVisible.value = false
  })
}

//弹窗跳转登录
const login = () => {
  loginDialogVisible.value = false
  return location.href = 'login.html'
}

</script>

<style lang="less" scoped>
.songs-list-wrapper {
  height: 100%;

  ::v-deep(.el-table) {
    tr {
      height: 45px;
      cursor: pointer;

      &:hover {
        .btn-list {
          display: block !important;
        }
      }
    }
  }

  .song-name {
    display: flex;
    align-items: center;

    .name {
      margin-right: auto;
    }

    .btn-list {
      display: none;

      .collect {
        .el-icon {
          width: 20px;
          height: 20px;
          color: #999999;

          &:hover {
            color: #66CCFF
          }

          svg {
            width: 100%;
            height: 100%;
          }
        }
      }
    }
  }

  a {
    color: inherit;
  }

  .dialog-wrapper {
    .songlist {
      margin-bottom: 20px;
      display: flex;
      align-items: center;
      box-sizing: border-box;
      padding: 10px;
      cursor: pointer;

      &:hover {
        background-color: #f2f2f2;
      }

      .pic {
        margin-right: 20px;
        width: 60px;
        height: 60px;

        img {
          width: 100%;
          height: 100%;
          object-fit: contain;
        }
      }

      .name {
        font-size: 20px;
      }
    }
  }
}
</style>