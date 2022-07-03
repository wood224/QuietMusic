<template>
    <div>
        <el-table :data="searchSongs" height="600" style="width: 100%" @cell-click="play">
            <el-table-column prop="name" label="歌曲名" width="300" />
            <el-table-column label="歌手" width="300">
                <template v-slot:default="scope">
                    <span v-for="item in scope.row.artists" :key="item.id">
                        {{ item.name }}&nbsp;
                    </span>
                </template>
            </el-table-column>
            <el-table-column prop="album.name" label="专辑" />
            <el-table-column prop="duration" label="时长" />
        </el-table>
        <audio :src="songUrl" autoplay></audio>
    </div>
</template>

<script>
import { mapState, mapMutations } from "vuex"
import { toRaw } from '@vue/reactivity'

export default {
    name: 'Search',
    data() {
        return {
            songUrl: ''
        }
    },
    computed: {
        ...mapState(['searchSongs'])
    },
    created() {

    },
    methods: {
        ...mapMutations(['setMusicInfo']),

        async play(row) {
            const data = toRaw(row)
            const songId = data.id
            const { data: res1 } = await this.$http.get('/song/url', {
                params: {
                    id: songId
                }
            })
            this.songUrl = res1.data[0].url
            const { data: res2 } = await this.$http.get('/song/detail', {
                params: {
                    ids: songId
                }
            })
            this.setMusicInfo(res2.songs[0])

        },
    },
}
</script>

<style lang="less">
.el-table tr {
    cursor: pointer;
}
</style>