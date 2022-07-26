<template>
    <div class="search-container">
        <!-- <el-input v-model="ipt" size="large" placeholder="搜索歌曲, 歌手...(按下ESC可快速清空)" clearable @keyup.enter="search"
            @keyup.esc="ipt = ''">
            <template #prefix>
                <el-icon class="el-input__icon">
                    <Search />
                </el-icon>
            </template>
        </el-input> -->
        <div class="mt-4 search-box">
            <el-input v-model="ipt" placeholder="搜索歌曲, 歌手...(按下ESC可快速清空)" clearable class="input-with-select"
                @keyup.enter="search" @keyup.esc="ipt = ''">
                <template #prepend>
                    <el-button :icon="Search" @click="search" />
                </template>
            </el-input>
        </div>
        <el-tabs type="border-card" @tab-click="tableHandlerClick" v-model="activeName">
            <el-tab-pane label="单曲" name="searchSingle">
                <el-table :data="searchSongs" height="480" style="width: 100%" @cell-click="play" stripe>
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
                <el-table :data="playList" height="480" style="width: 100%" stripe>
                    <el-table-column prop="name" label="名字" width="300" />
                </el-table>
            </el-tab-pane>
            <el-tab-pane label="专辑" name="searchAlbum">
            </el-tab-pane>
        </el-tabs>
    </div>
</template>

<script>
import { Search } from '@element-plus/icons-vue'
import { getTime } from "../fun"
import { getSearchApi, getCheckMusic } from "../http/api"
import { mapState, mapMutations } from "vuex"
import { toRaw } from '@vue/reactivity'

export default {
    name: 'Search',
    data() {
        return {
            ipt: '',
            keywords: '',
            activeName: 'searchSingle',
            singerLsit: [],     //歌手列表
            playList: [],       //歌单列表

            //搜索模式
            type: 1,
        }
    },
    computed: {
        ...mapState(['searchSongs', 'searchKeywords']),
    },
    watch: {
        searchKeywords: {
            handler() {
                this.keywords = this.searchKeywords
            }
        },
        keywords: {
            handler() {
                sessionStorage.setItem('keywords', this.searchKeywords)
                this.ipt = this.searchKeywords
                if (this.type === 1)
                    return this.searchSingle()
                if (this.type === 100)
                    return this.searchSinger()
                if (this.type === 1000)
                    return this.activeName = 'searchList'
                if (this.type === 10)
                    return this.activeName = 'searchAlbum'
            }
        },
        type: {
            handler() {
                if (this.type === 1) {
                    this.typeWords = '单曲'
                    this.activeName = 'searchSingle'
                }
                else if (this.type === 100) {
                    this.typeWords = '歌手'
                    this.activeName = 'searchSinger'
                }
                else if (this.type === 1000) {
                    this.typeWords = '歌单'
                    this.activeName = 'searchList'
                }
                else if (this.type === 10) {
                    this.typeWords = '专辑'
                    this.activeName = 'searchAlbum'
                }
            }
        }
    },
    created() {
        if (this.searchKeywords !== '') {
            if (sessionStorage.getItem('keywords') !== null && this.searchKeywords === sessionStorage.getItem('keywords')) {
                this.ipt = this.searchKeywords
                this.keywords = this.searchKeywords
            }
            this.keywords = this.searchKeywords
            sessionStorage.setItem('keywords', this.searchKeywords)
        }
        if (this.type === 1 && this.searchSongs.length === 0 && sessionStorage.getItem('keywords') !== null) {
            this.setSearchKeywords(sessionStorage.getItem('keywords'))
        }

    },
    mounted() {
    },
    methods: {
        ...mapMutations(['setMusicInfo', 'setMusicUrl', 'setSearchSongs', 'setMusicPlayerId', 'setSearchKeywords']),

        //组件搜索框的搜索
        search() {
            if (this.ipt === '') return
            this.keywords = this.ipt
            this.ipt = ''
            this.setSearchKeywords(this.keywords)
            sessionStorage.setItem('keywords', this.searchKeywords)
            // this.$router.push({ path: '/search', query: { keywords: this.ipt } })
        },

        //获取搜索列表
        getSearchList(keywords) {
            getSearchApi(keywords, 1)
                .then(res => {
                    if (!res.data.result.songs) return this.setSearchSongs(null)
                    this.setSearchSongs(res.data.result.songs)
                    this.searchSongs.forEach(item => {
                        item.duration = getTime(item.duration)
                    })
                }).catch(err => {
                    console.error(err)
                })
        },

        //播放歌曲
        play(row) {
            const song = toRaw(row)
            getCheckMusic(song.id)
                .then(res => {
                    if (res.data.success !== true) {
                        return ElMessage.warning('抱歉, 该歌暂无版权')
                    }
                    this.setMusicPlayerId(song.id)
                })
        },

        //搜索单曲
        searchSingle() {
            if (this.keywords === '') return
            getSearchApi(this.keywords, 1)
                .then(res => {
                    this.setSearchSongs(res.data.result.songs)
                    this.searchSongs.forEach(item => {
                        item.duration = getTime(item.duration)
                    })
                }).catch(err => {
                    console.error(err)
                })
        },

        //搜索歌手
        searchSinger() {
            if (this.keywords === '') return
            getSearchApi(this.keywords, 100)
                .then(res => {
                    this.singerLsit = res.data.result.artists
                }).catch(err => {
                    console.error(err)
                })
        },

        //搜索歌单
        searchList() {
            if (this.keywords === '') return
            getSearchApi(this.keywords, 1000)
                .then(res => {
                    this.playList = res.data.result.playlists
                }).catch(err => {
                    console.error(err)
                })
        },

        //搜索专辑
        searchAlbum() { },

        //表单搜索模式切换
        tableHandlerClick(tab, event) {
            // console.log(tab.props.name)
            if (tab.props.name === 'searchSingle') {
                this.type = 1
                this.getSearchList(this.keywords)
            } else if (tab.props.name === 'searchSinger') {
                this.type = 100
                this.searchSinger()
            } else if (tab.props.name === 'searchList') {
                this.type = 1000
                this.searchList()
            }
        },
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

    .search-box {
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