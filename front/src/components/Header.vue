<template>
    <div class="header">
        <div class="menu">
            <ul>
                <li>
                    <router-link to="/home">首页</router-link>
                </li>
            </ul>

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
export default {
    name: 'Header',
    data() {
        return {
            userInfo: {}
        }
    },
    mounted() {
        //判断登录状态
        this.userInfo = JSON.parse(localStorage.getItem('userInfo'))
    },
    methods: {
        logout() {
            localStorage.clear()
            //返回主页
            location.href = 'index.html'
        }
    },
}
</script>

<style lang="less" scoped>
.header {
    display: flex;
    height: 30px;
    padding: 0 20px;
    background: #2980b9;
    justify-content: space-between;
    color: white;
    font-size: 20px;
    line-height: 30px;

    .user {
        display: flex;
        cursor: pointer;

        &:hover {
            .userMenu {
                display: block;
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

    a {
        color: white;
    }
}
</style>