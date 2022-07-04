<template>
    <div>
        <el-table :data="searchSongs" height="620" style="width: 100%" @cell-click="play">
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
    </div>
</template>

<script>
import axios from "axios"
import { mapState, mapMutations } from "vuex"
import { toRaw } from '@vue/reactivity'

export default {
    name: 'Search',
    data() {
        return {
            songInfo: {}
        }
    },
    computed: {
        ...mapState(['searchSongs'])
    },
    created() {

    },
    methods: {
        ...mapMutations(['setMusicInfo', 'setMusicUrl']),

        async play(row) {
            const song = toRaw(row)

            const { data: res } = await this.$http.get('/song/musiccheck', {
                params: {
                    id: song.id
                }
            })
            if (res.code !== 200) {
                return ElMessage.warning('抱歉, 该歌暂无版权')
            }
            axios.all([
                this.$http.get('song/detail', {
                    params: {
                        ids: song.id
                    }
                }),
                this.$http.get('song/url', {
                    params: {
                        id: song.id
                    }
                })
            ]).then(axios.spread((info, url) => {
                // console.log(info.data.songs[0])
                this.setMusicInfo(info.data.songs[0])
                // console.log(url.data.data[0])
                this.setMusicUrl(url.data.data[0].url)
            }))

            // axios.all([
            //     this.$http.get('song/detail', {
            //         params: {
            //             ids: song.id
            //         }
            //     }),
            //     this.$http.get('song/url', {
            //         params: {
            //             id: song.id
            //         }
            //     })
            // ]).then(axios.spread((info, url) => {
            //     // console.log(info.data.songs[0])
            //     this.setMusicInfo(info.data.songs[0])
            //     // console.log(url.data.data[0])
            //     this.setMusicUrl(url.data.data[0].url)
            // }))
        }
    },
}
</script>

<style lang="less">
.el-table tr {
    cursor: pointer;
}
</style>