<template>
    <div class="song-details-container" ref="SongDetailsContainer">
        <div class="content">
            <div class="lyric" ref="lyric">
                <ul>
                    <li v-for="item in musicLyric" :key="item.key">
                        {{ item }}
                    </li>
                </ul>
            </div>
        </div>
        <div class="blurBgMask">这里是遮罩层</div>
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
            SongDetailsContainer: {},
            blurBg: {},
            bgi: '',
            musicLyric: {}
        }
    },
    computed: {
        ...mapState(['musicInfo']),
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
        this.SongDetailsContainer = this.$refs.SongDetailsContainer
        this.blurBg = this.$refs.blurBg
        const songInfo = JSON.parse(localStorage.getItem('songInfo'))
        if (songInfo !== null)
            if (songInfo.picUrl !== '' && songInfo.id !== -1)
                this.bgi = songInfo.picUrl
    },
    watch: {
        bgi: {
            handler() {
                this.blurBg.style.backgroundImage = `url("${this.bgi}")`
            },
        },
        musicLyric: {
            handler() {

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
* {
    overflow: hidden;
}

.song-details-container {
    position: relative;
    height: 630px;
    overflow: hidden;

    .content {
        position: absolute;
        width: 840px;
        height: 100%;
        left: 50%;
        margin-left: -420px;
        z-index: 100;
    }

    .blurBgMask {
        height: 100%;
        background-color: rgba(0, 0, 0, 0.35);
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