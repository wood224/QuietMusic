import { createStore } from 'vuex'

export default createStore({
    state: {
        userInfo: {}
    },
    getters: {
    },
    mutations: {
        setUserInfo(state, userInfo) {
            state.userInfo = userInfo
        },
    },
    actions: {
    },
    modules: {
    }
})