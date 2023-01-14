<template>
  <div class="collect-wrapper">
    <el-dialog v-model="loginDialogVisible" @close="emit('close')" title="提示" width="30%" :align-center="true">
      <span>请先登录</span>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="loginDialogVisible = false">稍后再说</el-button>
          <el-button type="primary" @click="login">现在登录</el-button>
        </span>
      </template>
    </el-dialog>

    <el-dialog v-model="collectDialogVisible" @close="emit('close')" title="选择歌单" append-to-body width="30%"
      :align-center="true">
      <CreateSongList v-show="createView" @setCreateView="setCreateView" @getUserSongList="getUserSongList"
        :user-id="userInfo.id"></CreateSongList>
      <div class="create" @click="setCreateView">
        <div class="icon">
          <el-icon>
            <CirclePlus />
          </el-icon>
        </div>
        <div class="text">新建歌单</div>
      </div>
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
import CreateSongList from './CreateSongList.vue';
import { getPlaylistAll } from "../http/api";
import { getTime, checkMusic } from "../fun"


const props = defineProps({
  musicId: {
    type: Number,
    default: -1,
  },
  songlistDetailId: {
    type: Number,
    default: -1,
  },
  single: {           //true：单曲 false：所有
    type: Boolean,
    required: true,
  },
  playList: {
    type: Boolean,
    default: false,
  }
})
const { musicId, songlistDetailId, single, playList } = toRefs(props)

const emit = defineEmits(['close'])

const { proxy } = getCurrentInstance()

const userInfo = ref(JSON.parse(localStorage.getItem('userInfo')))

const userSongList = ref([])  //用户歌单列表
const collectDialogVisible = ref(false)
const loginDialogVisible = ref(false)
const collectMusicInfo = ref({})  //收藏的歌曲详情

//获取用户歌单
const getUserSongList = () => {
  proxy.$http.get('/songlist/lists', {
    params: {
      id: userInfo.value.id
    }
  })
    .then(async res => {
      const data = res.data.data
      for (let i in data) {
        const item = data[i];
        item.coverImg = item.coverImg[0] === '/' ? item.coverImg : await proxy.$fun.getImg(item.coverImg)
      }
      userSongList.value = data
    })
}

//选择收藏歌单(单曲)
const selectList = () => {
  if (userInfo.value !== null) {
    if (single.value && musicId.value === -1) return emit('close')
    if (!single.value && songlistDetailId.value === -1) return emit('close')
    if (single.value) {    //单曲
      checkMusic(musicId.value).then(res => {
        if (res.result) {
          collectMusicInfo.value = res.info
        }
      })
    }
    collectDialogVisible.value = true
    getUserSongList()
  } else {
    loginDialogVisible.value = true
  }
}
selectList()

//收藏
const collect = async (id) => {
  if (collectMusicInfo === null) return collectDialogVisible = false
  if (single.value) {
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
    })
  } else {
    if (playList.value) {
      if (single.value) { }
      else {

      }
    }
    else {
      const { data } = await getPlaylistAll(songlistDetailId.value, 0, 0);
      let songs = []
      for (let i in data.songs) {
        const item = data.songs[i];
        if (item.noCopyrightRcmd) {
          continue
        }
        songs.push({
          listId: id,
          musicId: item.id,
          musicName: item.name,
          singerName: item.ar.map(item => {
            return item.name;
          }),
          album: item.al.name,
          time: getTime(item.dt)
        })
      }
      proxy.$http.post('/songlistdetails/insertAll',
        songs
      ).then(res => {
        const data = res.data
        if (data.code === 200) {
          ElMessage.success('添加成功')
        }
        else {
          ElMessage.warning(data.msg)
        }
      })
    }
  }
  collectDialogVisible.value = false
  emit('close')
}

//弹窗跳转登录
const login = () => {
  loginDialogVisible.value = false
  emit('close')
  return location.href = 'login.html'
}

const createView = ref(false)

const setCreateView = () => {
  createView.value = !createView.value
  // collectDialogVisible.value = !collectDialogVisible.value
}
</script>

<style lang="less" scoped>
.create {
  display: flex;
  align-items: center;
  padding: 10px;
  background: #e6e6e6;
  cursor: pointer;
  font-size: 20px;

  // border-bottom: 2px solid gray;
  .icon {
    margin-right: 20px;
    width: 60px;
    height: 60px;

    ::v-deep(.el-icon) {
      width: 100%;
      height: 100%;

      svg {
        width: 100%;
        height: 100%;
      }
    }
  }
}

.dialog-wrapper {
  .songlist {
    margin: 20px 0;
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
</style>