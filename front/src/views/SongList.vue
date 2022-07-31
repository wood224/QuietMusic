<template>
    <div class="song-list-container">
        <div class="left">
            <div class="pic">
                <img :src="songListDetail.coverImgUrl" alt="">
            </div>
            <div class="detail">
                <span>名称：</span>{{ songListDetail.name }}
                <br>
                <span>创建人：</span>{{ creatorName }}
                <br>
                <span>更新时间：</span>{{ updateTime }}
            </div>
            <div class="intro">
                <span>简介：</span>
                <span class="description">{{ songListDetail.description }}</span>
            </div>
        </div>
        <div class="right">
            <div class="title">
                <h6>&lt;{{ songListDetail.name }}&gt; －歌曲列表</h6>
                <el-button type="primary" @click="addAllPlaylistSong">
                    <span class="text">
                        <i class="fa fa-play"></i>添加至播放列表
                    </span>
                </el-button>
            </div>
            <div class="songs">
                <el-table :data="songList" @cell-click="play">
                    <el-table-column prop="name" label="歌曲名" />
                    <el-table-column label="歌手" width="300">
                        <template #default="scope">
                            <span v-for="item in scope.row.ar" :key="item.id">
                                {{ item.name }}&nbsp;
                            </span>
                        </template>
                    </el-table-column>
                    <el-table-column prop="al.name" label="专辑" />
                    <el-table-column prop="dt" label="时长" />
                </el-table>
            </div>
        </div>
    </div>
</template>

<script>
import { mapState, mapMutations, mapActions } from "vuex"
import { getPlaylistDetail } from "../http/api"
import { getTime } from "../fun"
import { getCheckMusic } from "../http/api"

export default {
    name: 'SongList',
    data() {
        return {
            songListDetail: {},     //歌单详情信息
            songList: [],           //歌单中歌曲列表
            creatorName: '',        //歌单创建作者名
            updateTime: '',         //歌单更新时间
            duration: '',           //歌曲时长

        }
    },
    mounted() {
        this.setSongListId(sessionStorage.getItem('songListId'))
        getPlaylistDetail(this.songListId)
            .then(res => {
                const playlisy = res.data.playlist
                this.songListDetail = playlisy
                this.songList = playlisy.tracks
                this.songList.forEach(item => {
                    item.dt = getTime(item.dt)
                })
                this.creatorName = playlisy.creator.nickname
                const date = new Date(playlisy.updateTime)
                const y = date.getFullYear()
                const m = date.getMonth() + 1
                const d = date.getDay()
                this.updateTime = `${y}-${m}-${d}`
            })

    },
    computed: {
        ...mapState(['songListId', 'playlistId'])
    },
    methods: {
        ...mapMutations(['setSongListId', 'setPlaylistId', 'setPlaylist']),
        ...mapActions(['play', 'getPlaylistSongs']),

        //添加歌单所有歌曲到歌曲列表
        addAllPlaylistSong() {
            for (let i = 0; i < this.songList.length; i++) {
                getCheckMusic(this.songList[i].id)
                    .then(res => {
                        if (res.data.success === true) {
                            let songInfo = this.songList[i]
                            let artists = []
                            songInfo.ar.forEach(e => {
                                artists.push(e.name)
                            })
                            this.$http.post('/listsongs/add', {
                                listId: this.playlistId,
                                musicId: songInfo.id,
                                musicName: songInfo.name,
                                singerName: artists,
                                time: songInfo.dt
                            }).then(res => {
                                if (res.data.code === 200) {
                                    this.getPlaylistSongs()
                                }
                            })
                        }
                    })
            }
            ElMessage('已将所有有版权的歌曲添加至播放列表')
        },
    },
}
</script>

<style lang="less" >
.song-list-container {
    display: flex;
    width: 1000px;
    margin: 0 auto 100px;
    padding-top: 10px;
    font-size: 14px;

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