<template>
    <div class="music-container">
        <div class="music-info">
            <div class="music-img">
                <img src="" alt="">
            </div>
            <div class="music-control">
                <div class="music-name">
                    <span class="name">{{ musicInfo.name }}</span>
                    <span class="singer" v-for="(item, index) in musicInfo.ar" :key="index">
                        {{ item.name + '/' }}
                    </span>
                </div>
                <div class="btns">
                    <div class="nav">
                        <button id="prev">
                            <i class="fas fa-backward"></i>
                        </button>
                        <button id="play">
                            <i class="fas fa-play"></i>
                        </button>
                        <button id="next">
                            <i class="fas fa-forward"></i>
                        </button>
                    </div>
                    <div class="action"></div>
                </div>
                <div class="demo-progress">
                    <el-progress :percentage="50" />
                </div>
            </div>
        </div>
    </div>
</template>

<script>
import { mapState } from "vuex"

export default {
    name: 'MusicPlayer',
    data() {
        return {
            imgUrl: ''
        }
    },
    computed: {
        ...mapState(['musicInfo'])
    },
    created() {

    },
}
</script>

<style lang="less">
.music-container {
    display: flex;
    position: fixed;
    bottom: 0;
    justify-content: center;
    width: 100%;
    height: 80px;
    background-color: white;
    z-index: 999;

    .music-info {
        display: flex;
        justify-content: space-between;
        padding: 0 10px;
        width: 50%;
        height: 100%;

        .music-img {
            width: 80px;
            height: 100%;

            img {
                background-color: green;
                width: 100%;
                height: 100%;
                border-radius: 50%;
            }
        }

        .music-control {
            display: flex;
            flex-direction: column;
            justify-content: space-around;
            width: 80%;
            height: 100%;

            .music-name {
                display: flex;
                height: 20px;

                .name {
                    margin-right: 150px;
                }
            }

            .btns {
                display: flex;

                .nav {
                    button {
                        // width: 30px;
                        // height: 30px;
                        border: 0;
                        background-color: rgba(0, 0, 0, 0);
                        padding: 10px;
                        margin: 0 20px;
                        font-size: 20px;
                        color: #0decfc;
                        cursor: pointer;
                    }
                }
            }


            .demo-progress {
                .el-progress {
                    cursor: pointer;
                }
            }

        }
    }
}
</style>