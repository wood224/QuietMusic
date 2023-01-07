<template>
    <div class="singer-detail-container">
        <div class="singer-detail">
            <div class="pic">
                <img :src="artist.picUrl" alt="">
            </div>
            <div class="detail">
                <div class="name">
                    <h3>{{ artist.name }}</h3>
                </div>
                <div class="brief scrollbar">
                    <span>{{ artist.briefDesc }}</span>
                </div>
            </div>
        </div>
        <div class="songs">
            <SongsList :list="songs"></SongsList>
            <!-- <el-table :data="songs" stripe height="100%" style="width: 100%" @cell-click="play">
                <el-table-column prop="name" label="歌曲名" width="400" />
                <el-table-column label="歌手" width="300">
                    <template #default="scope">
                        <span v-for="item in scope.row.ar" :key="item.id">
                            {{ item.name }}&nbsp;
                        </span>
                    </template>
                </el-table-column>
                <el-table-column prop="al.name" label="专辑" />
                <el-table-column prop="dt" label="时长" />
            </el-table> -->
        </div>
    </div>
</template>

<script>
import { getSingerDetail } from "../http/api"
import SongsList from "../components/SongsList.vue"
import { getTime } from "../fun"
import { mapActions } from "vuex"

export default {
    name: 'SingerDetail',
    data() {
        return {
            artist: {},
            songs: [],
        }
    },
    created() {
        const singerId = this.$route.params.id
        getSingerDetail(singerId)
            .then(res => {
                const data = res.data
                this.artist = data.artist
                this.songs = data.hotSongs
                this.songs.forEach(item => {
                    item.dt = getTime(item.dt)
                })
            })
    },
    computed: {

    },
    methods: {
        ...mapActions(['play'])
    },
    componentes: {
        SongsList,
    }
}
</script>

<style lang="less">
.singer-detail-container {
    display: flex;
    flex-direction: column;
    margin: 0 auto;
    box-sizing: border-box;
    padding: 10px 0;
    width: 1000px;
    height: calc(100vh - 198px);

    .singer-detail {
        display: flex;
        justify-content: space-between;
        height: 160px;
        box-shadow: 3px 3px 5px gray;

        .pic {
            width: 160px;
            height: 100%;

            img {
                width: 100%;
                height: 100%;
                object-fit: cover;
            }
        }

        .detail {
            width: 800px;
            padding: 10px;

            .brief {
                height: 100px;
                overflow: auto;
            }
        }
    }

    .songs {
        margin-top: 20px;
        height: calc(100% - 200px);
    }

}

tr {
    cursor: pointer;
}
</style>