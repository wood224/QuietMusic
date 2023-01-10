<template>
  <div class="songs-list-wrapper">
    <el-scrollbar>
      <el-table :data="songsList" style="width: 100%" @cell-click="playSong" row-key="id" :row-style="rowStyle" stripe>
        <el-table-column width="50" v-if="type === 'top'">
          <template #default="scope">
            {{ 10 * (currentPage - 1) + (scope.$index + 1) }}
          </template>
        </el-table-column>
        <el-table-column label="歌曲名" :width="type === 'top' ? 450 : 350">
          <template #default="scope">
            <div class="song-name">
              <div class="name">
                <span>{{ scope.row.name }}</span>
              </div>
              <div class="btn-list">
                <el-tooltip content="收藏" :show-after="400" :hide-after="0">
                  <div class="collect" @click.stop="openSelectList(scope.row.id)">
                    <el-icon>
                      <FolderAdd />
                    </el-icon>
                  </div>
                </el-tooltip>
                <el-tooltip v-if="deleteShow" content="移出歌单" :show-after="400" :hide-after="0">
                  <div class="delete" @click.stop="openDelete(scope.row.userMusicId)">
                    <el-icon>
                      <DeleteFilled />
                    </el-icon>
                  </div>
                </el-tooltip>
              </div>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="歌手" :width="type === 'top' ? 300 : 200">
          <template #default="scope">
            <span class="singer-name" :class="{ link: item.id !== 0 }" @click.stop="goSingerDetail(item.id)"
              v-for="item in scope.row.ar" :key="item.name">
              {{ item.name }}&nbsp;
            </span>
          </template>
        </el-table-column>
        <el-table-column label="专辑" v-if="type !== 'top'">
          <template #default="scope">
            <span class="link" @click.stop="goAlbumDetail(scope.row.al.id)">{{ scope.row.al.name }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="dt" label="时长" />
      </el-table>
    </el-scrollbar>
    <el-pagination v-model:current-page="currentPage" @current-change="setPage" :hide-on-single-page="true" background
      layout="prev, pager, next" :total="count" />

    <Collect :single="true" :music-id="collectMusicId" v-if="collectView" @close="closeSelectList"></Collect>
    <el-dialog v-model="deleteDialogVisible" title="提示" width="30%">
      <span>确认移出该歌曲吗</span>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="deleteDialogVisible = false">考虑一下</el-button>
          <el-button type="danger" @click="deleteSong(deleteSongId)">
            确认移除
          </el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, toRefs, getCurrentInstance, computed } from 'vue';
import { useStore } from "vuex";
import { getTime, checkMusic } from "../fun"
import Collect from './Collect.vue';
import { ElMessage } from 'element-plus'
import { useRouter } from "vue-router";

const { proxy } = getCurrentInstance()
const store = useStore()
const router = useRouter()

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
    default: 10,
  },
  isUsers: {
    type: Boolean,
    default: false,
  }
})
const emit = defineEmits(['getAll', 'deleteSong'])

const deleteShow = ref(false)
const deleteListId = ref(-1)
const { list, type, count, isUsers } = toRefs(props);
if (type.value === 'songlistUser' && isUsers.value) {
  deleteShow.value = true
  deleteListId.value = list.value[0].id
}

const currentPage = ref(1)
const setPage = () => {
  emit('getAll', (currentPage.value - 1) * 10)
}

const songsList = computed(() => {
  return list.value.map(item => {
    //用户歌单
    if (type.value === 'songlistUser') {
      return {
        id: item.musicId,
        userMusicId: item.id,
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
    //搜索
    else if (type.value === 'search') {
      return {
        id: item.id,
        name: item.name,
        ar: item.artists,
        al: item.album,
        dt: item.duration,
      }
    }
    //排行榜
    else if (type.value === 'top') {
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
    //其它
    else {
      return { ...item }
    }
  })
})
const collectView = ref(false)
const collectMusicId = ref()      //收藏的歌单id

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

//打开选择收藏歌单
const openSelectList = (id) => {
  collectMusicId.value = id
  collectView.value = true
}

// 关闭选择收藏歌单
const closeSelectList = () => {
  collectView.value = false
}

//打开歌手页面
const goSingerDetail = (id) => {
  if (id === 0) return
  router.push('/singerDetail/' + id)
}

//打开专辑页面
const goAlbumDetail = (id) => {
  if (id === 0) return
  router.push('/albumDetail/' + id)
}

const deleteDialogVisible = ref(false)
const deleteSongId = ref(-1)
//打开删除提醒弹窗
const openDelete = (id) => {
  deleteDialogVisible.value = true
  deleteSongId.value = id
}
//移除歌曲
const deleteSong = (id) => {
  emit('deleteSong', id, deleteListId.value, (currentPage.value - 1) * 10)
  deleteDialogVisible.value = false
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
          display: flex !important;
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

      .collect,
      .delete {
        margin-right: 6px;

        ::v-deep(.el-icon) {
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

  span {
    &.singer-name {
      cursor: default;
    }

    &.link {
      cursor: pointer;

      &:hover {
        text-decoration: underline;
        ;
      }
    }
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