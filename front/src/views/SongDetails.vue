<template>
	<div class="song-details-container" ref="SongDetailsContainer">
		<div class="content">
			<div class="left">
				<div class="pic">
					<img :src="songInfo.alPicUrl" alt="" @error="picNull(songInfo)">
				</div>
				<div class="comments-wrapper">
					<div class="comments-container">
						<div class="comment-input">
							<el-input v-model="commentText" :rows="3" type="textarea" placeholder="发表你的评论吧" :maxlength="100"
								:show-word-limit="true" @keydown.enter.prevent="submitComment(commentText)" />
						</div>
						<el-scrollbar class="comments-list">
							<ul>
								<li v-for="item in commentList" :key="item.id">
									<div class="comment">
										<div class="userPic"></div>
										<div class="comment-content">
											<div class="top">
												<div class="name">{{ item.usrName }}：</div>
												<div class="text">{{ item.content }}</div>
											</div>
											<div class="bottom">
												<div class="time">{{ item.createTime }}</div>
												<div class="interact">
													<div class="like" @click="setLike(item.flag, item.id)">
														<i class="fa fa-thumbs-up" v-if="item.flag"></i>
														<i class="fa fa-thumbs-o-up" v-else></i>
														<span>{{ item.agreement }}</span>
													</div>
													<span>|</span>
													<div class="btns">
														<a src="javascript:" class="delete" @click="openDeleteCommentDialog(item.id)"
															v-if="checkComment(item.userId)">删除</a>
														<a src="javascript:" class="reply" v-else>回复</a>
													</div>
												</div>
											</div>
										</div>
									</div>
								</li>
							</ul>
						</el-scrollbar>
					</div>
				</div>
			</div>
			<div class="right">
				<div class="info">
					<div class="title">
						<h3>{{ songInfo.name }}</h3>
					</div>
					<div class="detail">
						<div class="singer">
							<span class="fontColor">歌手：</span>
							<span v-for="(item, index) in songInfo.ar" :key="item.id">
								<span v-if="index !== 0">
									/
								</span>
								<router-link :to="'/singerDetail/' + item.id">{{ item.name }}</router-link>
							</span>
						</div>
						<div class="album">
							<span class="fontColor">专辑：</span>
							<router-link :to="'/albumDetail/' + songInfo.alId">
								{{ songInfo.alName }}
							</router-link>
						</div>
					</div>
				</div>
				<div class="lyric">
					<div class="lyricNull" v-if="lyricLength === 0">暂无歌词</div>
					<ul ref="lyricView">
						<li v-for="(item, key, index) in musicLyric" :key="key" :class="{ currentRow: index == currentRow }">
							{{ item }}
						</li>
					</ul>
				</div>
			</div>
		</div>
		<div class="blurBgMask"></div>
		<div class="blurBg" ref="blurBg"></div>

		<!-- 删除评论确认框 -->
		<el-dialog v-model="dialogVisible" title="提醒" width="30%">
			<span>确认删除该评论吗</span>
			<template #footer>
				<span class="dialog-footer">
					<el-button @click="dialogVisible = false">关闭</el-button>
					<el-button type="danger" @click="deleteComment">
						确认删除
					</el-button>
				</span>
			</template>
		</el-dialog>
	</div>
</template>

<script>
import { getLyricApi } from "../http/api"
import { getTime } from "../fun"
import { mapState, mapMutations } from "vuex"
import { ElMessage } from "element-plus"

export default {
	name: 'SongDetails',
	data() {
		return {
			songInfo: {
				id: -1,
				name: '',
				ar: [],
				url: '',
				duration: '',
				alId: -1,
				alName: '',
				alPicUrl: '',
			},
			userInfo: {},

			//dom对象
			SongDetailsContainer: {},
			lyricView: {},

			blurBg: {},     //模糊背景
			bgi: '',        //背景图片路径
			musicLyric: {},  //音乐歌词

			lyricLength: 0,     //歌词对象长度(属性数量)

			currentRow: 0,     //当前歌词播放行数

			commentList: [],  //评论列表
			commentText: '',  //评论内容

			dialogVisible: false,
			deleteCommentId: -1
		}
	},
	computed: {
		...mapState(['musicInfo', 'lyricCurrent', 'lastKey']),
	},
	created() {
		const info = JSON.parse(localStorage.getItem('songInfo'))
		// console.log(info)
		this.songInfo.id = info.id
		this.songInfo.name = info.name
		this.songInfo.ar = info.ar
		this.songInfo.duration = getTime(info.dt)
		this.songInfo.alId = info.alId
		this.songInfo.alName = info.alName
		this.songInfo.alPicUrl = info.alPicUrl

		this.userInfo = JSON.parse(localStorage.getItem('userInfo'))
		this.getLyric()
		this.getMusicComments(this.songInfo.id)

	},
	mounted() {
		this.lyricView = this.$refs.lyricView
		this.SongDetailsContainer = this.$refs.SongDetailsContainer
		this.blurBg = this.$refs.blurBg
		const songInfo = JSON.parse(localStorage.getItem('songInfo'))
		if (songInfo !== null)
			if (songInfo.picUrl !== '' && songInfo.id !== -1)
				this.bgi = songInfo.alPicUrl
	},
	watch: {
		bgi: {
			handler() {
				this.blurBg.style.backgroundImage = `url("${this.bgi}")`
			},
		},
		lyricCurrent: {
			handler() {
				Object.keys(this.musicLyric).forEach((key, index) => {
					if (parseFloat(this.lyricCurrent) >= parseFloat(this.lastKey) && parseFloat(this.lyricCurrent) < parseFloat(key)) {
						this.currentRow = index - 1
						if (index >= 5) {
							this.lyricView.style.top = -30 * (index - 5) + 'px'
						} else {
							this.lyricView.style.top = 0
						}
					} else if (this.lyricCurrent >= parseFloat(key)) {
						this.currentRow = index
						this.lyricView.style.top = -30 * (index - 5) + 'px'
					}
					this.setLastKey(parseFloat(key).toFixed(2))
				})
			}
		},
		musicInfo: {
			handler() {
				const info = this.musicInfo
				// console.log(info)
				this.songInfo.id = info.id
				this.songInfo.name = info.name
				this.songInfo.ar = info.ar
				this.songInfo.duration = getTime(info.dt)
				this.songInfo.alId = info.al.id
				this.songInfo.alName = info.al.name
				this.songInfo.alPicUrl = info.al.picUrl
				this.bgi = this.songInfo.alPicUrl
				this.getLyric()
				this.getMusicComments(this.songInfo.id)
			}
		},
		// songInfo: {
		// 	handler() {
		// 		// console.log('ok');
		// 		this.bgi = songInfo.alPicUrl
		// 		
		// 	}
		// }
	},
	methods: {
		...mapMutations(['setLastKey']),

		//获取歌词
		getLyric() {
			if (this.songInfo.id === -1) return
			getLyricApi(this.songInfo.id)
				.then(res => {
					let lyricArr = res.data.lrc.lyric.split('[').slice(1)
					let lrcObj = {}
					lyricArr.forEach(item => {
						let arr = item.split(']')
						let m = parseFloat(arr[0].split(':')[0])
						let s = parseFloat(arr[0].split(':')[1])
						arr[0] = parseFloat(m * 60 + s).toFixed(2)
						if (arr[1] !== '\n') {        //去除换行符
							lrcObj[arr[0]] = arr[1]
						}
					})
					this.musicLyric = lrcObj
					this.lyricLength = Object.keys(this.musicLyric).length
				})
		},

		//发表评论
		submitComment(text, replyId = -1) {
			if (this.userInfo === null) {
				return ElMessage.warning('请先登录');
			}
			this.$http.post('/comment/publish', {
				musicId: this.songInfo.id,
				userId: this.userInfo.id,
				content: text,
				replyId: replyId === -1 ? null : replyId
			}).then(res => {
				if (res.data.code === 200) {
					this.getMusicComments(this.songInfo.id)
					ElMessage.success('评论成功！');
					this.commentText = ''
				}
			})
		},

		//获取音乐评论
		getMusicComments(id) {
			this.$http.get('/comment/byMusic', {
				params: {
					id: id,
					userId: this.userInfo === null ? null : this.userInfo.id,
				}
			}).then(res => {
				const data = res.data.data
				this.commentList = data
			})
		},

		//判断评论是否为自己的
		checkComment(id) {
			if (id === this.userInfo.id) {
				return true
			}
			return false
		},

		//点赞 or 取消点赞
		setLike(flag, commentId) {
			if (flag) {
				this.$http.delete('/agreement/unagree', {
					userId: this.userInfo.id,
					commentId: commentId
				}).then(res => {
					getMusicComments(this.songInfo.id)
				})
			} else {
				this.$http.post('/agreement/agree', {
					userId: this.userInfo.id,
					commentId: commentId
				}).then(res => {
					this.getMusicComments(this.songInfo.id)
				})
			}
		},

		//打开删除确认弹窗
		openDeleteCommentDialog(id) {
			this.deleteCommentId = id
			this.dialogVisible = true
		},

		//删除评论
		deleteComment() {
			this.$http.get('/comment/delete', {
				params: {
					id: this.deleteCommentId,
				}
			}).then(res => {
				if (res.data.code === 200) {
					this.getMusicComments(this.songInfo.id)
					ElMessage('删除成功！');
					this.dialogVisible = false
				}
			})
		},

		//光盘图片为空时显示默认图片
		picNull(item) {
			item.picUrl = require('@/assets/DefaultCD.png')
		},
	},
}
</script>

<style lang="less" scoped>
.song-details-container {
	display: flex;
	justify-content: center;
	position: relative;
	height: calc(100vh - 198px);
	overflow: hidden;

	.content {
		display: flex;
		// position: absolute;
		width: 1000px;
		height: 100%;
		box-sizing: border-box;
		padding-top: 20px;
		z-index: 100;
		color: white;

		.left {
			display: flex;
			flex-direction: column;
			align-items: center;
			width: 50%;
			height: 100%;

			.pic {
				width: 300px;
				padding: 0 40px 20px;

				img {
					width: 100%;
				}
			}

			.comments-wrapper {
				width: 100%;
				height: calc(100% - 260px);
				box-sizing: border-box;
				padding: 10px 20px 0;
				color: black;

				.comments-container {
					display: flex;
					height: 100%;
					flex-direction: column;
					background-color: white;
					border: 1px solid gray;
					border-radius: 5px;


					.comment-input {
						::v-deep(.el-textarea) {

							.el-input__count {
								background-color: transparent;
								right: 30px;
								color: #66CCFF;
							}
						}
					}

					.comments-list {
						flex: 1;
						box-sizing: border-box;
						padding-top: 20px;

						ul {
							width: 100%;
							box-sizing: border-box;
							background-color: white;
							// border-left: 1px solid gray;
							// border-right: 1px solid gray;
							padding: 10px;

							li {
								width: 100%;

								// background-color: rgba(87, 87, 87, 0.5);
								.comment {
									box-sizing: border-box;
									padding: 15px 0;
									border-top: 1px solid #ccc;

									.comment-content {
										.top {
											display: flex;

											.name {
												margin-right: 5px;
												color: #0c73c2;
											}

											.text {
												color: black;
											}
										}

										.bottom {
											margin-top: 15px;
											display: flex;
											justify-content: space-between;
											align-items: center;
											box-sizing: border-box;
											padding-right: 10px;

											.time {
												color: #999;
												font-size: 14px;
											}

											.interact {
												display: flex;
												justify-content: space-between;
												width: 100px;

												.like {
													cursor: pointer;

													.fa {
														color: #1175BB;
													}
												}

												.btns {
													cursor: pointer;

													.delete {
														color: red;
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}

		.right {
			display: flex;
			flex-direction: column;
			width: 50%;

			.info {
				padding: 0 10px;
				border-radius: 10px;
				background-color: rgba(87, 87, 87, 0.5);

				.title {
					width: 100%;
					overflow: hidden;
					white-space: nowrap;
					text-overflow: ellipsis;
					margin-top: 1vw;
					text-align: center;
				}

				.detail {
					display: flex;
					justify-content: space-between;
					margin-top: 20px;
					margin-bottom: 20px;

					.singer {
						overflow: hidden;
						white-space: nowrap;
						text-overflow: ellipsis;

						a {
							color: white;
						}
					}

					.album {
						overflow: hidden;
						white-space: nowrap;
						text-overflow: ellipsis;

						a {
							color: white;
						}
					}

					.fontColor {
						color: #ffffff94;
					}

				}
			}

			.lyric {
				position: relative;
				margin-top: 20px;
				height: 54vh;
				overflow: hidden;
				padding: 0 10px;
				border-radius: 10px;
				background-color: rgba(87, 87, 87, 0.5);

				.lyricNull {
					margin: auto 0;
					text-align: center;
					font-size: x-large;
				}

				ul {
					width: 100%;
					position: absolute;
					transition: 0.3s;

					li {
						height: 25px;
						margin: 5px 0;
						line-height: 25px;
						text-align: center;
					}

					.currentRow {
						font-size: 20px;
						color: skyblue;
					}
				}

			}
		}
	}

	.blurBgMask {
		height: 100%;
		background-color: rgba(0, 0, 0, 0.7);
		z-index: 10;
	}

	.blurBg {
		position: absolute;
		top: 0;
		left: 0;
		background-repeat: no-repeat;
		background-size: cover;
		width: 100%;
		height: 100%;
		filter: blur(20px);
		opacity: 0.6;
		z-index: 9;
	}
}
</style>