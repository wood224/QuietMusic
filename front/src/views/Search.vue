<template>
    <div class="search-container">
        <el-tabs type="border-card" @tab-click="handleClick" v-model="activeName">
            <el-tab-pane label="单曲" name="searchSingle">
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
            <el-tab-pane label="歌手" name="searchSinger" v-loading="loading"
                element-loading-background="rgba(122, 122, 122, 0.4)" element-loading-text="加载中...(如果长时间未响应, 请刷新页面后重试)">
                <ul class="singerList">
                    <li v-for="item in singerLsit" :key="item.id">
                        <div class="singer">
                            <div class="pic">
                                <img :src="item.picUrl" alt="">
                            </div>
                            <div class="singerInfo">
                                <span>{{ item.name }}</span>
                            </div>
                        </div>
                    </li>
                </ul>

            </el-tab-pane>
            <el-tab-pane label="歌单" name="searchList">
            </el-tab-pane>
            <el-tab-pane label="专辑" name="searchAlbum">
            </el-tab-pane>
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
            activeName: 'searchSingle',
            songList: [],
            singerLsit: [],
            type: 1
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
                if (this.type === 1) return this.getSearchList(this.keywords)
                if (this.type === 100) return this.searchSinger()
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
        },

        //搜索单曲
        async searchSingle() {
            this.loading = true
            const { data: res } = await this.$http.get('/song/search', {
                params: {
                    keywords: this.keywords,
                    type: 1
                }
            })
            if (res.code === 200) {
                this.songLsit = res.result.songs
            }
            this.loading = false
        },

        //搜索歌手
        async searchSinger() {
            this.loading = true
            const { data: res } = await this.$http.get('/song/search', {
                params: {
                    keywords: this.keywords,
                    type: 100
                }
            })
            if (res.code === 200) {
                this.singerLsit = res.result.artists
            }
            this.loading = false
        },

        //搜索歌单
        searchList() { },

        //搜索专辑
        searchAlbum() { },

        handleClick(tab, event) {
            // console.log(tab.props.name)
            if (tab.props.name === 'searchSingle') {
                this.type = 1
                return this.getSearchList(this.keywords)
            }
            if (tab.props.name === 'searchSinger') {
                this.type = 100
                return this.searchSinger()
            }
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

    .el-tabs {
        .el-tabs__content {
            .singerList {
                display: flex;
                flex-wrap: wrap;
                justify-content: space-between;
                height: 540px;
                overflow: auto;

                .singer {
                    width: 140px;
                    cursor: pointer;

                    .pic {
                        height: 140px;

                        img {
                            width: 100%;
                            height: 100%;
                            object-fit: cover;
                        }

                        // background-color: hotpink;
                    }

                    .singerInfo {
                        height: 30px;
                        // background-color: skyblue;
                    }
                }
            }
        }
    }
}
</style>