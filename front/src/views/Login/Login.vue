<template>
    <div class="login">
        <button class="goBack" @click="goBack">&lt;&lt;返回</button>
        <h2>登录/注册</h2>
        <div class="container" :class="{ 'right-panel-active': isRightActive }">
            <div class="form-container sign-up-container">
                <form action="" @keydown.enter="signIn">
                    <h1>注册</h1>
                    <input type="text" placeholder="请输入用户名" v-model="name" @blur="validateName(name)">
                    <div class="alertError" v-show="alertName">
                        <el-icon>
                            <CircleCloseFilled />
                        </el-icon>
                        <span>用户名格式不正确: 6-30位字母或数字或"_"</span>
                    </div>
                    <div class="alertError" v-show="alertRepeatName">
                        <el-icon>
                            <CircleCloseFilled />
                        </el-icon>
                        <span>该用户名已存在</span>
                    </div>
                    <input type="phone" placeholder="请输入手机号码" v-model.number="phone" @blur="validatePhone(phone)">
                    <div class="alertError" v-show="alertPhone">
                        <el-icon>
                            <CircleCloseFilled />
                        </el-icon>
                        <span>手机号码格式不正确</span>
                    </div>
                    <div class="alertError" v-show="alertRepeatPhone">
                        <el-icon>
                            <CircleCloseFilled />
                        </el-icon>
                        <span>该手机号已注册</span>
                    </div>
                    <input type="password" placeholder="请输入密码" v-model="password" @blur="validatePwd(password)">
                    <div class="alertError" v-show="alertPwd">
                        <el-icon>
                            <CircleCloseFilled />
                        </el-icon>
                        <span>密码格式不正确: 6-30位字母或数字或"_"</span>
                    </div>
                    <input type="password" placeholder="密码确认(请再输入一遍密码)" v-model="confirmPassword" @blur="confirmPwd">
                    <div class="alertError" v-show="alertConfirmPwd">
                        <el-icon>
                            <CircleCloseFilled />
                        </el-icon>
                        <span>两次密码不一致</span>
                    </div>
                    <el-button type="primary" @click="signUp"
                        :disabled="alertName || alertPhone || alertPwd || alertConfirmPwd || alertRepeatName || alertRepeatPhone">
                        注册
                    </el-button>
                </form>
            </div>
            <div class="form-container sign-in-container">
                <form action="" @keydown.enter="signIn">
                    <h1>登录</h1>
                    <div class="mode">
                        <span @click="modeName">用户名登录</span>
                        <span @click="modePhone">手机登录</span>
                    </div>
                    <input type="text" placeholder="请输入用户名" v-model="name" v-show="modeLogin === 'Name'"
                        @blur="validateName(name)">
                    <div class="alertError" v-show="alertName">
                        <el-icon>
                            <CircleCloseFilled />
                        </el-icon>
                        <span>用户名格式不正确: 6-30位字母或数字或"_"</span>
                    </div>
                    <input type="phone" placeholder="请输入手机号码" v-model.number="phone" v-show="modeLogin === 'Phone'"
                        @blur="validatePhone(phone)">
                    <div class="alertError" v-show="alertPhone">
                        <el-icon>
                            <CircleCloseFilled />
                        </el-icon>
                        <span>手机号码格式不正确</span>
                    </div>
                    <input type="password" placeholder="请输入密码" v-model="password" @blur="validatePwd(password)">
                    <div class="alertError" v-show="alertPwd">
                        <el-icon>
                            <CircleCloseFilled />
                        </el-icon>
                        <span>密码格式不正确: 6-30位字母或数字或"_"</span>
                    </div>
                    <span class="errorPassword"></span>
                    <a href="#">忘记密码?</a>
                    <el-button type="primary" @click="signIn" :disabled="alertName || alertPhone || alertPwd">登录
                    </el-button>
                </form>
            </div>
            <div class="overlay-container">
                <div class="overlay">
                    <div class="overlay-panel overlay-left">
                        <h1>欢迎回来!</h1>
                        <p>要访问我们的网站，请使用您的个人信息登录</p>
                        <button class="ghost" id="signIn" @click="moveRight">登录</button>
                    </div>
                    <div class="overlay-panel overlay-right">
                        <h1>嗨~ 朋友</h1>
                        <p>输入您的个人信息，并与我们一起开始旅程</p>
                        <button class="ghost" id="signUp" @click="moveLeft">注册</button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
import { mapMutations } from "vuex"

export default {
    name: 'App',
    data() {
        return {
            modeLogin: 'Name',
            name: '',
            phone: undefined,
            password: '',
            confirmPassword: '',    //确认密码

            //格式不正确提示信息
            alertName: false,
            alertRepeatName: false,
            alertPhone: false,
            alertRepeatPhone: false,
            alertPwd: false,
            alertConfirmPwd: false,

            //移动class绑定
            isRightActive: false,
        }
    },
    methods: {
        ...mapMutations(['setUserInfo']),

        goBack() {
            this.$router.back()
        },

        moveRight() {
            // this.$refs.container.classList.remove('right-panel-active')
            this.isRightActive = false
            if (this.modeLogin === 'Name') {
                this.alertPhone = false
            } else {
                this.alertName = false
            }
            this.alertPwd = false
            this.alertConfirmPwd = false
        },
        moveLeft() {
            // this.$refs.container.classList.add('right-panel-active')
            this.isRightActive = true
            this.confirmPwd()
        },

        //切换到用户名登录模式
        modeName() {
            this.modeLogin = 'Name'
            this.alertPhone = false
        },

        //切换到电话号码登录模式
        modePhone() {
            this.modeLogin = 'Phone'
            this.alertName = false
        },

        //校验手机号是否存在
        async validateRepeatPhone() {
            //判断手机号是否已被注册
            const { data: res } = await this.$http.get('/repeat', {
                params: {
                    phone: this.phone
                }
            })
            if (res.code === 0) {
                return true
            } else {
                return false
            }
        },

        //校验手机号
        async validatePhone(Phone) {
            const reg = /^1[3-9][0-9]{9}$/
            if (Phone === '' || Phone === undefined || Phone === null) {
                return this.alertPhone = true
            } else {
                if ((!reg.test(Phone)) && Phone != '') {
                    //如果手机号不符合要求, 则一定不重复
                    this.alertRepeatPhone = false
                    return this.alertPhone = true
                } else {
                    const PromiseResult = await this.validateRepeatPhone()
                    this.alertRepeatPhone = PromiseResult
                    return this.alertPhone = false
                }
            }
        },

        //校验用户名是否存在
        async validateRepeatName() {
            //判断用户名是否已被注册
            const { data: res } = await this.$http.get('/repeat', {
                params: {
                    username: this.name
                }
            })
            if (res.code === 200) {
                return false
            } else {
                return true
            }
        },

        //校验用户名
        async validateName(Name) {
            const reg = /^\w{6,30}$/
            if (Name === '' || Name === undefined || Name === null) {
                return this.alertName = true
            } else {
                if ((!reg.test(Name)) && Name != '') {
                    //如果用户名不符合要求, 则一定不重复
                    this.alertRepeatName = false
                    return this.alertName = true
                } else {
                    const PromiseResult = await this.validateRepeatName()
                    this.alertRepeatName = PromiseResult
                    return this.alertName = false
                }
            }
        },

        //校验密码
        validatePwd(Pwd) {
            const reg = /^\w{6,30}$/
            if (Pwd === '' || Pwd === undefined || Pwd === null) {
                return this.alertPwd = true
            } else {
                if ((!reg.test(Pwd)) && Pwd != '') {
                    return this.alertPwd = true
                } else {
                    return this.alertPwd = false
                }
            }
        },

        //密码确认
        confirmPwd() {
            if (!(this.password === this.confirmPassword)) {
                return this.alertConfirmPwd = true
            } else {
                return this.alertConfirmPwd = false
            }
        },

        //登录
        async signIn() {
            if (this.modeLogin === 'Name' && this.name === '') {
                this.alertName = true
            }
            if (this.modeLogin === 'Phone' && (this.phone === undefined || this.phone === '')) {
                this.alertPhone = true
            }
            if (this.password === '') {
                this.alertPwd = true
            }
            else {
                let res
                //用户名登录
                if (this.modeLogin === 'Name') {
                    if (!this.alertName && !this.alertPwd) {
                        const { data } = await this.$http.post('/login', {
                            username: this.name,
                            password: this.password
                        })
                        res = data
                    }
                }
                //手机号登录
                else if (this.modeLogin === 'Phone') {
                    if (!this.alertPhone && !this.alertPwd) {
                        const { data } = await this.$http.post('/login', {
                            phone: this.phone,
                            password: this.password
                        })
                        res = data
                    }
                }
                if (res.code === 200) {
                    location.href = 'index.html'
                    //记录登录状态的对象
                    let userInfo = {
                        isLogin: true,
                        manage: true,
                        name: res.data.name
                    }
                    //将表示登录状态的对象存入 localstorage 和 vuex 中
                    localStorage.setItem("userInfo", JSON.stringify(userInfo))
                    this.setUserInfo(userInfo)
                } else {
                    return ElMessage.error(res.msg)
                }
            }
        },
        //注册
        async signUp() {
            if (this.name === '') {
                this.alertName = true
            }
            if (this.phone === undefined || this.phone === '') {
                this.alertPhone = true
            }
            if (this.password === '') {
                this.alertPwd = true
            }
            if (this.confirmPassword === '') {
                this.alertConfirmPwd = true
            }
            if (!this.alertName && !this.alertPhone && !this.alertPwd && !this.alertConfirmPwd && !this.alertRepeatName && !this.alertRepeatPhone) {
                const { data: res } = await this.$http.post('/register', {
                    name: this.name,
                    username: this.name,
                    phone: this.phone,
                    password: this.password
                })
                if (res.code === 200) {
                    ElMessage.success('注册成功')
                    this.password = ''
                    this.moveRight()
                } else {
                    ElMessage.error(res.msg)
                }
            }
        }
    },
}
</script>

<style lang="less" scoped>
* {
    box-sizing: border-box;
    padding: 0;
}

.login {
    display: flex;
    position: relative;
    background: linear-gradient(to left top, #0083fe, #00fff0);
    justify-content: center;
    align-items: center;
    flex-direction: column;
    font-family: 'Montserrat', sans-serif;
    height: 100vh;
}

h1 {
    font-weight: bold;
    margin: 0;
}

h2 {
    text-align: center;
    margin-bottom: 12px;
    color: whitesmoke;
}

p {
    font-size: 14px;
    font-weight: 100;
    line-height: 20px;
    letter-spacing: 0.5px;
    margin: 20px 0 30px;
}

span {
    font-size: 12px;
}

a {
    color: #333;
    font-size: 14px;
    text-decoration: none;
    margin: 15px 0;
}

button {
    border-radius: 20px;
    border: 1px solid #409EFF;
    background-color: #409EFF;
    color: #fff;
    font-style: 12px;
    font-weight: bold;
    padding: 12px 45px;
    letter-spacing: 1px;
    transition: transform 80ms ease-in;
    cursor: pointer;
}

button:active {
    transform: scale(0.95);
}

button:focus {
    outline: none;
}

button.ghost {
    background-color: transparent;
    border-color: #fff;
}

form {
    background-color: #fff;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    padding: 0 50px;
    height: 100%;
    text-align: center;
}

input {
    background-color: #eee;
    border: none;
    padding: 12px 15px;
    margin: 8px 0;
    width: 100%;
}

.goBack {
    position: absolute;
    top: 0;
    left: 0;
    padding: 10px 20px;
    font-size: 18px;
    border-radius: 30px;
}

.container {
    background-color: #fff;
    border-radius: 10px;
    box-shadow: 0 14px 28px rgba(0, 0, 0, 0.25), 0 10px 10px rgba(0, 0, 0, 0.22);
    position: relative;
    overflow: hidden;
    width: 768px;
    max-width: 100%;
    min-height: 480px;
}

.form-container {
    position: absolute;
    top: 0;
    height: 100%;
    transition: all 0.6s ease-in-out;
}

.sign-in-container {
    left: 0;
    width: 50%;
    z-index: 2;
}

.sign-up-container {
    left: 0;
    width: 50%;
    opacity: 0;
    z-index: 1;
}

.container.right-panel-active .sign-in-container {
    transform: translateX(100%);
}



.container.right-panel-active .sign-up-container {
    transform: translateX(100%);
    opacity: 1;
    z-index: 5;
    animation: show 0.6s;
}

@keyframes show {

    0%,
    49.99% {
        opacity: 0;
        z-index: 1;
    }

    50%,
    100% {
        opacity: 1;
        z-index: 5;
    }
}

.mode {
    display: flex;
    width: 200px;
    height: 30px;
    justify-content: space-around;

    span {
        line-height: 30px;
        font-size: 16px;
        color: #2193b0;
        cursor: pointer;
    }
}

.overlay-container {
    position: absolute;
    top: 0;
    left: 50%;
    width: 50%;
    height: 100%;
    overflow: hidden;
    transition: 0.6s ease-in-out;
    z-index: 100;
}

.container.right-panel-active .overlay-container {
    transform: translateX(-100%);
    //left: 0;
}

.overlay {
    background: #00fff0;
    background: linear-gradient(to right bottom, #0083fe, #00fff0);
    background-repeat: no-repeat;
    background-size: cover;
    background-position: 0 0;
    color: #ffffff;
    position: relative;
    left: -100%;
    height: 100%;
    width: 200%;
    transform: translateX(0);
    transition: 0.6s ease-in-out;
}

.container.right-panel-active .overlay {
    transform: translateX(50%);
}

.overlay-panel {
    position: absolute;
    display: flex;
    align-items: center;
    justify-content: center;
    flex-direction: column;
    text-align: center;
    top: 0;
    height: 100%;
    width: 50%;
    transform: translateX(0);
    transition: transform 0.6s ease-in-out;
}

.overlay-left {
    transform: translateX(-20%);
}

.container.right-panel-active .overlay-left {
    transform: translateX(0);
}

.overlay-right {
    right: 0;
    transform: translateX(0);
}

.container.right-panel-active .overlay-right {
    transform: translateX(20%);
}

.alertError {
    background-color: #FEF0F0;
    width: 280px;
    height: 30px;
    font-size: 16px;
    color: red;

    .el-icon {
        vertical-align: middle;
    }
}
</style>