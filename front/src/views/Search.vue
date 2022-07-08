<template>
    <div class="search-container">
        <el-tabs type="border-card">
            <el-tab-pane label="单曲">
                <el-table :data="searchSongs" height="540" style="width: 100%" @cell-click="play" v-loading="loading"
                    element-loading-background="rgba(122, 122, 122, 0.2)"
                    element-loading-text="加载中...(如果长时间未响应, 请刷新页面后重试)">
                    <el-table-column prop="name" label="歌曲名" width="300" />
                    <el-table-column label="歌手" width="300">
                        <template v-slot:default="scope">
                            <span v-for="item in scope.row.artists" :key="item.name">
                                {{ item.name }}&nbsp;
                            </span>
                        </template>
                    </el-table-column>
                    <el-table-column prop="album.name" label="专辑" />
                    <el-table-column prop="duration" label="时长" />
                </el-table>
            </el-tab-pane>
            <el-tab-pane label="歌手"></el-tab-pane>
            <el-tab-pane label="歌单"></el-tab-pane>
            <el-tab-pane label="专辑"></el-tab-pane>
        </el-tabs>
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
            loading: false,
            keywords: '',
            songList: []
        }
    },
    computed: {
        ...mapState(['searchSongs', 'searchKeywords'])
    },
    watch: {
        searchKeywords: {
            handler() {
                this.keywords = this.searchKeywords
            }
        },
        keywords: {
            handler() {
                this.getSearchList(this.keywords)
            }
        }
    },
    created() {
        if (this.searchKeywords !== '') {
            if (sessionStorage.getItem('keywords') !== null && this.searchKeywords === sessionStorage.getItem('keywords')) return
            this.keywords = this.searchKeywords
            sessionStorage.setItem('keywords', this.searchKeywords)
        }
    },
    mounted() {

    },
    methods: {
        ...mapMutations(['setMusicInfo', 'setMusicUrl', 'setSearchSongs', 'setMusicPlayerId', 'setSearchKeywords']),

        //时间格式化
        getTime(duration) {
            let ss = Math.floor(duration / 1000 % 60)
            ss = ss < 10 ? '0' + ss : ss
            let mm = Math.floor(duration / 1000 / 60)
            mm = mm < 10 ? '0' + mm : mm
            return mm + ':' + ss
        },

        //获取搜索列表
        async getSearchList(keywords) {
            this.loading = true
            const { data: res } = await this.$http.get('/song/search', {
                params: {
                    keywords: keywords
                }
            })
            this.setSearchSongs(res.result.songs)
            this.searchSongs.forEach(item => {
                item.duration = this.getTime(item.duration)
            })
            this.loading = false
        },

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
            this.setMusicPlayerId(song.id)
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