<template>
    <div class="song-details-container" ref="SongDetailsContainer">
        <div class="content">
            <div class="left">
                <div class="pic">
                    <img :src="songInfo.alPicUrl" alt="" @error="picNull(songInfo)">
                </div>
            </div>
            <div class="right">
                <div class="title">
                    <h3>{{ songInfo.name }}</h3>
                </div>
                <div class="detail">
                    <div class="singer">
                        <span class="fontColor">歌手:</span>
                        <span v-for="(item, index) in songInfo.ar" :key="item.id">
                            <span v-if="index !== 0">
                                /
                            </span>
                            {{ item.name }}
                        </span>
                    </div>
                    <div class="album">
                        <span class="fontColor">专辑:</span>
                        <span>
                            {{ songInfo.alName }}
                        </span>
                    </div>
                </div>
                <div class="lyric">
                    <ul ref="lyricView">
                        <li v-for="(item, key, index) in musicLyric" :key="key"
                            :class="{ currentRow: index == currentRow }">
                            {{ item }}
                        </li>
                    </ul>
                </div>
            </div>
        </div>
        <div class="blurBgMask"></div>
        <div class="blurBg" ref="blurBg"></div>
    </div>
</template>

<script>
import { getLyricApi } from "../http/api"
import { getTime } from "../fun"
import { mapState } from "vuex"

export default {
    name: 'SongDetails',
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

            //dom对象
            SongDetailsContainer: {},
            lyricView: {},

            blurBg: {},     //模糊背景
            bgi: '',        //背景图片路径
            musicLyric: {},  //音乐歌词

            currentRow: -1    //当前歌词播放行数
        }
    },
    computed: {
        ...mapState(['musicInfo', 'lyricCurrent']),
    },
    created() {
        const info = JSON.parse(localStorage.getItem('songInfo'))
        // console.log(info)
        this.songInfo.id = info.id
        this.songInfo.name = info.name
        this.songInfo.ar = info.ar
        this.songInfo.duration = getTime(info.dt)
        this.songInfo.alId = info.alId
        this.songInfo.alName = info.alName
        this.songInfo.alPicUrl = info.alPicUrl

        this.getLyric()
    },
    mounted() {
        this.lyricView = this.$refs.lyricView
        this.SongDetailsContainer = this.$refs.SongDetailsContainer
        this.blurBg = this.$refs.blurBg
        const songInfo = JSON.parse(localStorage.getItem('songInfo'))
        if (songInfo !== null)
            if (songInfo.picUrl !== '' && songInfo.id !== -1)
                this.bgi = songInfo.alPicUrl
    },
    watch: {
        bgi: {
            handler() {
                this.blurBg.style.backgroundImage = `url("${this.bgi}")`
            },
        },
        lyricCurrent: {
            handler() {
                let lastKey = -1
                Object.keys(this.musicLyric).forEach((key, index) => {
                    if (this.lyricCurrent >= lastKey && this.lyricCurrent < key) {
                        this.currentRow = index - 1
                        if (index >= 5) {
                            this.lyricView.style.top = -30 * (index - 5) + 'px'
                        }
                    }
                    lastKey = key
                })
            }
        }
    },
    methods: {
        //获取歌词
        getLyric() {
            if (this.songInfo.id === -1) return
            getLyricApi(this.songInfo.id)
                .then(res => {
                    let lyricArr = res.data.lrc.lyric.split('[').slice(1)
                    let lrcObj = {}
                    lyricArr.forEach(item => {
                        let arr = item.split(']')
                        let m = parseInt(arr[0].split(':')[0])
                        let s = parseInt(arr[0].split(':')[1])
                        arr[0] = m * 60 + s
                        if (arr[1] !== '\n') {        //去除换行符
                            lrcObj[arr[0]] = arr[1]
                        }
                    })
                    this.musicLyric = lrcObj
                })
        },

        //光盘图片为空时显示默认图片
        picNull(item) {
            item.picUrl = require('@/assets/DefaultCD.png')
        },
    },
}
</script>

<style lang="less" scoped>
.song-details-container {
    position: relative;
    height: 630px;
    overflow: hidden;

    .content {
        display: flex;
        position: absolute;
        width: 840px;
        height: 100%;
        left: 50%;
        margin-top: 2vw;
        margin-left: -420px;
        z-index: 100;
        color: white;

        .left {
            width: 40%;
            height: 100%;

            .pic {
                width: 300px;
                padding: 0 40px 40px;

                img {
                    width: 100%;
                }
            }
        }

        .right {
            width: 60%;

            .title {
                margin-top: 1vw;
            }

            .detail {
                display: flex;
                justify-content: space-between;
                margin-top: 20px;
                margin-bottom: 20px;

                .fontColor {
                    color: #ffffff94;
                }
            }

            .lyric {
                position: relative;
                height: 100%;
                overflow: hidden;

                ul {
                    position: absolute;
                    transition: 0.3s;

                    li {
                        height: 25px;
                        margin: 5px 0;
                    }

                    .currentRow {
                        font-size: 20px;
                        color: skyblue;
                    }
                }

            }
        }
    }

    .blurBgMask {
        height: 100%;
        background-color: rgba(0, 0, 0, 0.7);
        z-index: 10;
    }

    .blurBg {
        position: absolute;
        top: 0;
        left: 0;
        background-repeat: no-repeat;
        background-size: cover;
        width: 100%;
        height: 100%;
        filter: blur(20px);
        opacity: 0.6;
        z-index: 9;
    }
}
</style>