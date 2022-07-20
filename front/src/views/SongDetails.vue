<template>
    <div class="song-details-container" ref="SongDetailsContainer">
        <div class="content">
            <div class="left"></div>
            <div class="right">
                <div class="title"></div>
                <div class="detail"></div>
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
                Object.keys(this.musicLyric).forEach((key, index) => {
                    if (this.lyricCurrent == key) {
                        this.currentRow = index
                        if (index >= 5) {
                            this.lyricView.style.top = -30 * (index - 5) + 'px'
                        }
                    }
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
        }
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
        margin-left: -420px;
        z-index: 100;

        .left {
            width: 40%;
            height: 100%;
        }

        .right {
            width: 60%;

            .lyric {
                position: relative;
                height: 100%;

                ul {
                    position: absolute;

                    li {
                        height: 25px;
                        margin: 5px 0;
                    }

                    .currentRow {
                        font-size: 20px;
                        color: #08C7DA;
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
        opacity: .6;
        z-index: 9;
    }
}
</style>