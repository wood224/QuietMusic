<template>
	<UpdateUserInfo v-show="updateView" @setUpdateView="setUpdateView" @getUserDetail="getUserDetail">
	</UpdateUserInfo>
	<CreateSongList v-show="createView" @setCreateView="setCreateView" @getUserSongList="getUserSongList"
		:user-id="userDetail.id"></CreateSongList>
	<div class="user-home-container">
		<div class="userInfo">
			<div class="img">
				<el-image :src="userDetail.img === null ? url : userDetail.img" fit="cover">
					<template #error>
						<div class="image-slot">
							<el-icon>
								<Picture />
							</el-icon>
						</div>
					</template>
				</el-image>
			</div>
			<el-descriptions class="info" :title="userDetail.name" :column="3" border>
				<template #extra>
					<el-button type="primary" @click="setUpdateView">编辑个人资料</el-button>
				</template>
				<el-descriptions-item width="100px">
					<template #label>
						<div class="cell-item">
							<el-icon>
								<user />
							</el-icon>
							账号
						</div>
					</template>
					{{ userDetail.username }}
				</el-descriptions-item>
				<el-descriptions-item>
					<template #label>
						<div class="cell-item">
							<el-icon>
								<iphone />
							</el-icon>
							电话号码
						</div>
					</template>
					{{ userDetail.phone }}
				</el-descriptions-item>
				<el-descriptions-item>
					<template #label>
						<div class="cell-item">
							<el-icon>
								<UserFilled />
							</el-icon>
							性别
						</div>
					</template>
					<!-- <span v-if="userDetail.sex === 0">保密</span> -->
					<el-icon size="17px" v-if="userDetail.sex === 0">
						<Remove />密
					</el-icon>
					<el-icon color="hotpink" size="17px" v-else-if="userDetail.sex === 2">
						<Female />
					</el-icon>
					<el-icon color="skyblue" size="17px" v-else-if="userDetail.sex === 1">
						<Male />
					</el-icon>
				</el-descriptions-item>
				<el-descriptions-item>
					<template #label>
						<div class="cell-item">
							<el-icon>
								<ChatLineSquare />
							</el-icon>
							简介
						</div>
					</template>
					<span v-if="userDetail.description === null || userDetail.description === ''">这个人很懒，没有留下任何内容</span>
					<span v-else>{{ userDetail.description }}</span>
				</el-descriptions-item>
			</el-descriptions>
		</div>

		<div class="song-list">
			<div class="title">
				<h4>我的歌单</h4>
				<div class="create">
					<el-button type="primary" plain @click="setCreateView">
						<i class="fa fa-plus"></i>新建歌单
					</el-button>
				</div>
			</div>
			<div class="songs">
				<ul>
					<li v-for="(item, index) in userSongList" :key="item.id">
						<div class="list">
							<div class="more">
								<div class="btn" @click.stop="moreIndex = index" @mouseenter="moreIndex = index"
									@mouseleave="moreIndex = -1">
									<el-icon>
										<More />
									</el-icon>
								</div>
								<div class="menu" @mouseenter="moreIndex = index" @mouseleave="moreIndex = -1"
									v-show="moreIndex === index">
									<ul>
										<li>
											<router-link :to="'/songListUser/' + item.id">
												<i class="fa fa-folder-open-o"></i>
												打开歌单
											</router-link>
										</li>
										<li><i class="fa fa-edit"></i> 编辑歌单</li>
										<li @click.stop="centerDialogVisible = true"><i class="fa fa-trash-o"></i> 删除歌单</li>
									</ul>
								</div>
								<el-dialog v-model="centerDialogVisible" title="删除歌单" width="30%" center>
									<span>确定删除歌单 <span style="font-weight: bold;">{{ item.name }}</span> 吗？</span>
									<template #footer>
										<span class="dialog-footer">
											<el-button @click="centerDialogVisible = false">取消</el-button>
											<el-button type="danger" @click="deleteSongList(item.id)">确认删除</el-button>
										</span>
									</template>
								</el-dialog>
							</div>
							<router-link :to="'/songListUser/' + item.id" class="pic">
								<img :src="item.coverImg" alt="暂无封面">
							</router-link>
							<router-link :to="'/songListUser/' + item.id" class="list-name">
								<span>{{ item.name }}</span>
							</router-link>
						</div>
					</li>
				</ul>
			</div>
		</div>
	</div>
</template>

<script>
import { getCurrentInstance } from "vue";
import { mapState, mapMutations } from "vuex"
import UpdateUserInfo from '../components/UpdateUserInfo.vue'
import CreateSongList from '../components/CreateSongList.vue'

export default {
	name: 'UserHome',
	data() {
		return {
			userDetail: {},
			userSongList: [],
			updateView: false,
			createView: false,
			url: 'https://fuss10.elemecdn.com/e/5d/4a731a90594a4af544c0c25941171jpeg.jpeg',
			moreIndex: -1,
			centerDialogVisible: false,
		}
	},
	computed: {
	},
	created() {
		this.getUserDetail()
		this.getUserSongList()
	},
	methods: {
		...mapMutations(['setUserInfo', 'setUserSongListId']),

		getUserDetail() {
			const userId = JSON.parse(localStorage.getItem('userInfo')).id
			this.$http.get(`/user/${userId}`)
				.then(async res => {
					let userInfo = {
						name: res.data.data.name,
						id: res.data.data.id,
						sex: res.data.data.sex,
						phone: res.data.data.phone,
						description: res.data.data.description,
						img: res.data.data.img,
					}
					const { img, ...data } = res.data.data;
					this.userDetail = data;
					this.userDetail.img = await this.$fun.getImg(img)
					//将表示登录状态的对象存入 localstorage 和 vuex 中
					localStorage.setItem("userInfo", JSON.stringify(userInfo))
					this.setUserInfo(userInfo)
				}).catch((err) => {
					console.error(err)
				})
		},

		//获取用户歌单
		getUserSongList() {
			const userId = JSON.parse(localStorage.getItem('userInfo')).id
			this.$http.get('/songlist/lists', {
				params: {
					id: userId
				}
			})
				.then(res => {
					this.userSongList = res.data.data
				})
		},

		//获取用户歌单详情
		userSongListDetail(id) {
			this.setUserSongListId(id)
			sessionStorage.setItem('userSongListId', id)
			this.$router.push('/songListUser')
		},

		//删除歌单
		deleteSongList(id) {
			this.centerDialogVisible = false
			this.$http.delete('/songlist/delete', {
				params: {
					id: id
				}
			})
				.then(res => {
					this.getUserSongList()
				})
		},

		setUpdateView() {
			this.updateView = !this.updateView
		},

		setCreateView() {
			this.createView = !this.createView
		},
	},
	components: {
		UpdateUserInfo,
		CreateSongList
	}
}
</script>

<style lang="less">
.user-home-container {
	width: 1000px;
	margin: 0 auto;

	.userInfo {
		display: flex;
		margin-top: 10px;
		box-shadow: 4px 4px 15px;

		.img {
			position: relative;
			width: 200px;
			height: 200px;
			padding: 20px;

			.el-image {
				width: 100%;
				height: 100%;

				.image-slot {
					display: flex;
					justify-content: center;
					align-items: center;
					width: 100%;
					height: 100%;
					background: var(--el-fill-color-light);
					color: var(--el-text-color-secondary);
					font-size: 30px;

					.el-icon {
						font-size: 30px;
						color: black;
					}
				}
			}
		}

		.info {
			width: 800px;
			padding: 20px 0;
			padding-right: 10px;
		}
	}

	.song-list {
		margin-top: 40px;

		.title {
			display: flex;
			justify-content: space-between;
			border-bottom: 1px solid gray;

			.create {
				i {
					line-height: 20px;
				}
			}
		}

		.songs {
			margin-top: 10px;

			ul {
				display: flex;
				flex-wrap: wrap;
				justify-content: flex-start;

				li {
					margin-right: 40px;

					a {
						display: block;
					}

					.list {
						position: relative;
						cursor: pointer;
						color: black;

						&:hover {
							.more {
								display: block;
							}
						}

						.more {
							display: none;

							.btn {
								position: absolute;
								right: 0;
								padding: 0 4px;
								border-radius: 5px;

								&:hover {
									background-color: rgba(255, 255, 255, 0.8);
								}
							}

							.menu {
								position: absolute;
								left: 100%;
								width: 120px;
								z-index: 10;
								border: 1px solid rgb(177, 177, 177);
								border-radius: 8px;
								background-color: rgba(255, 255, 255);
								overflow: hidden;

								li {
									margin: 0;
									padding: 4px 10px;
									box-sizing: border-box;
									width: 100%;
									text-align: center;

									i {
										width: 16px;
										height: 16px;
										margin-right: 2px;
									}

									&:hover {
										background-color: rgba(100, 176, 255, 0.6);
									}

									&:nth-child(2) {
										color: rgba(0, 0, 0, 0.2);
										cursor: not-allowed;
									}

									&:last-child {
										margin-top: 6px;
										border-top: 1px solid gray;
										color: red;
									}
								}
							}
						}

						.pic {
							width: 100px;
							height: 100px;

							img {
								width: 100%;
								height: 100%;
							}
						}

						.list-name {
							color: black;
						}
					}
				}
			}
		}
	}
}
</style>