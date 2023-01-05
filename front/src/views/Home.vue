<template>
    <div class="home-wrapper">
        <Banner></Banner>
        <div class="wrapper-container">
            <div class="hot">
                <div class="title">
                    <div class="circle"></div>
                    <div class="text">
                        <span>热门推荐</span>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup>
import Banner from '../components/Banner.vue'


</script>

<style lang="less" scoped>
.home-wrapper {
    .wrapper-container {
        margin: 0 auto;
        width: 1200px;

        .hot {
            .title {
                display: flex;
                align-items: center;
                height: 40px;
                line-height: 40px;

                .circle {
                    margin-right: 14px;
                    display: flex;
                    justify-content: center;
                    align-items: center;
                    position: relative;
                    width: 20px;
                    height: 20px;
                    border-radius: 50%;
                    background-color: #66CCFF;

                    &::before {
                        content: '';
                        display: block;
                        width: 6px;
                        height: 6px;
                        border-radius: 50%;
                        background-color: #fff;
                    }
                }

                .text {
                    font-size: 20px;
                }
            }
        }
    }
}
</style>