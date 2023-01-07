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

<style lang="less" scoped>
.app {
	min-width: 1200px;
	min-height: 800px;
}
</style>