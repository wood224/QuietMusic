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
							<el-input ref="comment" @input="commentInput" v-model="commentText" :rows="3" type="textarea"
								placeholder="发表你的评论吧" :maxlength="100" :show-word-limit="true"
								@keydown.enter.prevent="submitComment(commentText, replyId)" />
						</div>
						<el-scrollbar class="comments-list">
							<div class="tabs">
								<div class="tab" :class="{ checked: actionType === item.type }" v-for="item in tabs"
									@click="setType(item.type)">
									<span>{{ item.name }}</span>
								</div>
							</div>
							<ul>
								<li v-for="item in commentList" :key="item.id">
									<div class="comment">
										<div class="userPic">
											<img :src="getImg(item.img)" alt="">
										</div>
										<div class="comment-content">
											<div class="top">
												<div class="user-comment">
													<div class="name">{{ item.usrName }}：</div>
													<div class="text">{{ item.content }}</div>
												</div>
												<div class="reply-content" v-if="item.replyContent !== null">
													<div class="name">{{ item.replyUserName }}：</div>
													<div class="text">{{ item.replyContent }}</div>
												</div>
											</div>
											<div class="bottom">
												<div class="time">{{ item.createTime }}</div>
												<div class="interact">
													<div class="like" @click="setLike(item)">
														<i class="fa fa-thumbs-up" v-if="item.flag"></i>
														<i class="fa fa-thumbs-o-up" v-else></i>
														<span>{{ item.agreement }}</span>
													</div>
													<span>|</span>
													<div class="btns">
														<a src="javascript:" class="delete" @click="openDeleteCommentDialog(item.id)"
															v-if="checkComment(item.userId)">删除</a>
														<a src="javascript:" class="reply" @click="reply(item.id, item.usrName)" v-else>回复</a>
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

			tabs: [
				{
					name: '最热',
					type: 2,
				},
				{
					name: '最新',
					type: 1,
				}
			],
			actionType: 2,

			commentList: [],  //评论列表
			commentText: '',  //评论内容
			replyId: -1, 			//回复评论的评论id
			replyName: '',    //回复评论的名字

			dialogVisible: false,
			deleteCommentId: -1
		}
	},
	computed: {
		...mapState(['musicInfo', 'userBaseImg', 'lyricCurrent', 'lastKey']),
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
		this.getMusicComments(this.songInfo.id, this.actionType)

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
				this.getMusicComments(this.songInfo.id, this.actionType)
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

		//输入框内容改变时
		commentInput() {
			if (this.replyId !== -1) {	//回复状态
				if (this.commentText.slice(1, this.replyName.length + 2) !== this.replyName + '：') {
					this.replayId = -1
					this.replyName = ''
					if (this.commentText.length < this.replyName.length + 2) {
						this.commentText = ''
					}
				}
			} else {		//非回复状态

			}
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
					ElMessage.success('评论成功！');
					this.commentText = ''
					this.getMusicComments(this.songInfo.id, this.actionType,)
				} else if (res.data.code === 410) {
					ElMessage.warning('评论过于频繁')
				}
			})
		},

		//设置排序方式
		setType(type) {
			this.actionType = type
			this.getMusicComments(this.songInfo.id, this.actionType)
		},

		//获取音乐评论
		getMusicComments(id, type) {
			this.$http.get('/comment/byMusic', {
				params: {
					id: id,
					userId: this.userInfo === null ? null : this.userInfo.id,
					order: type,
				}
			}).then(async res => {
				this.commentList = []
				const data = res.data.data
				const List = data.map(async item => {
					item.img = item.img === null ? null : await this.$fun.getImg(item.img)
					return {
						...item
					}
				})
				for (let i in List) {
					const comment = await List[i];
					this.commentList.push(comment);
				}
			})
		},

		//获取用户头像
		getImg(img) {
			return img === null ? this.userBaseImg : img
		},

		//判断评论是否为自己的
		checkComment(id) {
			if (id === this.userInfo.id) {
				return true
			}
			return false
		},

		//点赞 or 取消点赞
		setLike(commentInfo) {
			if (commentInfo.flag) {		//如果已点赞
				this.$http.delete('/agreement/unagree', {
					data: {
						userId: this.userInfo.id,
						commentId: commentInfo.id
					}
				}).then(res => {
					if (res.data.code === 200) {
						commentInfo.flag = false
						commentInfo.agreement--
					} else {
						ElMessage.error('出现未知错误，请稍后再试');
					}
				})
			} else {		//如果未点赞
				this.$http.post('/agreement/agree', {
					userId: this.userInfo.id,
					commentId: commentInfo.id
				}).then(res => {
					if (res.data.code === 200) {
						// this.getMusicComments(this.songInfo.id);
						commentInfo.flag = true
						commentInfo.agreement++
					}
					else {
						ElMessage.error('出现未知错误，请稍后再试');
					}
				})
			}
		},

		//回复评论
		reply(id, userName) {
			this.replyId = id;
			this.replyName = userName;
			this.commentText = `@${this.replyName}：`;
			this.$refs.comment.focus();
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
					this.getMusicComments(this.songInfo.id, this.actionType,)
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

						.tabs {
							display: flex;
							width: 100%;
							height: 50px;
							box-sizing: border-box;
							padding-left: 20px;
							text-align: center;
							line-height: 50px;

							.tab {
								margin-right: 20px;
								width: 50px;
								font-size: 18px;
								box-sizing: border-box;
								cursor: pointer;

								&:hover {
									color: var(--el-color-primary);
								}

								&.checked {
									color: var(--el-color-primary);
									border-bottom: 2px solid var(--el-color-primary);
								}
							}
						}

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
									display: flex;
									// align-items: center;
									box-sizing: border-box;
									padding: 15px 0;
									border-top: 1px solid #ccc;

									.userPic {
										margin-right: 10px;
										width: 50px;
										height: 50px;

										img {
											width: 100%;
											height: 100%;
										}
									}

									.comment-content {
										flex: 1;

										.top {
											.name {
												margin-right: 5px;
												color: #0c73c2;
											}

											.text {
												color: black;
											}

											.user-comment {
												display: flex;

												.name {
													margin-right: 5px;
													color: #0c73c2;
												}

												.text {
													color: black;
												}
											}

											.reply-content {
												display: flex;
												padding: 8px 19px;
												margin-top: 10px;
												line-height: 20px;
												background: #f4f4f4;
												border: 1px solid #dedede;
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
						min-height: 25px;
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