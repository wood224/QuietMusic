<template>
	<div class="rank-wrapper">
		<div class="left">
			<div class="left-menu" :class="{ checked: index === checkedIndex }" v-for="(item, index) in leftMenu" :key="index"
				@click="getRank(index)">
				<div class="icon" :style="{ backgroundColor: item.bgColor }">
					<span class="icon-text">{{ item.name }}</span>
				</div>
				<div class="name"><span>{{ item.name }}</span></div>
			</div>
		</div>
		<div class="right">
			<SongsList :list="rankList" type="top"></SongsList>
		</div>
	</div>
</template>

<script setup>
import { ref, getCurrentInstance } from 'vue';
import SongsList from '../components/SongsList.vue';

const { proxy } = getCurrentInstance()

const leftMenu = ref([
	{
		name: '总榜单',
		bgColor: '#ED86AF',
		url: '/music/rank',
	},
	{
		name: '昨日榜',
		bgColor: '#82D7C4',
		url: '/music/oneRank',
	},
	{
		name: '七日榜',
		bgColor: '#75C2EA',
		url: '/music/sevenRank',
	},
	{
		name: '三十日榜',
		bgColor: '#ED8E7C',
		url: '/music/thirtyRank'
	},
])

const rankList = ref([])
const checkedIndex = ref(0)
const getRank = (index) => {
	checkedIndex.value = index
	proxy.$http.post(leftMenu.value[index].url).then(res => {
		const data = res.data.data
		rankList.value = data
	})
}
getRank(0)


</script>

<style lang="less" scoped>
.rank-wrapper {
	display: flex;
	width: 1200px;
	margin: 0 auto;
	box-sizing: border-box;

	.left {
		width: 300px;
		box-sizing: border-box;
		padding: 20px 0;
		border: 1px #D9D9D9 solid;

		.left-menu {
			padding: 20px 30px;
			display: flex;
			align-items: center;
			width: 100%;
			cursor: pointer;

			&.checked {
				background-color: #e6e6e6;

				&:hover {
					background-color: #e6e6e6;
				}
			}

			&:hover {
				background-color: #f4f2f2;
			}

			.icon {
				overflow: hidden;
				margin-right: 20px;
				width: 70px;
				height: 70px;
				line-height: 70px;
				text-align: center;

				.icon-text {
					font-size: 20px;
					font-weight: bold;
					color: white;
				}
			}

			.name {
				font-size: 18px;
			}
		}
	}

	.right {
		width: calc(1200px - 300px);
	}
}
</style>