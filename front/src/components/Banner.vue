<template>
    <div class="banner" id="banner" v-loading="loading" element-loading-text="Loading..."
        element-loading-background="rgba(122, 122, 122, 0.4)">
        <el-carousel :interval="3000" type="card" trigger="click" v-if="banners[0]">
            <el-carousel-item v-for="item in banners" :key="item">
                <img :src="item.imageUrl" :alt="item.typeTitle">
            </el-carousel-item>
        </el-carousel>
    </div>
</template>

<script>
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
    created() {
        this.getBanner()
    },
    mounted() {

    },
    methods: {
        getBanner() {
            this.loading = true
            getBannerApi()
                .then(res => {
                    this.banners = res.data.banners
                    this.loading = false
                })
                .catch(err => {
                    console.error(err)
                    this.loading = false
                })
        }
    },
}
</script>

<style lang="less">
.banner {
    .el-carousel {
        .el-carousel__container {
            img {
                height: 18.6vw;
            }
        }
    }
}
</style>