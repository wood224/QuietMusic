<template>
    <div class="search-container">
        <div>
            <el-tabs type="border-card">
                <el-tab-pane label="单曲">
                    <div>
                        <el-table :data="searchSongs" height="540" style="width: 100%" @cell-click="play">
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
                </el-tab-pane>
                <el-tab-pane label="歌手"></el-tab-pane>
                <el-tab-pane label="歌单"></el-tab-pane>
                <el-tab-pane label="专辑"></el-tab-pane>
            </el-tabs>
        </div>

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

        }
    },
    computed: {
        ...mapState(['searchSongs'])
    },
    created() {

    },
    mounted() {
        this.songInfo = JSON.parse(localStorage.getItem('songInfo'))
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
                localStorage.setItem('songInfo', JSON.stringify(this.songInfo))
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
.search-container {
    width: 1000px;
    margin: 0 auto;

    .el-table tr {
        cursor: pointer;
    }
}
</style>