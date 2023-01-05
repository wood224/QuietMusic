<template>
	<div class="search-container">
		<div class="search-box">
			<el-input v-model="ipt" placeholder="搜索歌曲, 歌手...(按下ESC可快速清空)" clearable class="input-with-select"
				@keyup.enter="search" @keyup.esc="ipt = ''">
				<template #prepend>
					<el-button :icon="Search" @click="search" />
				</template>
			</el-input>
		</div>
		<el-tabs type="border-card" @tab-click="tableHandlerClick" v-model="activeName" v-loading="loading"
			element-loading-text="Loading..." element-loading-background="rgba(224, 224, 224, 0.4)">

			<el-tab-pane label="单曲" name="searchSingle">
				<el-table :data="searchSongs" height="100%" style="width: 100%" @cell-click="play" stripe>
					<el-table-column label="歌曲名" width="500">
						<template #default="scope">
							<div class="song-name">
								<div class="name">
									<span>{{ scope.row.name }}</span>
								</div>
								<div class="btn-list">
									<el-tooltip content="收藏" :show-after="400" :hide-after="0">
										<div class="collect" @click.stop="selectList(scope.row.id)">
											<el-icon>
												<FolderAdd />
											</el-icon>
										</div>
									</el-tooltip>
								</div>
							</div>
						</template>
					</el-table-column>
					<el-table-column label="歌手" width="200">
						<template #default="scope">
							<span v-for="item in scope.row.artists" :key="item.name">
								{{ item.name }}&nbsp;
							</span>
						</template>
					</el-table-column>
					<el-table-column prop="album.name" label="专辑" />
					<el-table-column prop="duration" label="时长" />
				</el-table>
			</el-tab-pane>

			<el-tab-pane label="歌手" name="searchSinger">
				<el-scrollbar>
					<ul class="singerList">
						<li v-for="item in singerLsit" :key="item.id" @click="singerDetail(item.id)">
							<el-tooltip :content="item.name" effect="dark" :teleported="false" placement="bottom" :show-after="400">
								<div class="singer">
									<div class="pic">
										<img :src="item.picUrl" alt="">
									</div>
									<div class="singerInfo">
										<span>{{ item.name }}</span>
									</div>
								</div>
							</el-tooltip>
						</li>
					</ul>
				</el-scrollbar>
			</el-tab-pane>

			<el-tab-pane label="歌单" name="searchList">
				<el-table :data="playList" height="100%" style="width: 100%" @cell-click="playlistDetail" stripe>
					<el-table-column prop="name" label="名字" width="500" />
					<el-table-column prop="creator.nickname" label="创建者" />
					<el-table-column prop="trackCount" label="歌曲数 (首)" />
				</el-table>
			</el-tab-pane>

			<el-tab-pane label="专辑" name="searchAlbum">
				<el-scrollbar>
					<ul class="albumList">
						<li v-for="item in albumList" :key="item.id" @click="albumDetail(item.id)">
							<el-tooltip :content="item.name" effect="dark" :teleported="false" placement="bottom" :show-after="400">
								<div class="album">
									<div class="pic">
										<img :src="item.picUrl" alt="">
									</div>
									<div class="albumInfo">
										<span>{{ item.name }}</span>
									</div>
								</div>
							</el-tooltip>
						</li>
					</ul>
				</el-scrollbar>
			</el-tab-pane>
		</el-tabs>

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
					<li v-for="item in userSongList" :key="item.id" @click="collect(item.id)">
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
import { Search } from '@element-plus/icons-vue'
import { getTime, checkMusic } from "../fun"
import { getSearchApi, getCheckMusic, getMusicDetail } from "../http/api"
import { mapState, mapMutations, mapActions } from "vuex"
import { toRaw } from '@vue/reactivity'
import { ElMessage } from 'element-plus'

export default {
	name: 'Search',
	data() {
		return {
			ipt: '',
			keywords: '',
			singerLsit: [],     //歌手列表
			playList: [],       //歌单列表
			albumList: [],      //专辑列表

			activeName: '',     //用于取消只度警告

			//搜索模式
			type: 1,

			loading: false,

			loginDialogVisible: false,
			collectDialogVisible: false,
			collectMusicInfo: null,	//收藏的歌曲详情
			userSongList: [],			//用户歌单列表
		}
	},
	computed: {
		...mapState(['searchSongs', 'searchKeywords', 'playlistId', 'searchActiveName']),
	},
	watch: {
		searchKeywords: {
			handler() {
				this.keywords = this.searchKeywords
			}
		},
		keywords: {
			handler() {
				sessionStorage.setItem('keywords', this.searchKeywords)
				this.ipt = this.searchKeywords
				if (this.type === 1)
					return this.searchSingle()
				if (this.type === 100)
					return this.searchSinger()
				if (this.type === 1000)
					return this.searchList()
				if (this.type === 10)
					return this.searchAlbum()
			}
		},
		searchActiveName: {
			handler() {
				this.activeName = this.searchActiveName
			}
		}
	},
	created() {
		if (this.searchKeywords !== '') {
			if (sessionStorage.getItem('keywords') !== null && this.searchKeywords === sessionStorage.getItem('keywords')) {
				this.ipt = this.searchKeywords
				this.keywords = this.searchKeywords
			}
			this.keywords = this.searchKeywords
			sessionStorage.setItem('keywords', this.searchKeywords)
		}
		if (this.type === 1 && this.searchSongs.length === 0 && sessionStorage.getItem('keywords') !== null) {
			this.setSearchKeywords(sessionStorage.getItem('keywords'))
		}
		//设置type
		if (this.searchActiveName === 'searchSingle') this.type = 1
		if (this.searchActiveName === 'searchSinger') this.type = 100
		if (this.searchActiveName === 'searchList') this.type = 1000
		if (this.searchActiveName === 'searchAlbum') this.type = 10
		this.activeName = this.searchActiveName
	},
	mounted() {
	},
	methods: {
		...mapMutations(['setMusicInfo', 'setMusicUrl', 'setSearchSongs', 'setMusicPlayerId', 'setSearchKeywords', 'setSearchActiveName', 'setSongListId', 'setSingerId', 'setAlbumId']),
		...mapActions(['play']),

		//组件搜索框的搜索
		search() {
			if (this.ipt === '') return
			this.keywords = this.ipt
			this.ipt = ''
			this.setSearchKeywords(this.keywords)
			sessionStorage.setItem('keywords', this.searchKeywords)
			// this.$router.push({ path: '/search', query: { keywords: this.ipt } })
		},

		//获取搜索列表
		getSearchList(keywords) {
			getSearchApi(keywords, 1)
				.then(res => {
					if (!res.data.result.songs) return this.setSearchSongs(null)
					this.setSearchSongs(res.data.result.songs)
					this.searchSongs.forEach(item => {
						item.duration = getTime(item.duration)
					})
				}).catch(err => {
					console.error(err)
				})
		},

		//搜索单曲
		searchSingle() {
			if (this.keywords === '') return
			this.loading = true
			getSearchApi(this.keywords, 1)
				.then(res => {
					this.setSearchSongs(res.data.result.songs)
					this.searchSongs.forEach(item => {
						item.duration = getTime(item.duration)
					})
					this.loading = false
				}).catch(err => {
					console.error(err)
					this.loading = false
				})
		},

		//搜索歌手
		searchSinger() {
			if (this.keywords === '') return
			this.loading = true
			getSearchApi(this.keywords, 100)
				.then(res => {
					this.singerLsit = res.data.result.artists
					this.loading = false
				}).catch(err => {
					console.error(err)
					this.loading = false
				})
		},

		//搜索歌单
		searchList() {
			if (this.keywords === '') return
			this.loading = true
			getSearchApi(this.keywords, 1000)
				.then(res => {
					this.playList = res.data.result.playlists
					this.loading = false
				}).catch(err => {
					console.error(err)
					this.loading = false
				})
		},

		//搜索专辑
		searchAlbum() {
			if (this.keywords === '') return
			this.loading = true
			getSearchApi(this.keywords, 10)
				.then(res => {
					this.albumList = res.data.result.albums
					this.loading = false
				}).catch(err => {
					console.error(err)
					this.loading = false
				})
		},

		//表单搜索模式切换
		tableHandlerClick(tab, event) {
			// console.log(tab.props.name)
			if (tab.props.name === 'searchSingle') {
				this.type = 1
				this.setSearchActiveName('searchSingle')
				this.getSearchList(this.keywords)
			} else if (tab.props.name === 'searchSinger') {
				this.type = 100
				this.setSearchActiveName('searchSinger')
				this.searchSinger()
			} else if (tab.props.name === 'searchList') {
				this.type = 1000
				this.setSearchActiveName('searchList')
				this.searchList()
			} else if (tab.props.name === 'searchAlbum') {
				this.type = 10
				this.setSearchActiveName('searchAlbum')
				this.searchAlbum()
			}
		},

		//进入歌单详情
		playlistDetail(row) {
			const songList = toRaw(row)
			this.setSongListId(songList.id)
			sessionStorage.setItem('songListId', songList.id)
			this.$router.push('songList')
		},

		//进入歌手详情
		singerDetail(id) {
			this.setSingerId(id)
			sessionStorage.setItem('singerId', id)
			this.$router.push('singerDetail')
		},

		//进入专辑详情
		albumDetail(id) {
			this.setAlbumId(id)
			sessionStorage.setItem('albumId', id)
			this.$router.push('albumDetail')
		},

		//选择收藏歌单
		selectList(id) {
			const userInfo = JSON.parse(localStorage.getItem('userInfo'))
			if (userInfo !== null) {
				checkMusic(id).then(res => {
					if (res.result) {
						this.collectMusicInfo = res.info
						this.collectDialogVisible = true
						this.$http.get('/songlist/lists', {
							params: {
								id: userInfo.id,
							}
						}).then(res => {
							const data = res.data.data
							this.userSongList = data
						})
					}
				});
			} else {
				this.loginDialogVisible = true
			}
		},

		//收藏
		collect(id) {
			if (this.collectMusicInfo === null) return this.collectDialogVisible = false
			this.$http.post('/songlistdetails/add', {
				listId: id,
				musicId: this.collectMusicInfo.id,
				musicName: this.collectMusicInfo.name,
				singerName: this.collectMusicInfo.ar.map(item => {
					return item.name;
				}),
				album: this.collectMusicInfo.al.name,
				time: getTime(this.collectMusicInfo.dt)
			}).then(res => {
				const data = res.data
				if (data.code === 200) {
					ElMessage.success('收藏成功')
				}
				this.collectDialogVisible = false
			})
		},

		//弹窗跳转登录
		login() {
			this.loginDialogVisible = false
			return location.href = 'login.html'
		},
	},
	setup() {
		return {
			Search,
		}
	}
}
</script>

<style lang="less">
.search-container {
	display: flex;
	flex-direction: column;
	width: 1000px;
	height: calc(100vh - 198px);
	margin: 0 auto;
	box-sizing: border-box;
	padding-top: 10px;

	.search-box {
		margin-bottom: 10px;
	}

	.el-table tr {
		height: 45px;
		cursor: pointer;

		&:hover {
			.btn-list {
				display: block !important;
			}
		}
	}

	.el-tabs {
		height: calc(100% - 52px);

		.el-tabs__content {
			height: calc(100% - 39px);

			.el-tab-pane {
				height: 100%;

				.song-name {
					display: flex;
					align-items: center;

					.name {
						margin-right: auto;
					}

					.btn-list {
						display: none;

						.collect {
							.el-icon {
								width: 20px;
								height: 20px;
								color: #999999;

								&:hover {
									color: #66CCFF
								}

								svg {
									width: 100%;
									height: 100%;
								}
							}
						}
					}
				}

				.singerList {
					display: flex;
					flex-wrap: wrap;
					overflow: auto;
					box-sizing: border-box;
					padding: 0 4px;

					li {
						padding: 0 0 45px 30px;

						.singer {
							cursor: pointer;

							.pic {
								width: 160px;
								height: 160px;
								box-shadow: 1px 1px 5px black;

								img {
									width: 100%;
									height: 100%;
									object-fit: cover;
								}
							}

							.singerInfo {
								width: 160px;
								overflow: hidden;
								white-space: nowrap;
								text-overflow: ellipsis;
							}
						}
					}
				}

				.albumList {
					display: flex;
					flex-wrap: wrap;
					overflow: auto;
					box-sizing: border-box;
					padding: 0 4px;

					li {
						padding: 0 0 45px 30px;

						.album {
							cursor: pointer;

							.pic {
								width: 160px;
								height: 160px;
								box-shadow: 1px 1px 5px black;

								img {
									width: 100%;
									height: 100%;
									object-fit: cover;
								}
							}

							.albumInfo {
								width: 160px;
								overflow: hidden;
								white-space: nowrap;
								text-overflow: ellipsis;
							}
						}
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
</style>