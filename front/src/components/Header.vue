<template>
    <nav class="navbar navbar-light bg-primary">
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
            <el-icon @click="search">
                <Search />
            </el-icon>
            <div class="ipt">
                <el-input v-model="input" placeholder="搜索歌曲, 歌手..." @keyup.enter="search" @keyup.esc="input = ''" />
            </div>
        </div>
        <div class="user">
            <div v-if="userInfo === null" class="login">
                <a href="login.html">登录</a>
            </div>
            <div v-else class="userInfo">
                <el-dropdown size="large">
                    <span class="el-dropdown-link">
                        {{ userInfo.name }}, 欢迎您
                        <el-icon class="el-icon--right">
                            <arrow-down />
                        </el-icon>
                    </span>
                    <template #dropdown>
                        <el-dropdown-menu>
                            <el-dropdown-item>Action 1</el-dropdown-item>
                            <el-dropdown-item>Action 2</el-dropdown-item>
                            <el-dropdown-item>Action 3</el-dropdown-item>
                            <el-dropdown-item divided @click="logout()">退出登录</el-dropdown-item>
                        </el-dropdown-menu>
                    </template>
                </el-dropdown>
            </div>
        </div>
    </nav>
</template>

<script>
import { mapMutations, mapState } from "vuex"
import axios from "axios"

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
            else {
                for (let i = 0; i < this.classBgWihte.length; i++) {
                    this.classBgWihte[i] = false
                }
            }
        }
    },
    methods: {
        ...mapMutations(['setSearchKeywords']),

        logout() {
            localStorage.removeItem('userInfo')
            //返回主页
            this.userInfo = JSON.parse(localStorage.getItem('userInfo'))
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

        async search() {
            if (this.input === '') return
            this.setSearchKeywords(this.input)
            this.$router.push({ path: '/search', query: { keywords: this.input } })
        }
    },
}
</script>

<style lang="less">
.navbar.navbar-light {
    z-index: 999;

    .menu {
        ul {
            display: flex;
            justify-content: space-around;
            align-items: center;
            height: 100%;
            margin-top: 1rem;

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
                width: 150px;
                height: 50px;
                text-align: center;
                cursor: pointer;

                &:hover {
                    a {
                        color: white;
                        text-decoration: none;
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
        // height: 50px;
        align-items: center;

        .el-icon {
            font-size: 28px;
            cursor: pointer;
        }
    }

    .user {
        position: relative;
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
            display: flex;
            background-color: white;
            border-radius: 30px;
            width: 100px;
            height: 50%;
            align-items: center;
            text-align: center;

            a {
                width: 100%;
                color: #000046;
                text-decoration: none;
            }
        }

        .userInfo {
            .el-dropdown-link {
                font-size: 20px;
                color: black;
            }
        }
    }
}

.el-dropdown-menu--large {
    .el-dropdown-menu__item {
        font-size: 16px;
    }
}
</style>