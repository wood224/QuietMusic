<template>
    <div class="search-container">
        <div class="mt-4 search-box">
            <el-input v-model="ipt" placeholder="搜索歌曲, 歌手...(按下ESC可快速清空)" clearable class="input-with-select"
                @keyup.enter="search" @keyup.esc="ipt = ''">
                <template #prepend>
                    <el-button :icon="Search" @click="search" />
                </template>
            </el-input>
        </div>
        <el-tabs type="border-card" @tab-click="tableHandlerClick" v-model="activeName" v-loading="loading"
            element-loading-text="Loading..." element-loading-background="rgba(224, 224, 224, 0.4)">
            <el-tab-pane label="单曲" name="searchSingle">
                <el-table :data="searchSongs" height="480" style="width: 100%" @cell-click="play" stripe>
                    <el-table-column prop="name" label="歌曲名" width="300" />
                    <el-table-column label="歌手" width="300">
                        <template #default="scope">
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
                    <li v-for="item in singerLsit" :key="item.id" @click="singerDetail(item.id)">
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
                <el-table :data="playList" height="480" style="width: 100%" @cell-click="playlistDetail" stripe>
                    <el-table-column prop="name" label="名字" width="500" />
                    <el-table-column prop="creator.nickname" label="创建者" />
                    <el-table-column prop="trackCount" label="歌曲数 (首)" />
                </el-table>
            </el-tab-pane>
            <el-tab-pane label="专辑" name="searchAlbum">
                <ul class="albumList">
                    <li v-for="item in albumList" :key="item.id" @click="albumDetail(item.id)">
                        <div class="album">
                            <div class="pic">
                                <img :src="item.picUrl" alt="">
                            </div>
                            <div class="albumInfo">
                                <span>{{ item.name }}</span>
                            </div>
                        </div>
                    </li>
                </ul>
            </el-tab-pane>
        </el-tabs>
    </div>
</template>

<script>
import { Search } from '@element-plus/icons-vue'
import { getTime } from "../fun"
import { getSearchApi } from "../http/api"
import { mapState, mapMutations, mapActions } from "vuex"
import { toRaw } from '@vue/reactivity'

export default {
    name: 'Search',
    data() {
        return {
            ipt: '',
            keywords: '',
            singerLsit: [],     //歌手列表
            playList: [],       //歌单列表
            albumList: [],      //专辑列表

            activeName: '',     //用于取消只度警告

            //搜索模式
            type: 1,

            loading: false
        }
    },
    computed: {
        ...mapState(['searchSongs', 'searchKeywords', 'playlistId', 'searchActiveName']),
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
                    return this.searchList()
                if (this.type === 10)
                    return this.searchAlbum()
            }
        },
        searchActiveName: {
            handler() {
                this.activeName = this.searchActiveName
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

        //设置type
        if (this.searchActiveName === 'searchSingle') this.type = 1
        if (this.searchActiveName === 'searchSinger') this.type = 100
        if (this.searchActiveName === 'searchList') this.type = 1000
        if (this.searchActiveName === 'searchAlbum') this.type = 10
        this.activeName = this.searchActiveName



    },
    mounted() {
    },
    methods: {
        ...mapMutations(['setMusicInfo', 'setMusicUrl', 'setSearchSongs', 'setMusicPlayerId', 'setSearchKeywords', 'setSearchActiveName', 'setSongListId', 'setSingerId', 'setAlbumId']),
        ...mapActions(['play']),

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

        //搜索单曲
        searchSingle() {
            if (this.keywords === '') return
            this.loading = true
            getSearchApi(this.keywords, 1)
                .then(res => {
                    this.setSearchSongs(res.data.result.songs)
                    this.searchSongs.forEach(item => {
                        item.duration = getTime(item.duration)
                    })
                    this.loading = false
                }).catch(err => {
                    console.error(err)
                    this.loading = false
                })
        },

        //搜索歌手
        searchSinger() {
            if (this.keywords === '') return
            this.loading = true
            getSearchApi(this.keywords, 100)
                .then(res => {
                    this.singerLsit = res.data.result.artists
                    this.loading = false
                }).catch(err => {
                    console.error(err)
                    this.loading = false
                })
        },

        //搜索歌单
        searchList() {
            if (this.keywords === '') return
            this.loading = true
            getSearchApi(this.keywords, 1000)
                .then(res => {
                    this.playList = res.data.result.playlists
                    this.loading = false
                }).catch(err => {
                    console.error(err)
                    this.loading = false
                })
        },

        //搜索专辑
        searchAlbum() {
            if (this.keywords === '') return
            this.loading = true
            getSearchApi(this.keywords, 10)
                .then(res => {
                    this.albumList = res.data.result.albums
                    this.loading = false
                }).catch(err => {
                    console.error(err)
                    this.loading = false
                })
        },

        //表单搜索模式切换
        tableHandlerClick(tab, event) {
            // console.log(tab.props.name)
            if (tab.props.name === 'searchSingle') {
                this.type = 1
                this.setSearchActiveName('searchSingle')
                this.getSearchList(this.keywords)
            } else if (tab.props.name === 'searchSinger') {
                this.type = 100
                this.setSearchActiveName('searchSinger')
                this.searchSinger()
            } else if (tab.props.name === 'searchList') {
                this.type = 1000
                this.setSearchActiveName('searchList')
                this.searchList()
            } else if (tab.props.name === 'searchAlbum') {
                this.type = 10
                this.setSearchActiveName('searchAlbum')
                this.searchAlbum()
            }
        },

        //进入歌单详情
        playlistDetail(row) {
            const songList = toRaw(row)
            this.setSongListId(songList.id)
            sessionStorage.setItem('songListId', songList.id)
            this.$router.push('songList')
        },

        //进入歌手详情
        singerDetail(id) {
            this.setSingerId(id)
            sessionStorage.setItem('singerId', id)
            this.$router.push('singerDetail')
        },

        //进入专辑详情
        albumDetail(id) {
            this.setAlbumId(id)
            sessionStorage.setItem('albumId', id)
            this.$router.push('albumDetail')
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
                justify-content: flex-start;
                height: 59vh;
                overflow: auto;

                .singer {
                    margin-right: 1.28vw;
                    width: 8vw;
                    cursor: pointer;

                    .pic {
                        height: 8vw;
                        box-shadow: 1px 1px 5px black;

                        img {
                            width: 100%;
                            height: 100%;
                            object-fit: cover;
                        }
                    }

                    .singerInfo {
                        overflow: hidden;
                        white-space: nowrap;
                        text-overflow: ellipsis;
                        height: 30px;
                    }
                }
            }

            .albumList {
                display: flex;
                flex-wrap: wrap;
                justify-content: flex-start;
                height: 59vh;
                overflow: auto;

                .album {
                    margin-right: 1.28vw;
                    width: 8vw;
                    cursor: pointer;

                    .pic {
                        height: 8vw;
                        box-shadow: 1px 1px 5px black;

                        img {
                            width: 100%;
                            height: 100%;
                            object-fit: cover;
                        }
                    }

                    .albumInfo {
                        overflow: hidden;
                        white-space: nowrap;
                        text-overflow: ellipsis;
                        height: 30px;
                    }
                }
            }
        }
    }
}
</style>