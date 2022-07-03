<template>
	<div class="app">
		<Header></Header>
		<div class="container">
			<router-view></router-view>
		</div>
		<MusicPlayer></MusicPlayer>
	</div>
</template>

<script>
import Header from './components/Header.vue'
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
		MusicPlayer
	}
}
</script>

<style lang="less" scoped>
.app {
	background-color: rgb(240, 239, 239);
}

.container {
	margin: 0 auto;
	width: 1000px;
}
</style>