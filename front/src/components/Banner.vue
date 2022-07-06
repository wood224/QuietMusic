<template>
    <div class="banner">
        <el-carousel :interval="3000" type="card" trigger="click">
            <el-carousel-item v-for="item in banners" :key="item">
                <img :src="item.imageUrl" :alt="item.typeTitle">
            </el-carousel-item>
        </el-carousel>
    </div>
</template>

<script>
export default {
    name: 'Banner',
    data() {
        return {
            banners: []
        }
    },
    created() {
        this.getBanner()
    },
    methods: {
        async getBanner() {
            const { data: res } = await this.$http.get('/song/banner')
            this.banners = res.banners
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