<template>
	<div class="app">
		<Header></Header>
		<router-view></router-view>
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
</style>