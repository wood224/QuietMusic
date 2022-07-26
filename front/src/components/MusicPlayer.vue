<template>
    <div class="music-container" :class="{ 'play': isPlaying }" ref="musicContainer" v-loading="loading"
        element-loading-background="rgba(122, 122, 122, 0.8)" element-loading-text="加载中...(如果长时间未响应, 请刷新页面后重试)">
        <div class="music-info">
            <!-- 播放列表 -->
            <div class="playlist" v-show="playlistView">
                <div class="title">播放列表/{{ playlist.length }}
                    <div class="close" @click="setPlaylistView">
                        <i class="fa fa-close"></i>
                    </div>
                </div>
                <div class="musicList">
                    <li v-for="(item, index) in playlist" :key="item.id">
                        <div class="musicList-item" @click="play(item.musicId, index)">
                            <div class="musicList-number">{{ index + 1 }}</div>
                            <div class="musicList-name">{{ item.musicName }}
                                <el-tooltip class="box-item" effect="light" content="移除列表" placement="top"
                                    :hide-after="0">
                                    <div class="delete" @click.stop="deletePlaylistSong(item.musicId)">
                                        <i class="fa fa-trash"></i>
                                    </div>
                                </el-tooltip>
                            </div>
                            <div class="musicList-artist textHidden">
                                <span v-for="(name, index) in item.singerName" :key="index">
                                    <span v-if="index !== 0">/</span>{{ name }}
                                </span>
                            </div>
                            <div class="musicList-time">{{ item.time }}</div>
                        </div>
                    </li>
                </div>
            </div>
            <div class="music-img" @click="goSongDetails">
                <img :src="songInfo.alPicUrl" alt="" @error="picNull(songInfo)">
            </div>
            <div class="music-control">
                <div class="music-name" @click="goSongDetails">
                    <span class="name">{{ songInfo.name }}</span>
                    <div class="singers">
                        <span class="singer" v-for="(item, index) in songInfo.ar" :key="index">
                            <span v-if="index !== 0">/</span>{{ item.name }}
                        </span>
                    </div>
                </div>
                <div class="btns">
                    <div class="nav">
                        <button id="prev" @click="prevSong">
                            <i class="fa fa-backward"></i>
                        </button>
                        <button id="play" @click="playFun">
                            <i class="fa" :class="[isPlaying ? 'fa-pause' : 'fa-play']"></i>
                        </button>
                        <button id="next" @click="nextSong">
                            <i class="fa fa-forward"></i>
                        </button>
                    </div>
                    <div class="volume-info">
                        <button @click="setMute">
                            <i class="fa fa-volume-mute" v-show="isMute"></i>
                            <i class="fa fa-volume-down" v-show="!isMute && !isloud"></i>
                            <i class="fa fa-volume-up" v-show="!isMute && isloud"></i>
                        </button>
                        <div class="volume-container" @click="setVolume($event)" v-show="!isMute">
                            <div class="volume" ref="volume"></div>
                        </div>
                    </div>
                    <!-- 操作按钮组 -->
                    <div class="action">
                        <!-- 循环按钮 -->
                        <button>
                            <i class="fa fa-refresh" v-show="isListLoop"></i>
                        </button>
                        <!-- 列表按钮 -->
                        <button @click="setPlaylistView">
                            <i class="fa fa-list"></i>
                        </button>
                    </div>
                </div>
                <div class="progress-info">
                    <div class="progress-container" @click="setProgress($event)">
                        <div class="progress" ref="progress"></div>
                    </div>
                    <span v-if="songInfo.duration === ''">00:00/00:00</span>
                    <span v-else>{{ currentDuration }}/{{ songInfo.duration }}</span>
                </div>
            </div>
        </div>
        <audio :src="songInfo.url" ref="audio" :autoplay="isPlaying" @ended="nextSong"
            @timeupdate="updateProgress"></audio>
    </div>
</template>

<script>
import { getTime } from "../fun"
import { getMusicDetail, getMusicUrl } from "../http/api"
import { mapState, mapMutations } from "vuex"
import axios from "axios"

export default {
    name: 'MusicPlayer',
    data() {
        return {
            songInfo: {
                id: -1,
                name: '',
                ar: [],
                url: '',
                duration: '',
                alId: -1,
                alName: '',
                alPicUrl: '',
            },

            playlist: [],           //播放列表

            isPlaying: false,       //是否在播放
            isMute: false,          //是否静音
            isloud: false,          //音量是否大于60%
            isListLoop: true,          //是否开启列表循环

            playlistView: false,     //是否显示歌曲列表

            ////DOM元素
            audio: {},
            musicContainer: {},
            volume: {},
            progress: {},

            tempVolume: 0,          //临时音量
            currentDuration: '',    //音频播放位置
            testSongList: [],

            songIndex: 0,        //播放第几首

            record: null,       //听歌时长到一定时间后记录播放次数

            loading: false
        }
    },
    mounted() {
        this.progress = this.$refs.progress
        this.volume = this.$refs.volume
        this.audio = this.$refs.audio
        this.musicContainer = this.$refs.musicContainer

        const songInfo = JSON.parse(localStorage.getItem('songInfo'))
        const songIndex = JSON.parse(localStorage.getItem('songIndex'))
        const volume = localStorage.getItem('volume')
        this.isloud = volume >= 0.6 ? true : false
        this.volume.style.width = `${volume * 100}%`
        this.audio.volume = volume

        if (songInfo !== null) {
            this.songInfo = songInfo
            // if (this.musicPlayerId !== this.songInfo.id) this.setMusicPlayerId(this.songInfo.id)
        }
        if (songIndex !== null) {
            this.songIndex = songIndex
        } else {
            this.songIndex = 0
        }

        this.getPlaylistSongs()


    },
    computed: {
        ...mapState(['musicInfo', 'musicUrl', 'musicPlayerId', 'playlistId']),
    },
    watch: {
        musicInfo: function () {
            const info = JSON.parse(JSON.stringify(this.musicInfo))
            // console.log(info)
            this.songInfo.id = info.id
            this.songInfo.name = info.name
            this.songInfo.ar = info.ar
            this.songInfo.duration = getTime(info.dt)
            const album = info.al
            this.songInfo.alId = album.id
            this.songInfo.alName = album.name
            this.songInfo.alPicUrl = album.picUrl
            getMusicUrl(this.songInfo.id).then(res => {
                this.setMusicUrl(res.data.data[0].url)
            })
        },
        musicUrl: function () {
            const url = JSON.parse(JSON.stringify(this.musicUrl))
            // console.log(url)
            this.songInfo.url = url
            if (this.songInfo.url === '') return
            this.isPlaying = true
            this.record = setTimeout(this.recordMusic, 10000)
            let artists = []
            this.songInfo.ar.forEach(e => {
                artists.push(e.name)
            })
            this.$http.post('/listsongs/add', {
                listId: this.playlistId,
                musicId: this.songInfo.id,
                musicName: this.songInfo.name,
                singerName: artists,
                time: this.songInfo.duration
            }).then(res => {
                if (res.data.code === 200) {
                    this.getPlaylistSongs()
                }
            })

            localStorage.setItem('songInfo', JSON.stringify(this.songInfo))

        },
        musicPlayerId: {
            handler() {
                this.loading = true
                clearTimeout(this.record)
                axios.all([
                    getMusicDetail(this.musicPlayerId),
                    getMusicUrl(this.musicPlayerId)
                ]).then(axios.spread((info, url) => {
                    // console.log(info.data.songs[0])
                    this.setMusicInfo(info.data.songs[0])
                    // console.log(url.data.data[0])
                    this.setMusicUrl(url.data.data[0].url)
                    this.loading = false
                })).catch(err => {
                    console.error(err)
                    this.loading = false
                })
            },
        },
        songIndex: {
            handler() {
                localStorage.setItem('songIndex', this.songIndex)
            }
        }
    },
    methods: {
        ...mapMutations(['setMusicInfo', 'setMusicUrl', 'setMusicPlayerId', 'setLyricCurrent', 'setplaylistId']),

        getHash() {
            return location.hash.slice(1) || '/'
        },

        //获取currentDuration
        getCurrentDuration(currentDuration) {
            let ss = Math.floor(currentDuration % 60)
            ss = ss < 10 ? '0' + ss : ss
            let mm = Math.floor(currentDuration / 60)
            mm = mm < 10 ? '0' + mm : mm
            return mm + ':' + ss
        },

        //光盘图片为空时显示默认图片
        picNull(item) {
            item.picUrl = require('@/assets/DefaultCD.png')
        },

        //切换到上一首
        prevSong() {
            this.songIndex--
            if (this.songIndex < 0) {
                this.songIndex = this.playlist.length - 1
            }
            this.play(this.playlist[this.songIndex].musicId, this.songIndex)
        },

        //播放
        playSong() {
            clearTimeout(this.record)
            if (this.songInfo.url === '' || this.songInfo.url === null) return
            this.isPlaying = true
            this.audio.play()
            this.record = setTimeout(this.recordMusic, 10000)
        },

        //暂停
        pauseSong() {
            if (this.songInfo.url === '' || this.songInfo.url === null) return
            this.isPlaying = false
            this.audio.pause()
        },

        //切换到下一首
        nextSong() {
            this.songIndex++
            if (this.songIndex > this.playlist.length - 1) {
                this.songIndex = 0
            }
            this.play(this.playlist[this.songIndex].musicId, this.songIndex)
        },

        //播放或暂停
        playFun() {
            this.musicContainer.classList.contains('play') ? this.pauseSong() : this.playSong()
        },

        //更新进度条
        updateProgress(e) {
            //audio.duration: 音频长度
            //audio.currenTime: 音频播放位置
            const {
                duration,
                currentTime,
            } = e.target
            this.setLyricCurrent(parseFloat(currentTime).toFixed(2))
            this.currentDuration = this.getCurrentDuration(currentTime)
            const progressPercent = (currentTime / duration) * 100
            //进度条
            this.progress.style.width = `${progressPercent}%`
        },

        //设置进度条
        setProgress(e) {
            const width = e.currentTarget.clientWidth
            const clickX = e.offsetX
            this.setLyricCurrent(parseFloat(clickX).toFixed(2))
            const duration = this.audio.duration
            this.audio.currentTime = (clickX / width) * duration
        },

        //设置音量
        setVolume(e) {
            const width = e.currentTarget.clientWidth
            const clickX = e.offsetX
            // console.log(width, clickX)
            const volume = (clickX / width)
            this.isloud = volume >= 0.6 ? true : false
            localStorage.setItem('volume', volume)
            this.volume.style.width = `${volume * 100}%`
            this.audio.volume = volume
        },

        //控制音量开启或静音
        setMute() {
            this.isMute = !this.isMute
            if (this.isMute) {
                this.tempVolume = this.audio.volume
                this.audio.volume = 0
            } else {
                this.audio.volume = this.tempVolume
            }
        },

        //前往音乐详情页面
        goSongDetails() {
            if (this.songInfo.url === '') return
            const hash = this.getHash()
            if (this.songInfo.url !== '' && hash !== '/Songdetails') this.$router.push('/Songdetails')
            else this.$router.go(-1)
        },

        //开启/关闭单曲循环
        setListLoop() {
            this.isListLoop = !this.isListLoop
        },

        //打开/关闭歌曲列表
        setPlaylistView() {
            this.playlistView = !this.playlistView
        },

        //记录听歌次数
        recordMusic() {
            const userInfo = JSON.parse(localStorage.getItem('userInfo'))
            if (userInfo === null) {
                this.$http.post('/music/save', {
                    musicId: this.songInfo.id,
                    userId: null
                })
            } else {
                this.$http.post('music/save', {
                    musicId: this.songInfo.id,
                    userId: userInfo.id

                })
            }
        },

        //获取列表歌曲
        async getPlaylistSongs() {
            const playlistId = JSON.parse(localStorage.getItem('playlistId'))
            if (playlistId !== null) {
                this.setplaylistId(playlistId)
                const { data: res } = await this.$http.get('/listsongs/getsongs', {
                    params: {
                        id: this.playlistId
                    }
                })
                this.playlist = res.data
            } else {
                const { data: res } = await this.$http.post('/playlist/create')
                this.setplaylistId(res.data.id)
                localStorage.setItem('playlistId', this.playlistId)
            }
        },

        //移除歌曲列表中的歌曲
        deletePlaylistSong(id) {
            this.$http.post('/listsongs/delete', {
                listId: this.playlistId,
                musicId: id
            }).then(res => {
                if (res.data.code === 200) {
                    this.getPlaylistSongs()
                }
            })
        },

        //播放歌曲列表中的歌曲
        play(id, index) {
            this.setMusicPlayerId(id)
            this.songIndex = index
        }
    },
}
</script>

<style lang="less">
.textHidden {
    overflow: hidden;
    white-space: nowrap;
    text-overflow: ellipsis;
}

.fa {
    width: 20px;
    height: 20px;
}

.music-container {
    display: flex;
    position: fixed;
    bottom: 0;
    justify-content: center;
    width: 100%;
    height: 100px;
    background-color: #00A9FF;
    z-index: 999;

    .music-info {
        display: flex;
        position: relative;
        justify-content: space-between;
        padding: 0 10px;
        width: 50%;
        height: 100%;

        .playlist {
            position: absolute;
            bottom: 100%;
            right: 0;
            width: 480px;
            height: 410px;
            background-color: #0dc9fc;
            z-index: 99;

            .title {
                display: flex;
                padding: 10px;
                background-color: #04b6ff;
                justify-content: space-between;
                align-items: center;
                font-size: 20px;
                color: #003993;

                .close {
                    cursor: pointer;
                }
            }

            .musicList {
                padding: 0 10px;
                color: #414141;

                li {
                    height: 30px;
                    border-bottom: 1px solid #0bb0dd;
                    cursor: pointer;

                    &:hover {
                        background-color: #73c9ff;

                        .musicList-item {
                            .musicList-name {
                                .delete {
                                    display: block;
                                }
                            }
                        }
                    }

                    .musicList-item {
                        display: flex;
                        height: 100%;
                        line-height: 30px;

                        .musicList-number {
                            width: 30px;
                        }

                        .musicList-name {
                            display: flex;
                            justify-content: space-between;
                            width: 254px;
                            color: white;

                            .delete {
                                display: none;
                                width: 30px;
                                height: 30px;
                                text-align: center;

                                &:hover {
                                    color: gray;
                                }
                            }
                        }

                        .musicList-artist {
                            width: 90px;
                            font-size: 14px;
                        }

                        .musicList-time {
                            width: 60px;
                            font-size: 16px;
                        }
                    }
                }
            }
        }

        .music-img {
            display: flex;
            justify-items: center;
            width: 100px;
            height: 100%;
            cursor: pointer;

            img {
                height: 100%;
                object-fit: cover;
                border-radius: 50%;
                animation: imgRotate 5s linear infinite;
                animation-play-state: paused;
            }
        }

        .music-control {
            display: flex;
            flex-direction: column;
            justify-content: space-around;
            width: 80%;
            height: 100%;

            .music-name {
                display: flex;
                height: 28px;
                color: white;

                cursor: pointer;

                .name {
                    overflow: hidden;
                    white-space: nowrap;
                    text-overflow: ellipsis;
                    width: 16vw;
                    font-size: 1.2vw;
                    margin-right: 3vw;
                }

                .singers {
                    width: 19vw;
                    overflow: hidden;
                    white-space: nowrap;
                    text-overflow: ellipsis;
                    color: black;

                    .singer {
                        font-size: 1vw;
                        color: black;
                    }
                }
            }

            .btns {
                display: flex;
                justify-content: space-between;

                button {
                    // width: 30px;
                    // height: 30px;
                    border: 0;
                    background-color: rgba(0, 0, 0, 0);
                    padding: 0 0.5vw;
                    margin: 0 1vw;
                    font-size: 20px;
                    color: #0decfc;
                    cursor: pointer;
                }

                .nav {
                    button:nth-child(1) {
                        margin-left: 0;
                    }
                }

                .volume-info {
                    display: flex;
                    width: 20.4%;
                    align-items: center;

                    button {
                        margin: 0;
                    }

                    .volume-container {
                        background-color: gray;
                        border-radius: 5px;
                        margin: 10px 0;
                        width: 100%;
                        height: 6px;
                        cursor: pointer;

                        .volume {
                            background-color: #0decfc;
                            border-radius: 5px;
                            height: 100%;
                            /* 默认音量为50% */
                            width: 50%;
                            transition: width 0.1s linear;
                        }
                    }
                }
            }

            .progress-info {
                display: flex;
                width: 100%;

                .progress-container {
                    background-color: gray;
                    border-radius: 5px;
                    margin: 10px 0;
                    width: 88%;
                    height: 6px;
                    cursor: pointer;

                    .progress {
                        background-color: #0decfc;
                        border-radius: 5px;
                        height: 100%;
                        /* 一开始进度条长度为0 */
                        width: 0%;
                        transition: width 0.1s linear;
                    }
                }
            }
        }
    }
}

.music-container.play .music-img img {
    animation-play-state: running;
}

@keyframes imgRotate {
    from {
        transform: rotate(0);
    }

    to {
        transform: rotate(360deg);
    }
}
</style>