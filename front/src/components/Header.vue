<template>
    <div class="header">
        <div class="menu">
            <ul>
                <li id="home" @click="goHome" :class="{ bgWihte: classBgWihte[0] }">
                    <router-link to="/home">首页</router-link>
                </li>
                <li id="rank" @click="goRank" :class="{ bgWihte: classBgWihte[1] }">
                    <router-link to="/rank">榜单</router-link>
                </li>
                <li id="song" @click="goSong" :class="{ bgWihte: classBgWihte[2] }">
                    <router-link to="/song">歌单</router-link>
                </li>
                <li id="singer" @click="goSinger" :class="{ bgWihte: classBgWihte[3] }">
                    <router-link to="/singer">歌手</router-link>
                </li>
            </ul>
        </div>
        <div class="search">
            <el-icon>
                <Search />
            </el-icon>
            <div class="ipt">
                <el-input v-model="input" placeholder="搜索歌曲, 歌手" clearable @keyup.enter="search" />
            </div>
        </div>
        <div class="user">
            <div v-if="userInfo === null" class="login">
                <a href="login.html">登录</a>
            </div>
            <div v-else class="userInfo">
                <div class="userName">{{ userInfo.name }}, 欢迎您</div>
                <div class="userMenu">
                    <ul>
                        <li>功能1</li>
                        <li>功能2</li>
                        <li>功能3</li>
                        <li>功能4</li>
                        <li @click="logout">退出登录</li>
                    </ul>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
import { mapMutations, mapState } from "vuex"

export default {
    name: 'Header',
    data() {
        return {
            userInfo: {},
            classBgWihte: [false, false, false, false],
            input: ''
        }
    },
    mounted() {
        //判断登录状态
        this.userInfo = JSON.parse(localStorage.getItem('userInfo'))
    },
    computed: {
        ...mapState(['searchSongs'])
    },
    watch: {
        $route(to) {
            if (to.path === '/home') {
                for (let i = 0; i < this.classBgWihte.length; i++) {
                    if (i === 0) {
                        this.classBgWihte[i] = true
                    } else {
                        this.classBgWihte[i] = false
                    }
                }
            }
            else if (to.path === '/rank') {
                for (let i = 0; i < this.classBgWihte.length; i++) {
                    if (i === 1) {
                        this.classBgWihte[i] = true
                    } else {
                        this.classBgWihte[i] = false
                    }
                }
            }
            else if (to.path === '/song') {
                for (let i = 0; i < this.classBgWihte.length; i++) {
                    if (i === 2) {
                        this.classBgWihte[i] = true
                    } else {
                        this.classBgWihte[i] = false
                    }
                }
            }
            else if (to.path === '/singer') {
                for (let i = 0; i < this.classBgWihte.length; i++) {
                    if (i === 3) {
                        this.classBgWihte[i] = true
                    } else {
                        this.classBgWihte[i] = false
                    }
                }
            }
            else if (to.path === '/search') {
                for (let i = 0; i < this.classBgWihte.length; i++) {
                    this.classBgWihte[i] = false
                }
            }
        }
    },
    methods: {
        ...mapMutations(['setSearchSongs']),

        logout() {
            localStorage.clear()
            //返回主页
            location.href = 'index.html'
        },

        goHome() {
            this.$router.push('/')
        },
        goRank() {
            this.$router.push('/rank')
        },
        goSong() {
            this.$router.push('/song')
        },
        goSinger() {
            this.$router.push('/singer')
        },

        getTime(duration) {
            let ss = Math.ceil(duration / 1000 % 60)
            ss = ss < 10 ? '0' + ss : ss
            let mm = Math.floor(duration / 1000 / 60)
            mm = mm < 10 ? '0' + mm : mm
            return mm + ':' + ss
        },

        async search() {
            if (this.input === '') return
            const { data: res } = await this.$http.get('/search', {
                params: {
                    keywords: this.input
                }
            })
            this.setSearchSongs(res.result.songs)
            this.searchSongs.forEach(item => {
                item.duration = this.getTime(item.duration)
            })
            this.$router.push('/search')
        }
    },
}
</script>

<style lang="less" scoped>
.header {
    display: flex;
    height: 90px;
    padding: 0 20px;
    // background: skyblue;
    background-color: rgba(109, 213, 250, 0.2);
    justify-content: space-between;
    align-items: center;
    color: black;
    font-size: 20px;
    line-height: 30px;

    .menu {
        margin-left: 200px;

        ul {
            display: flex;
            justify-content: space-around;
            align-items: center;
            height: 100%;

            .bgWihte {
                background-color: #00A9FF;
                border-radius: 30px;

                &:hover {
                    a {
                        color: white;
                    }
                }

                a {
                    color: white;
                }
            }

            li {
                margin: 0 40px;
                width: 150px;
                height: 50px;
                text-align: center;
                cursor: pointer;

                &:hover {
                    a {
                        color: #6DD5FA;
                    }

                }

                a {
                    font-size: 24px;
                    line-height: 50px;
                    color: black;
                }
            }
        }
    }

    .search {
        display: flex;
        // background-color: #6DD5FA;
        height: 50px;
        align-items: center;

        .el-icon {
            font-size: 28px;
        }
    }

    .user {
        display: flex;
        height: 100%;
        align-items: center;
        cursor: pointer;

        &:hover {
            .userMenu {
                display: block;
            }
        }

        .login {
            background-color: #6DD5FA;
            width: 100px;
            height: 100%;
            text-align: center;

            a {
                color: #000046;
                line-height: 90px;
            }
        }

        .userInfo {
            .userName {
                line-height: 90px;
            }
        }

        .userMenu {
            display: none;
            color: black;
            box-shadow: 2px 2px 10px #D3D3D3;

            li {
                &:hover {
                    background-color: #F0F0F5;
                }
            }
        }
    }
}
</style>