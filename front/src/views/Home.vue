<template>
	<div class="home-wrapper">
		<Banner></Banner>
		<div class="wrapper-container">
			<div class="hot-song-list">
				<div class="title">
					<div class="circle"></div>
					<div class="text">
						<span>热门歌单</span>
					</div>
				</div>
				<div class="content">
					<div class="item" v-for="item in hotSongList" :key="item.id">
						<div class="pic">
							<img :src="item.coverImgUrl" alt="">
						</div>
						<div class="name">
							<span>{{ item.name }}</span>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</template>

<script setup>
import { ref } from 'vue';
import { getFinePlaylist } from "../http/api";
import Banner from '../components/Banner.vue'

const hotSongList = ref([])
getFinePlaylist().then(res => {
	const data = res.data
	hotSongList.value = data.playlists

})

</script>

<style lang="less" scoped>
.home-wrapper {
	.wrapper-container {
		margin: 0 auto;
		width: 1200px;

		.hot-song-list {
			.title {
				display: flex;
				align-items: center;
				height: 40px;
				line-height: 40px;

				.circle {
					margin-right: 14px;
					display: flex;
					justify-content: center;
					align-items: center;
					position: relative;
					width: 20px;
					height: 20px;
					border-radius: 50%;
					background-color: #66CCFF;

					&::before {
						content: '';
						display: block;
						width: 6px;
						height: 6px;
						border-radius: 50%;
						background-color: #fff;
					}
				}

				.text {
					font-size: 20px;
				}
			}

			.content {
				display: flex;
				flex-wrap: wrap;

				.item {
					width: 20%;
					padding: 0 12px 20px 0;
					box-sizing: border-box;
					cursor: pointer;

					.pic {
						width: 100%;

						img {
							width: 100%;
							object-fit: contain;
						}
					}

					// .name {
					// 	width: 100%;
					// 	overflow: hidden;
					// 	white-space: nowrap;
					// 	text-overflow: ellipsis;
					// }
				}
			}
		}
	}
}
</style>