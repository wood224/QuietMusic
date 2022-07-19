<template>
    <div class="song-details-container" ref="SongDetailsContainer">
        <div class="content"></div>
        <div class="blurBgMask">这里是遮罩层</div>
        <div class="blurBg" ref="blurBg"></div>
    </div>
</template>

<script>
import { mapState } from "vuex"

export default {
    name: 'SongDetails',
    data() {
        return {
            SongDetailsContainer: {},
            blurBg: {},
            bgi: ''
        }
    },
    computed: {
        ...mapState(['musicInfo']),
    },
    mounted() {
        this.SongDetailsContainer = this.$refs.SongDetailsContainer
        this.blurBg = this.$refs.blurBg
        // const info = JSON.parse(JSON.stringify(this.musicInfo))
        // if (info.al.picUrl !== '' && info.id !== -1) this.bgi = info.al.picUrl
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