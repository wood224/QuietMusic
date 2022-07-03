<template>
    <div>
        <el-table :data="songs" height="700" style="width: 100%">
            <el-table-column prop="name" label="歌曲名" width="200" />
            <el-table-column label="歌手" width="180">
                <template slot-scope="scope">
                    <span v-for="item in scope.row.artists" :key="item.id">
                        {{ item.name }}
                    </span>
                </template>
            </el-table-column>
            <el-table-column prop="album.name" label="专辑" />
            <el-table-column prop="duration" label="时长" />
        </el-table>
    </div>
</template>

<script>
import { mapState } from "vuex"

export default {
    name: 'Search',
    data() {
        return {
            songs: []
        }
    },
    computed: {
        ...mapState(['searchSongs'])
    },
    created() {
        this.songs = this.searchSongs
        this.songs.forEach(item => {
            item.duration = this.getTime(item.duration)
        })
    },
    methods: {
        getTime(duration) {
            let ss = Math.ceil(duration / 1000 % 60)
            ss = ss < 10 ? '0' + ss : ss
            let mm = Math.floor(duration / 1000 / 60)
            mm = mm < 10 ? '0' + mm : mm
            return mm + ':' + ss
        }
    },
}
</script>

<style lang="less" scoped>
</style>