<template>
	<nav class="navbar navbar-light bg-primary">
		<div class="menu">
			<ul>
				<li id="home" @click="goHome" :class="{ bgWihte: classBgWihte[0] }">
					<router-link to="/home">首页</router-link>
				</li>
				<li id="rank" @click="goRank" :class="{ bgWihte: classBgWihte[1] }">
					<router-link to="/rank">榜单</router-link>
				</li>
				<li id="song" @click="goSong" :class="{ bgWihte: classBgWihte[2] }">
					<router-link to="/song">歌单</router-link>
				</li>
				<li id="singer" @click="goSinger" :class="{ bgWihte: classBgWihte[3] }">
					<router-link to="/singer">歌手</router-link>
				</li>
			</ul>
		</div>
		<div class="search">
			<el-icon @click="search">
				<Search />
			</el-icon>
			<div class="ipt">
				<el-input v-model="input" placeholder="搜索歌曲, 歌手..." @keyup.enter="search" @keyup.esc="input = ''" />
			</div>
		</div>
		<div class="user">
			<div v-if="!userInfo.hasOwnProperty('name')" class="login">
				<a href="login.html">登录</a>
			</div>
			<div v-else class="userInfo">
				<el-dropdown size="large">
					<div class="img">
						<img :src="userInfo.img === null ? url : userImg" alt="" />
					</div>
					<template #dropdown>
						<el-dropdown-menu>
							<el-dropdown-item @click="goUserHome">个人主页</el-dropdown-item>
							<!-- <el-dropdown-item>菜单 2</el-dropdown-item>
							<el-dropdown-item>菜单 3</el-dropdown-item> -->
							<el-dropdown-item divided @click="logout()"><span style="color:red">退出登录</span></el-dropdown-item>
						</el-dropdown-menu>
					</template>
				</el-dropdown>
			</div>
		</div>
	</nav>
</template>

<script>
import { mapMutations, mapState } from "vuex"
import { getHash } from "../fun"

export default {
	name: 'Header',
	data() {
		return {
			url: 'https://fuss10.elemecdn.com/e/5d/4a731a90594a4af544c0c25941171jpeg.jpeg',
			classBgWihte: [false, false, false, false],
			input: '',
			userImg: ''
		}
	},
	created() {
		//判断登录状态
		if (localStorage.getItem('userInfo') !== null) {
			const id = JSON.parse(localStorage.getItem('userInfo')).id
			this.getUserDetail(id)
		}
	},
	computed: {
		...mapState(['searchSongs', 'searchKeywords', 'userInfo']),
	},
	watch: {
		searchKeywords: {
			handler() {
				this.input = this.searchKeywords
			}
		},

		userInfo: {
			handler() {
				this.getUserDetail(this.userInfo.id)
			},
		},

		$route(to) {
			if (to.path === '/home') {
				for (let i = 0; i < this.classBgWihte.length; i++) {
					if (i === 0) {
						this.classBgWihte[i] = true
					} else {
						this.classBgWihte[i] = false
					}
				}
			}
			else if (to.path === '/rank') {
				for (let i = 0; i < this.classBgWihte.length; i++) {
					if (i === 1) {
						this.classBgWihte[i] = true
					} else {
						this.classBgWihte[i] = false
					}
				}
			}
			else if (to.path === '/song') {
				for (let i = 0; i < this.classBgWihte.length; i++) {
					if (i === 2) {
						this.classBgWihte[i] = true
					} else {
						this.classBgWihte[i] = false
					}
				}
			}
			else if (to.path === '/singer') {
				for (let i = 0; i < this.classBgWihte.length; i++) {
					if (i === 3) {
						this.classBgWihte[i] = true
					} else {
						this.classBgWihte[i] = false
					}
				}
			}
			else {
				for (let i = 0; i < this.classBgWihte.length; i++) {
					this.classBgWihte[i] = false
				}
			}
		}
	},
	methods: {
		...mapMutations(['setSearchKeywords', 'setUserInfo']),

		logout() {
			localStorage.removeItem('userInfo')
			//返回主页
			if ((getHash().slice(0, 9)) === '/userHome') return this.$router.go(-1)
			location.reload()
		},

		goHome() {
			this.$router.push('/')
		},
		goRank() {
			this.$router.push('/rank')
		},
		goSong() {
			this.$router.push('/song')
		},
		goSinger() {
			this.$router.push('/singer')
		},
		goUserHome() {
			this.$router.push('/userHome/?id=' + this.userInfo.id)
		},

		async search() {
			if (this.input === '') return
			this.setSearchKeywords(this.input)
			this.$router.push('/search')
		},

		//更新用户详情信息
		getUserDetail(id) {
			this.$http.get(`/user/${id}`)
				.then(async res => {
					let userInfo = {
						name: res.data.data.name,
						id: res.data.data.id,
						sex: res.data.data.sex,
						phone: res.data.data.phone,
						description: res.data.data.description,
						img: res.data.data.img
					}
					//将表示登录状态的对象存入 localstorage 和 vuex 中
					localStorage.setItem("userInfo", JSON.stringify(userInfo))
					this.setUserInfo(userInfo)

					this.userImg = await this.$fun.getImg(this.userInfo.img)
				}).catch((err) => {
					console.error(err)
				})
		},
	},
}
</script>

<style lang="less">
.navbar.navbar-light {
	z-index: 999;
	min-width: 1000px;

	.menu {
		ul {
			display: flex;
			justify-content: space-around;
			align-items: center;
			height: 100%;
			margin-top: 1rem;

			.bgWihte {
				background-color: #00A9FF;
				border-radius: 30px;

				&:hover {
					a {
						color: white;
					}
				}

				a {
					color: white;
				}
			}

			li {
				width: 150px;
				height: 50px;
				text-align: center;
				cursor: pointer;

				&:hover {
					a {
						color: white;
						text-decoration: none;
					}

				}

				a {
					font-size: 24px;
					line-height: 50px;
					color: black;
				}
			}
		}
	}

	.search {
		display: flex;
		// background-color: #6DD5FA;
		// height: 50px;
		align-items: center;

		.el-icon {
			font-size: 28px;
			cursor: pointer;
		}
	}

	.user {
		position: relative;
		display: flex;
		height: 100%;
		align-items: center;
		user-select: none;
		cursor: pointer;

		&:hover {
			.userMenu {
				display: block;
			}
		}

		.login {
			display: flex;
			background-color: white;
			border-radius: 30px;
			width: 100px;
			height: 50%;
			align-items: center;
			text-align: center;

			a {
				width: 100%;
				color: #000046;
				text-decoration: none;
			}
		}

		.userInfo {
			margin-right: 40px;
			width: 40px;
			height: 40px;

			.el-dropdown {
				width: 100%;
				height: 100%;

				.img {
					width: 100%;
					height: 100%;
					border-radius: 50%;

					img {
						border-radius: 50%;
						width: 100%;
						height: 100%;
					}
				}
			}
		}
	}
}

.el-dropdown-menu--large {
	.el-dropdown-menu__item {
		font-size: 16px;
	}
}
</style>