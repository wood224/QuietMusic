<template>
	<div class="song-list-container">
		<div class="left">
			<div class="pic">
				<img :src="albumDetail.blurPicUrl" alt="">
			</div>
			<div class="detail">
				<span>名称：</span>{{ albumDetail.name }}
				<br>
				<span>歌手：</span>{{ creatorName }}
				<br>
				<span>发行时间：</span>{{ publishTime }}
			</div>
			<div class="intro scrollbar">
				<span>简介：</span>
				<span class="description">{{ albumDetail.description }}</span>
			</div>
		</div>
		<div class="right">
			<div class="title">
				<h6>&lt;{{ albumDetail.name }}&gt; －歌曲列表</h6>
				<div class="btn-list">
					<el-button type="primary" @click="addAllPlaylistSong">
						<span class="text">
							<i class="fa fa-play"></i>将歌曲添加至播放列表
						</span>
					</el-button>
				</div>
			</div>
			<div class="songs">
				<SongsList :list="albumPageSongs" type="album" @getAll="getPageSongs" :count="albumSongs.length"
					v-loading="loading">
				</SongsList>
				<!-- <el-table :data="albumSongs" @cell-click="play" max-height="520">
					<el-table-column prop="name" label="歌曲名" width="300" />
					<el-table-column label="歌手" width="200">
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
	</div>
</template>

<script>
import { mapState, mapMutations, mapActions } from "vuex"
import SongsList from "../components/SongsList.vue"
import { getAlbumDetail, getCheckMusic } from "../http/api"
import { getTime } from "../fun"
import { ElMessage } from 'element-plus'

export default {
	name: 'AlbumDetail',
	data() {
		return {
			albumDetail: {},     //专辑详情信息
			albumSongs: [],         //专辑中歌曲列表
			albumPageSongs: [], 	//专辑中分页歌曲列表
			creatorName: '',        //专辑创建作者名
			publishTime: '',         //专辑更新时间
			duration: '',           //歌曲时长

			loading: false,
		}
	},
	mounted() {
		// this.setAlbumId(sessionStorage.getItem('albumId'))
		const id = this.$route.params.id
		this.loading = true
		getAlbumDetail(id)
			.then(res => {
				const data = res.data
				this.albumDetail = data.album
				this.albumSongs = data.songs
				this.albumSongs.forEach(item => {
					item.dt = getTime(item.dt)
				})
				this.creatorName = data.album.artist.name
				const date = new Date(data.album.publishTime)
				const y = date.getFullYear()
				const m = date.getMonth() + 1
				const d = date.getDate()
				this.publishTime = `${y}-${m}-${d}`
				this.getPageSongs(0)
				this.loading = false
			})

	},
	computed: {
		...mapState(['albumId', 'playlistId'])
	},
	methods: {
		...mapMutations(['setAlbumId', 'setPlaylistId', 'setPlaylist']),
		...mapActions(['play', 'getPlaylistSongs']),

		//分页获取歌曲
		getPageSongs(offset) {
			this.albumPageSongs = this.albumSongs.slice(offset, offset + 10)
		},

		//添加专辑所有歌曲到歌曲列表
		async addAllPlaylistSong() {
			let msg = ElMessage('添加中...')
			for (let i of this.album) {
				const { data: res } = await getCheckMusic(i.id)
				if (res.success === true) {
					let songInfo = i
					let artists = []
					songInfo.ar.forEach(e => {
						artists.push(e.name)
					})
					await this.$http.post('/listsongs/add', {
						listId: this.playlistId,
						musicId: songInfo.id,
						musicName: songInfo.name,
						singerName: artists,
						time: songInfo.dt
					})
				}
			}
			msg.close()
			ElMessage.success('已将所有有版权的歌曲添加至播放列表')
			this.getPlaylistSongs()
		}
	},
	componentes: {
		SongsList,
	},
}
</script>

<style lang="less" >
.song-list-container {
	display: flex;
	width: 1000px;
	height: calc(100vh - 198px);
	margin: 0 auto;
	padding: 10px 0;
	font-size: 14px;
	box-sizing: border-box;

	.left {
		width: 20%;
		height: 100%;
		margin-right: 20px;

		span {
			font-size: 12px;
			color: #7d7d7d;
		}

		.pic {
			width: 180px;
			height: 180px;

			img {
				width: 100%;
				height: 100%;
				object-fit: contain;
			}
		}

		.intro {
			height: 340px;
			overflow: auto;

			.description {
				font-size: 14px;
				color: black;
			}
		}
	}

	.right {
		width: 80%;
		padding: 0 20px;
		border-left: 1px solid gray;

		.title {
			padding-bottom: 10px;
			border-bottom: 1px solid gray;

			h6 {
				font-weight: 800;
			}

			.el-button {
				.text {
					margin: auto;
					font-size: 14px;

					.fa {
						height: auto;
					}
				}
			}
		}
	}
}

tr {
	cursor: pointer;
}
</style>