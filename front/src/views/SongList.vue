<template>
	<div class="song-list-container">
		<div class="left">
			<div class="pic">
				<img :src="songListDetail.coverImgUrl" alt="">
			</div>
			<div class="detail">
				<span>名称：</span>{{ songListDetail.name }}
				<br>
				<span>创建人：</span>{{ creatorName }}
				<br>
				<span>更新时间：</span>{{ updateTime }}
			</div>
			<div class="intro scrollbar">
				<span>简介：</span>
				<span class="description">{{ songListDetail.description }}</span>
			</div>
		</div>
		<div class="right">
			<div class="title">
				<h6>&lt;{{ songListDetail.name }}&gt; －歌曲列表</h6>
				<div class="action">
					<el-button type="primary" @click="addAllPlaylistSong">
						<span class="text">
							<i class="fa fa-play"></i>将歌曲添加至播放列表
						</span>
					</el-button>
					<el-button type="success" @click="selectList">
						<span class="text">
							<el-icon>
								<FolderAdd />
							</el-icon>
							将所有歌曲添加至歌单
						</span>
					</el-button>
				</div>
			</div>
			<div class="songs">
				<SongsList :list="songList" type="songlist" @getAll="getAll" :count="songListDetail.trackCount"
					v-loading="loading"></SongsList>
			</div>
		</div>
		<el-dialog v-model="loginDialogVisible" title="提示" width="30%" :align-center="true">
			<span>请先登录</span>
			<template #footer>
				<span class="dialog-footer">
					<el-button @click="loginDialogVisible = false">稍后再说</el-button>
					<el-button type="primary" @click="login">现在登录</el-button>
				</span>
			</template>
		</el-dialog>

		<el-dialog v-model="collectDialogVisible" title="选择歌单" width="30%" :align-center="true">
			<el-scrollbar>
				<ul class="dialog-wrapper">
					<li v-for="item in userSongList" :key="item.id" @click="addAllSonglist(item.id)">
						<div class="songlist">
							<div class="pic">
								<img :src="item.coverImg" alt="">
							</div>
							<div class="name">{{ item.name }}</div>
						</div>
					</li>
				</ul>
			</el-scrollbar>
		</el-dialog>
	</div>
</template>

<script>
import { mapState, mapMutations, mapActions } from "vuex"
import { getPlaylistDetail, getCheckMusic, getPlaylistAll } from "../http/api"
import { getTime } from "../fun"
import { ElMessage } from 'element-plus'
import SongsList from "../components/SongsList.vue"
import qs from 'qs'
import axios from "axios"

export default {
	name: 'SongList',
	data() {
		return {
			songListDetail: {},     //歌单详情信息
			songList: [],           //歌单中歌曲列表
			creatorName: '',        //歌单创建作者名
			updateTime: '',         //歌单更新时间
			duration: '',           //歌曲时长

			userSongList: [], 		  //用户歌单列表

			collectDialogVisible: false,
			loginDialogVisible: false,

			loading: false,
		}
	},
	mounted() {
		// this.setSongListId(sessionStorage.getItem('songListId'))
		const id = this.$route.params.id
		getPlaylistDetail(id)
			.then(res => {
				const playlisy = res.data.playlist
				this.songListDetail = playlisy
				// this.songList = playlisy.tracks
				// this.songList.forEach(item => {
				// 	item.dt = getTime(item.dt)
				// })
				this.creatorName = playlisy.creator.nickname
				const date = new Date(playlisy.updateTime)
				const y = date.getFullYear()
				const m = date.getMonth() + 1
				const d = date.getDate()
				this.updateTime = `${y}-${m}-${d}`

				this.loading = true
				this.getAll(0)
			})
	},
	computed: {
		...mapState(['songListId', 'playlistId'])
	},
	methods: {
		...mapMutations(['setSongListId', 'setPlaylistId', 'setPlaylist']),
		...mapActions(['play', 'getPlaylistSongs']),

		//获取歌单所有歌曲
		getAll(offset, id = this.songListDetail.id, limit = 10,) {
			this.loading = true
			getPlaylistAll(id, limit, offset).then(res => {
				const data = res.data.songs
				this.songList = data
				this.songList.forEach(item => {
					item.dt = getTime(item.dt)
				})
				this.loading = false
			})
		},

		//添加歌单所有歌曲到歌曲列表
		async addAllPlaylistSong() {
			let msg = ElMessage('添加中...')
			for (let i of this.songList) {
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
		},

		//选择收藏歌单
		selectList() {
			const userInfo = JSON.parse(localStorage.getItem('userInfo'))
			if (userInfo !== null) {
				this.collectDialogVisible = true
				this.$http.get('/songlist/lists', {
					params: {
						id: userInfo.id,
					}
				}).then(res => {
					const data = res.data.data;
					this.userSongList = data;
				})
			}
			else {
				this.loginDialogVisible = true;
			}
		},

		//将所有歌曲添加至歌单
		async addAllSonglist(id) {
			const { data } = await getPlaylistAll(this.songListDetail.id, 0, 0);
			let songs = []
			for (let i in data.songs) {
				const item = data.songs[i];
				if (item.noCopyrightRcmd) {
					continue
				}
				songs.push({
					listId: id,
					musicId: item.id,
					musicName: item.name,
					singerName: item.ar.map(item => {
						return item.name;
					}),
					album: item.al.name,
					time: getTime(item.dt)
				})
			}
			this.$http.post('/songlistdetails/insertAll',
				songs
			).then(res => {
				const data = res.data
				if (data.code === 200) {
					ElMessage.success('添加成功')
				}
				else {
					ElMessage.warning(data.msg)
				}
				this.collectDialogVisible = false
			})
		},

		//弹窗跳转登录
		login() {
			this.loginDialogVisible = false
			return location.href = 'login.html'
		},

		//表格行样式
		rowStyle({ row, rowIndex }) {
			if (row.noCopyrightRcmd !== null) {
				return {
					color: "#bbb",
				}
			}
		}
	},
	components: {
		SongsList,
	},
}
</script>

<style lang="less" >
.song-list-container {
	display: flex;
	width: 1000px;
	height: 100%;
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
					vertical-align: middle;

					.fa {
						height: auto;
					}
				}
			}
		}
	}

	.dialog-wrapper {
		.songlist {
			margin-bottom: 20px;
			display: flex;
			align-items: center;
			box-sizing: border-box;
			padding: 10px;
			cursor: pointer;

			&:hover {
				background-color: #f2f2f2;
			}

			.pic {
				margin-right: 20px;
				width: 60px;
				height: 60px;

				img {
					width: 100%;
					height: 100%;
					object-fit: contain;
				}
			}

			.name {
				font-size: 20px;
			}
		}
	}
}

tr {
	cursor: pointer;
}
</style>