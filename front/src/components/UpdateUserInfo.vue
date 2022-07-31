<template>
    <div class="bg">
        <div class="Update-user-info-container">
            <div class="close">
                <el-icon @click="setUpdateView">
                    <Close />
                </el-icon>
            </div>
            <div class="form">
                <el-form :model="form" :rules="rules">
                    <el-form-item label="昵称" prop="name">
                        <el-input v-model="form.name" clearable />
                    </el-form-item>
                    <el-form-item label="性别">
                        <el-radio-group v-model="form.sex">
                            <el-radio :label="0">保密</el-radio>
                            <el-radio :label="1">男</el-radio>
                            <el-radio :label="2">女</el-radio>
                        </el-radio-group>
                    </el-form-item>
                    <el-form-item label="电话" prop="phone">
                        <el-input v-model="form.phone" clearable />
                    </el-form-item>
                    <el-form-item label="简介">
                        <el-input v-model="form.description" maxlength="80" show-word-limit type="textarea" />
                    </el-form-item>
                    <el-form-item class="right">
                        <el-button type="primary" @click="Submit">确认修改</el-button>
                        <el-button @click="setUpdateView">关闭</el-button>
                    </el-form-item>
                </el-form>
            </div>
        </div>
    </div>
</template>

<script>
import { mapState } from "vuex"
import { ElMessage } from 'element-plus'

export default {
    name: 'UpdateUserInfo',
    data() {
        return {
            form: {
                id: 0,
                name: '',
                sex: 0,
                phone: '',
                description: '',
            },
            rules: {
                name: [{ validator: this.validateName, trigger: 'blur' }],
                phone: [{ validator: this.validatePhone, trigger: 'blur' }],
            }
        }
    },
    computed: {
        ...mapState(['userInfo'])
    },
    created() {
        this.form = this.userInfo
    },
    methods: {
        setUpdateView() {
            this.$emit('setUpdateView', 'setUserInfo')
        },

        //校验昵称
        async validateName(rule, Name, callback) {
            const reg = /[a-zA-Z0-9\u4e00-\u9fa5]{3,30}/
            if (reg.test(Name)) {
                return callback()
            }
            callback(new Error('用户名格式不正确,请输入3-30个字母、数字、汉字'))
        },
        //校验手机号
        async validatePhone(rule, Phone, callback) {
            const reg = /^1[3-9][0-9]{9}$/
            if (reg.test(Phone)) {
                return callback()
            }
            callback(new Error('手机号码格式不正确'))
        },

        //提交修改信息
        Submit() {
            this.$http.put('/user/update', {
                id: this.form.id,
                name: this.form.name === '' ? null : this.form.name,
                sex: this.form.sex,
                phone: this.form.phone === '' ? null : this.form.phone,
                description: this.form.description === '' ? '这个人很懒，没有留下任何内容' : this.form.description
            }).then(res => {
                if (res.data.code === 200) {
                    ElMessage.success('修改成功!')
                    this.$emit('getUserDetail')
                } else {
                    ElMessage.error('修改失败!')
                }
            }).catch(err => {
                console.error(err)
            })
            this.setUpdateView()
        }
    },
}
</script>

<style lang="less">
.bg {
    position: absolute;
    top: 90px;
    left: 50%;
    transform: translateX(-50%);
    height: 80vh;
    background-color: rgba(122, 122, 122, 0.8);
    width: 100%;
    padding: 40px 0;

    .Update-user-info-container {
        width: 800px;
        margin: 0 auto;
        background-color: white;
        z-index: 99;

        .close {
            top: 10px;
            right: 10px;
            cursor: pointer;
        }

        .form {
            margin-top: 30px;
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