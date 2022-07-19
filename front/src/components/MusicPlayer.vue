<template>
    <div class="music-container" :class="{ 'play': isPlaying }" ref="musicContainer" v-loading="loading"
        element-loading-background="rgba(122, 122, 122, 0.8)" element-loading-text="加载中...(如果长时间未响应, 请刷新页面后重试)">
        <div class="music-info">
            <div class="music-img" @click="goSongDetails">
                <img :src="songInfo.picUrl" alt="" @error="picNull(songInfo)">
            </div>
            <div class="music-control">
                <div class="music-name" @click="goSongDetails">
                    <span class="name">{{ songInfo.name }}</span>
                    <span class="singer" v-for="(item, index) in songInfo.ar" :key="index">
                        <span v-if="index !== 0">/</span>{{ item.name }}
                    </span>
                </div>
                <div class="btns">
                    <div class="nav">
                        <button id="prev" @click="prevSong">
                            <i class="fas fa-backward"></i>
                        </button>
                        <button id="play" @click="playFun">
                            <i class="fas" :class="[isPlaying ? 'fa-pause' : 'fa-play']"></i>
                        </button>
                        <button id="next" @click="nextSong">
                            <i class="fas fa-forward"></i>
                        </button>
                    </div>
                    <div class="volume-info">
                        <button @click="setMute">
                            <i class="fas fa-volume-mute" v-show="isMute"></i>
                            <i class="fas fa-volume-down" v-show="!isMute && !isloud"></i>
                            <i class="fas fa-volume-up" v-show="!isMute && isloud"></i>
                        </button>
                        <div class="volume-container" @click="setVolume($event)" v-show="!isMute">
                            <div class="volume" ref="volume"></div>
                        </div>
                    </div>
                    <div class="action">
                        <button>
                            <i class="fas fa-sliders-h"></i>
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
        <audio :src="songInfo.url" autoplay ref="audio" @ended="pauseSong" @timeupdate="updateProgress"></audio>
    </div>
</template>

<script>
import { mapState, mapMutations, mapGetters } from "vuex"
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
                picUrl: '',
                duration: ''
            },
            isPlaying: false,       //是否在播放
            isMute: false,          //是否静音
            isloud: false,          //音量是否大于60%

            ////DOM元素
            audio: {},
            musicContainer: {},
            volume: {},
            progress: {},

            tempVolume: 0,          //临时音量
            currentDuration: '',    //音频播放位置
            testSongList: [],

            loading: false
        }
    },
    mounted() {
        this.progress = this.$refs.progress
        this.volume = this.$refs.volume
        this.audio = this.$refs.audio
        this.musicContainer = this.$refs.musicContainer
        this.audio.volume = 0.5
        const songInfo = JSON.parse(localStorage.getItem('songInfo'))
        if (songInfo !== null) {
            this.songInfo = songInfo
            // if (this.musicPlayerId !== this.songInfo.id) this.setMusicPlayerId(this.songInfo.id)
            if (this.songInfo.url !== '') this.isPlaying = true
        }
    },
    computed: {
        ...mapState(['musicInfo', 'musicUrl', 'musicPlayerId']),
        ...mapGetters(['getBaseURLCloudMusic'])
    },
    watch: {
        musicInfo: function () {
            const info = JSON.parse(JSON.stringify(this.musicInfo))
            // console.log(info)
            this.songInfo.id = info.id
            this.songInfo.name = info.name
            this.songInfo.ar = info.ar
            this.songInfo.duration = this.getTime(info.dt)
            this.songInfo.picUrl = info.al.picUrl
        },
        musicUrl: function () {
            const url = JSON.parse(JSON.stringify(this.musicUrl))
            // console.log(url)
            this.songInfo.url = url
            if (this.songInfo.url !== '') {
                this.isPlaying = true
            }
            localStorage.setItem('songInfo', JSON.stringify(this.songInfo))

        },
        musicPlayerId: {
            handler() {
                this.loading = true
                axios.all([
                    axios.get(this.getBaseURLCloudMusic + '/song/detail', {
                        params: {
                            ids: this.musicPlayerId
                        }
                    }),
                    axios.get(this.getBaseURLCloudMusic + '/song/url', {
                        params: {
                            id: this.musicPlayerId
                        }
                    })
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
        }
    },
    methods: {
        ...mapMutations(['setMusicInfo', 'setMusicUrl', 'setMusicPlayerId']),

        getHash() {
            return location.hash.slice(1) || '/'
        },

        //毫秒转为时分格式
        getTime(duration) {
            let ss = Math.floor(duration / 1000 % 60)
            ss = ss < 10 ? '0' + ss : ss
            let mm = Math.floor(duration / 1000 / 60)
            mm = mm < 10 ? '0' + mm : mm
            return mm + ':' + ss
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

        },

        //播放
        playSong() {
            if (this.songInfo.url === '' || this.songInfo.url === null) return
            this.isPlaying = true
            this.audio.play()
        },

        //暂停
        pauseSong() {
            if (this.songInfo.url === '' || this.songInfo.url === null) return
            this.isPlaying = false
            this.audio.pause()
        },

        //切换到下一首
        nextSong() {

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
            this.currentDuration = this.getCurrentDuration(currentTime)
            const progressPercent = (currentTime / duration) * 100
            //进度条
            this.progress.style.width = `${progressPercent}%`
        },

        //设置进度条
        setProgress(e) {
            const width = e.currentTarget.clientWidth
            const clickX = e.offsetX
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
            const hash = this.getHash()
            if (this.songInfo.url !== '' && hash !== '/Songdetails') this.$router.push('/Songdetails')
            else this.$router.go(-1)

        }
    },
}
</script>

<style lang="less">
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
        justify-content: space-between;
        padding: 0 10px;
        width: 50%;
        height: 100%;

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
                    font-size: 20px;
                    margin-right: 150px;
                }

                .singer {
                    color: black;
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
                    padding: 0 10px;
                    margin: 0 20px;
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