<template>
    <div class="login">
        <h2>登录/注册</h2>
        <div class="container" :class="{ 'right-panel-active': isRightActive }">
            <div class="form-container sign-up-container">
                <form action="">
                    <h1>注册</h1>
                    <input type="text" placeholder="请输入用户名" v-model="name" @blur="validateName(name)">
                    <div class="alterError" v-show="alterName">
                        <el-icon>
                            <CircleCloseFilled />
                        </el-icon>
                        <span>用户名格式不正确: 6-30位字母或数字或"_"</span>
                    </div>
                    <input type="phone" placeholder="请输入手机号码" v-model.number="phone" @blur="validatePhone(phone)">
                    <div class="alterError" v-show="alterPhone">
                        <el-icon>
                            <CircleCloseFilled />
                        </el-icon>
                        <span>手机号码格式不正确</span>
                    </div>
                    <input type="password" placeholder="请输入密码" v-model="password" @blur="validatePwd(password)">
                    <div class="alterError" v-show="alterPwd">
                        <el-icon>
                            <CircleCloseFilled />
                        </el-icon>
                        <span>密码格式不正确: 6-30位字母或数字或"_"</span>
                    </div>
                    <input type="password" placeholder="密码确认(请再输入一遍密码)" v-model="confirmPassword" @blur="confirmPwd">
                    <div class="alterError" v-show="alterConfirmPwd">
                        <el-icon>
                            <CircleCloseFilled />
                        </el-icon>
                        <span>两次密码不一致</span>
                    </div>
                    <el-button type="primary" @click="signUp">注册</el-button>
                </form>
            </div>
            <div class="form-container sign-in-container">
                <form action="">
                    <h1>登录</h1>
                    <div class="mode">
                        <span @click="modeName">用户名登录</span>
                        <span @click="modePhone">手机登录</span>
                    </div>
                    <input type="text" placeholder="请输入用户名" v-model="name" v-show="modeLogin === 'Name'"
                        @blur="validateName(name)">
                    <div class="alterError" v-show="alterName">
                        <el-icon>
                            <CircleCloseFilled />
                        </el-icon>
                        <span>用户名格式不正确: 6-30位字母或数字或"_"</span>
                    </div>
                    <input type="phone" placeholder="请输入手机号码" v-model.number="phone" v-show="modeLogin === 'Phone'"
                        @blur="validatePhone(phone)">
                    <div class="alterError" v-show="alterPhone">
                        <el-icon>
                            <CircleCloseFilled />
                        </el-icon>
                        <span>手机号码格式不正确</span>
                    </div>
                    <input type="password" placeholder="请输入密码" v-model="password">
                    <span class="errorPassword"></span>
                    <a href="#">忘记密码?</a>
                    <el-button type="primary" @click="signIn">登录</el-button>
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
            alterName: false,
            alterPhone: false,
            alterPwd: false,
            alterConfirmPwd: false,

            //移动class绑定
            isRightActive: false,
        }
    },
    methods: {
        moveRight() {
            // this.$refs.container.classList.remove('right-panel-active')
            this.isRightActive = false
            if (this.modeLogin === 'Name') {
                this.alterPhone = false
            } else {
                this.alterName = false
            }
            this.alterPwd = false
            this.alterConfirmPwd = false
        },
        moveLeft() {
            // this.$refs.container.classList.add('right-panel-active')
            this.isRightActive = true
        },

        //切换到用户名登录模式
        modeName() {
            this.modeLogin = 'Name'
            this.alterPhone = false
        },

        //切换到电话号码登录模式
        modePhone() {
            this.modeLogin = 'Phone'
            this.alterName = false
        },

        //校验手机号
        validatePhone(Phone) {
            const reg = /^1[3-9][0-9]{9}$/
            if (Phone === '' || Phone === undefined || Phone === null) {
                return this.alterPhone = true
            } else {
                if ((!reg.test(Phone)) && Phone != '') {
                    return this.alterPhone = true
                } else {
                    return this.alterPhone = false
                }
            }
        },

        //校验用户名
        validateName(Name) {
            const reg = /^\w{6,30}$/
            if (Name === '' || Name === undefined || Name === null) {
                return this.alterName = true
            } else {
                if ((!reg.test(Name)) && Name != '') {
                    return this.alterName = true
                } else {
                    return this.alterName = false
                }
            }
        },

        //校验密码
        validatePwd(Pwd) {
            const reg = /^\w{6,30}$/
            if (Pwd === '' || Pwd === undefined || Pwd === null) {
                return this.alterPwd = true
            } else {
                if ((!reg.test(Pwd)) && Pwd != '') {
                    return this.alterPwd = true
                } else {
                    return this.alterPwd = false
                }
            }
        },

        //密码确认
        confirmPwd() {
            if (!(this.password === this.confirmPassword)) {
                return this.alterConfirmPwd = true
            } else {
                return this.alterConfirmPwd = false
            }
        },

        //登录
        signIn() {

        },

        //注册
        signUp() {

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
    background: linear-gradient(to left top, #0083fe, #00fff0);
    display: flex;
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

.alterError {
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