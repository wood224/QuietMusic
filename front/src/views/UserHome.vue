<template>
    <div class="user-home-container">
        <div class="userInfo">
            <div class="img">
                <el-image :src="url" fit="cover">
                    <template #error>
                        <div class="image-slot">
                            <el-icon>
                                <Picture />
                            </el-icon>
                        </div>
                    </template>
                </el-image>
            </div>
            <el-descriptions class="info" :title="userDetail.name" :column="3" border>
                <template #extra>
                    <el-button type="primary" @click="setUpdateView">编辑个人资料</el-button>
                </template>
                <el-descriptions-item width="100px">
                    <template #label>
                        <div class="cell-item">
                            <el-icon>
                                <user />
                            </el-icon>
                            账号
                        </div>
                    </template>
                    {{ userDetail.username }}
                </el-descriptions-item>
                <el-descriptions-item>
                    <template #label>
                        <div class="cell-item">
                            <el-icon>
                                <iphone />
                            </el-icon>
                            电话号码
                        </div>
                    </template>
                    {{ userDetail.phone }}
                </el-descriptions-item>
                <el-descriptions-item>
                    <template #label>
                        <div class="cell-item">
                            <el-icon>
                                <UserFilled />
                            </el-icon>
                            性别
                        </div>
                    </template>
                    <span v-if="userDetail.sex === 0">保密</span>
                    <el-icon color="hotpink" size="20px" v-else-if="userDetail.sex === 2">
                        <Female />
                    </el-icon>
                    <el-icon color="skyblue" size="20px" v-else-if="userDetail.sex === 1">
                        <Male />
                    </el-icon>
                </el-descriptions-item>
                <el-descriptions-item>
                    <template #label>
                        <div class="cell-item">
                            <el-icon>
                                <ChatLineSquare />
                            </el-icon>
                            简介
                        </div>
                    </template>
                    <span v-if="userDetail.description === null || userDetail.description === ''">这个人很懒，没有留下任何内容</span>
                    <span v-else>{{ userDetail.description }}</span>
                </el-descriptions-item>
            </el-descriptions>
        </div>
        <UpdateUserInfo v-show="updateView" @setUpdateView="setUpdateView" @getUserDetail="getUserDetail">
        </UpdateUserInfo>
    </div>
</template>

<script>
import { mapState, mapMutations } from "vuex"
import UpdateUserInfo from '../components/UpdateUserInfo.vue'

export default {
    name: 'UserHome',
    data() {
        return {
            userDetail: {},
            updateView: false,
            url: 'https://fuss10.elemecdn.com/e/5d/4a731a90594a4af544c0c25941171jpeg.jpeg'
        }
    },
    computed: {
        ...mapState(['userInfo']),
    },
    created() {
        this.getUserDetail()
    },
    methods: {
        ...mapMutations(['setUserInfo']),

        getUserDetail() {
            this.$http.get(`/user/${this.userInfo.id}`)
                .then(res => {
                    this.userDetail = res.data.data
                    let userInfo = {
                        name: res.data.data.name,
                        id: res.data.data.id,
                        sex: res.data.data.sex,
                        phone: res.data.data.phone,
                        description: res.data.data.description
                    }
                    //将表示登录状态的对象存入 localstorage 和 vuex 中
                    localStorage.setItem("userInfo", JSON.stringify(userInfo))
                    this.setUserInfo(userInfo)
                }).catch((err) => {
                    console.error(err)
                })
        },
        setUpdateView() {
            this.updateView = !this.updateView
        }
    },
    components: {
        UpdateUserInfo,
    }

}
</script>

<style lang="less">
.user-home-container {
    width: 1000px;
    margin: 0 auto;

    .userInfo {
        display: flex;
        margin-top: 10px;
        box-shadow: 4px 4px 15px;

        .img {
            width: 200px;
            height: 200px;
            padding: 20px;

            .el-image {
                width: 100%;
                height: 100%;

                .image-slot {
                    display: flex;
                    justify-content: center;
                    align-items: center;
                    width: 100%;
                    height: 100%;
                    background: var(--el-fill-color-light);
                    color: var(--el-text-color-secondary);
                    font-size: 30px;

                    .el-icon {
                        font-size: 30px;
                        color: black;
                    }
                }
            }
        }

        .info {
            width: 800px;
            padding: 20px 0;
            padding-right: 10px;
        }
    }
}
</style>