<template>
	<div>
		<Header></Header>
		<div class="container">
			<router-view></router-view>
		</div>

	</div>
</template>

<script>
import Header from './components/Header.vue'

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
	}
}
</script>

<style lang="less" scoped>
.container {
	margin: 0 auto;
	width: 1000px;
}
</style>