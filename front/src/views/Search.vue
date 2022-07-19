<template>
    <div class="search-container">
        <el-input v-model="ipt" size="large" placeholder="搜索歌曲, 歌手...(按下ESC可快速清空)" :prefix-icon="Search"
            @keyup.enter="search" @keyup.esc="ipt = ''" />
        <el-tabs type="border-card" @tab-click="handleClick" v-model="activeName">
            <el-tab-pane label="单曲" name="searchSingle">
                <el-table :data="searchSongs" height="480" style="width: 100%" @cell-click="play">
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
            <el-tab-pane label="歌手" name="searchSinger">
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
import { Search } from '@element-plus/icons-vue'
import axios from "axios"
import { getSearch } from "../http/api"
import { mapState, mapMutations, mapGetters } from "vuex"
import { toRaw } from '@vue/reactivity'

export default {
    name: 'Search',
    data() {
        return {
            ipt: '',
            keywords: '',
            activeName: 'searchSingle',
            songList: [],
            singerLsit: [],
            type: 1,
        }
    },
    computed: {
        ...mapState(['searchSongs', 'searchKeywords']),
        ...mapGetters(['getBaseURLCloudMusic'])
    },
    watch: {
        searchKeywords: {
            handler() {
                this.keywords = this.searchKeywords
            }
        },
        keywords: {
            handler() {
                this.ipt = this.searchKeywords
                if (this.type === 1) return this.getSearchList(this.keywords)
                if (this.type === 100) return this.searchSinger()
            }
        },
    },
    created() {
        if (this.searchKeywords !== '') {
            if (sessionStorage.getItem('keywords') !== null && this.searchKeywords === sessionStorage.getItem('keywords')) return this.ipt = this.searchKeywords
            this.keywords = this.searchKeywords
            sessionStorage.setItem('keywords', this.searchKeywords)
        }

    },
    mounted() {
    },
    methods: {
        ...mapMutations(['setMusicInfo', 'setMusicUrl', 'setSearchSongs', 'setMusicPlayerId', 'setSearchKeywords']),

        //组件搜索框的搜索
        async search() {
            if (this.ipt === '') return
            this.setSearchKeywords(this.ipt)
            this.ipt = ''
            // this.$router.push({ path: '/search', query: { keywords: this.ipt } })
        },

        //时间格式化
        getTime(duration) {
            let ss = Math.floor(duration / 1000 % 60)
            ss = ss < 10 ? '0' + ss : ss
            let mm = Math.floor(duration / 1000 / 60)
            mm = mm < 10 ? '0' + mm : mm
            return mm + ':' + ss
        },

        //获取搜索列表
        getSearchList(keywords) {
            getSearch(keywords, 1)
                .then(res => {
                    if (!res.data.result.songs) return this.setSearchSongs(null)
                    this.setSearchSongs(res.data.result.songs)
                    this.searchSongs.forEach(item => {
                        item.duration = this.getTime(item.duration)
                    })
                }).catch(err => {
                    console.error(err)
                })
        },

        //播放歌曲
        play(row) {
            const song = toRaw(row)
            axios.get(this.getBaseURLCloudMusic + '/check/music', {
                params: {
                    id: song.id
                }
            }).then(res => {
                if (res.data.success !== true) {
                    return ElMessage.warning('抱歉, 该歌暂无版权')
                }
                this.setMusicPlayerId(song.id)
            })
        },

        //搜索单曲
        searchSingle() {
            getSearch(this.keywords, 1)
                .then(res => {
                    console.log(res.data)
                    this.setSearchSongs(res.data.result.songs)
                    this.searchSongs.forEach(item => {
                        item.duration = this.getTime(item.duration)
                    })
                }).catch(err => {
                    console.error(err)
                })
        },

        //搜索歌手
        searchSinger() {
            getSearch(this.keywords, 100)
                .then(res => {
                    this.singerLsit = res.data.result.artists
                }).catch(err => {
                    console.error(err)
                })
        },

        //搜索歌单
        searchList() { },

        //搜索专辑
        searchAlbum() { },

        //搜索模式切换
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
    setup() {
        return {
            Search,
        }
    }
}
</script>

<style lang="less">
.search-container {
    width: 1000px;
    margin: 0 auto;

    .el-input {
        margin: 10px 0;
    }

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