<template>
	<div class="bg">
		<div class="create-song-list-container">
			<div class="close">
				<el-icon @click="setCreateView">
					<Close />
				</el-icon>
			</div>
			<div class="title">
				<h4>新建歌单</h4>
			</div>
			<div class="form">
				<el-form :model="form" :rules="rules" ref="ruleForm">
					<el-form-item label="歌单名称" prop="name">
						<el-input v-model="form.name" clearable maxlength="20" />
					</el-form-item>
					<el-form-item label="歌单简介">
						<el-input v-model="form.description" maxlength="80" show-word-limit type="textarea" />
					</el-form-item>
					<el-form-item class="right">
						<el-button type="primary" @click="create(ruleForm)">确认修改</el-button>
						<el-button @click="setCreateView">关闭</el-button>
					</el-form-item>
				</el-form>
			</div>
		</div>
	</div>
</template>

<script setup>
import { ref, reactive, defineEmits, onMounted, getCurrentInstance } from 'vue'
import defaultImg from '../assets/defaultImg.png'
let { proxy } = getCurrentInstance()

const ruleForm = ref()	//dom元素

let form = reactive({
	name: '',
	coverImg: '',
	description: ''
})

const emits = defineEmits(['setCreateView', 'getUserSongList'])
const setCreateView = () => {
	emits('setCreateView')
}

//校验昵称
const validateName = async (rule, Name, callback) => {
	const reg = /[a-zA-Z0-9\u4e00-\u9fa5]{3,20}/
	if (reg.test(Name)) {
		return callback()
	}
	callback(new Error('歌单名称格式不正确,请输入3-20个字母、数字、汉字'))
}
const rules = {
	name: [
		{ validator: validateName, trigger: 'blur' },
	],
}

const props = defineProps({
	userId: Number,
})
//提交创建信息
const create = (formEl) => {
	if (!formEl) return
	formEl.validate((valid) => {
		if (valid) {
			proxy.$http.post('/songlist/create', {
				userId: props.userId,
				name: form.name,
				coverImg: form.coverImg === '' ? defaultImg : form.coverImg,
				description: form.description
			})
				.then(res => {
					if (res.data.code === 200) {
						ElMessage.success('创建成功!')
						emits('getUserSongList')
					} else {
						ElMessage.error('修改失败!')
					}
				})
				.catch(err => {
					console.log(err)
				})
			setCreateView()
		} else {
			return false
		}
	})
}
</script>

<style lang="less" scoped>
.bg {
	position: absolute;
	top: 90px;
	left: 50%;
	transform: translateX(-50%);
	height: 80vh;
	background-color: rgba(122, 122, 122, 0.8);
	width: 100%;
	padding: 40px 0;
	z-index: 99;

	.create-song-list-container {
		width: 800px;
		margin: 0 auto;
		background-color: white;
		z-index: 99;

		.close {
			top: 10px;
			right: 10px;
			cursor: pointer;
		}

		.title {
			padding: 10px 100px 0;

			h4 {
				margin: 0;
			}
		}

		.form {
			padding: 30px 100px;

			.el-form {
				position: relative;
				margin: 30px 0;

				.right {
					position: absolute;
					right: 0;
				}
			}
		}
	}
}
</style>