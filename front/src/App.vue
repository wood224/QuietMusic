<template>
	<div class="app">
		<Header></Header>
		<Main></Main>
		<MusicPlayer></MusicPlayer>
	</div>
</template>

<script>
import Header from './components/Header.vue'
import Main from './components/Main.vue'
import MusicPlayer from './components/MusicPlayer.vue'

export default {
	name: 'App',
	created() {

	},
	methods: {
		getHash() {
			return location.hash
		},
	},
	components: {
		Header,
		Main,
		MusicPlayer,
	}
}
</script>

<style lang="less">
* {
	margin: 0;
	padding: 0;
	box-sizing: border-box;
}

a {
	text-decoration: none;
}

li {
	list-style: none;
}

button {
	outline: none;
}

.app {
	min-width: 1200px;
	min-height: 800px;
}

/* 滚动条样式 */
.scrollbar {
	&:hover::-webkit-scrollbar-thumb {
		background-color: rgba(150, 150, 150, 0.3);
	}

	&::-webkit-scrollbar {
		width: 7px;
	}

	//设置滚动条
	&::-webkit-scrollbar-thumb {
		border-radius: 5px;
		background-color: rgba(150, 150, 150, 0);

		&:hover {
			background-color: rgba(150, 150, 150, 0.5);
		}
	}
}
</style>