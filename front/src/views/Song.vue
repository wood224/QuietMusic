<template>
	<div class="songlist-wrapper">
		<div class="title">
			<h1>精品歌单</h1>
		</div>
		<div class="container">
			<div class="menu" v-for="(menu, index) in menuList" :key="index">
				<h3>{{ menu.name }}</h3>
				<div class="list">
					<div class="item" v-for="item in menu.songlists" :key="item.id">
						<router-link :to="'/songList/' + item.id" class="pic">
							<img :src="item.coverImgUrl" alt="">
						</router-link>
						<router-link :to="'/songList/' + item.id" class="name">
							<span>{{ item.name }}</span>
						</router-link>
					</div>
				</div>
			</div>
		</div>
	</div>
</template>

<script setup>
import { ref } from 'vue';
import { getFinePlaylist } from '../http/api'

const menuList = ref([
	{
		name: '华语',
		songlists: [],
	},
	{
		name: '古风',
		songlists: [],
	},
	{
		name: '欧美',
		songlists: [],
	},
	{
		name: '流行',
		songlists: [],
	}
])

for (let i in menuList.value) {
	const item = menuList.value[i]
	getFinePlaylist(item.name).then(res => {
		const data = res.data.playlists
		item.songlists.push(...data)
	})
}


</script>

<style lang="less" scoped>
.songlist-wrapper {
	width: 1200px;
	margin: 0 auto;
	box-sizing: border-box;
	padding-top: 20px;

	.title {
		box-sizing: border-box;
		border-bottom: 2px #66CCFF solid;
	}

	.container {
		.menu {
			margin-top: 10px;

			h3 {
				line-height: 40px;
				border-bottom: 1px #66CCFF solid;
			}

			.list {
				display: flex;
				flex-wrap: wrap;

				.item {
					width: 20%;
					padding: 0 12px 20px 0;
					box-sizing: border-box;
					cursor: pointer;

					a {
						color: inherit;
					}

					.pic {
						width: 100%;

						img {
							width: 100%;
							object-fit: contain;
						}
					}
				}
			}
		}
	}
}
</style>