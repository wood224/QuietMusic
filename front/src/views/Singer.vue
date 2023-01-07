<template>
	<div class="singer-wrapper">
		<div class="container">
			<div class="hot-singer">
				<div class="title">
					<div class="text">
						<span>热门歌手</span>
					</div>
				</div>
				<div class="content">
					<div class="item" v-for="item in hotSingerList" :key="item.id">
						<router-link :to="'/singerDetail/' + item.id" class="pic">
							<img :src="item.picUrl" alt="">
						</router-link>
						<router-link :to="'/singerDetail/' + item.id" class="name">
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
import { getTopArtists } from '../http/api'

const hotSingerList = ref([])

getTopArtists().then(res => {
	const data = res.data.artists
	hotSingerList.value = data
})
</script>

<style lang="less" scoped>
.container {
	margin: 0 auto;
	width: 1200px;

	.hot-singer {
		.title {
			display: flex;
			align-items: center;
			height: 40px;
			line-height: 40px;

			.text {
				font-size: 20px;
			}
		}

		.content {
			display: flex;
			flex-wrap: wrap;

			a {
				color: black;
			}

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
			}
		}
	}
}
</style>