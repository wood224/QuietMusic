<template>
    <div class="banner" id="banner" v-loading="loading" element-loading-background="rgba(122, 122, 122, 0.2)"
        element-loading-text="加载中...(如果长时间未响应, 请刷新页面后重试)">
        <el-carousel :interval="3000" type="card" trigger="click" :initial-index="1">
            <el-carousel-item v-for="item in banners" :key="item">
                <img :src="item.imageUrl" :alt="item.typeTitle">
            </el-carousel-item>
        </el-carousel>
    </div>
</template>

<script>
import axios from 'axios'
import { getBannerApi } from "../http/api"
import { mapGetters } from 'vuex'

export default {
    name: 'Banner',
    data() {
        return {
            banners: [],
            loading: false
        }
    },
    computed: {
        ...mapGetters(['getBaseURLCloudMusic'])
    },
    watch: {
        banners: {
            handler() {
                this.loading = false
            }
        }
    },
    created() {
        this.loading = true
        this.getBanner()
    },
    mounted() {

    },
    methods: {
        getBanner() {
            // const { data: res } = await this.$http.get('/banner')
            // this.banners = res.banners
            getBannerApi()
                .then(res => {
                    this.banners = res.data.banners
                })
                .catch(err => {
                    console.error(err)
                })
        }
    },
}
</script>

<style lang="less">
.banner {
    background-color: #80808033;

    .el-carousel {
        .el-carousel__container {
            img {
                height: 300px;
            }
        }
    }
}
</style>