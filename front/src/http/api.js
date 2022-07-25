import request from './request'

//获取轮播图
export function getBannerApi() {
    return request({
        url: '/banner',
        method: 'GET',
    })
}

//搜索
export function getSearchApi(keywords, type) {
    return request({
        url: '/search',
        method: 'GET',
        params: {
            keywords: keywords,
            type: type
        }
    })
}

//获取歌词
export function getLyricApi(id) {
    return request({
        url: '/lyric',
        method: 'GET',
        params: {
            id: id
        }
    })
}

//检查音乐是否可用
export function getCheckMusic(id) {
    return request({
        url: '/check/music',
        method: 'GET',
        params: {
            id: id
        }
    })
}

//获取音乐详情
export function getMusicDetail(ids) {
    return request({
        url: '/song/detail',
        method: 'GET',
        params: {
            ids: ids
        }
    })
}

//获取音乐Url
export function getMusicUrl(id) {
    return request({
        url: '/song/url',
        method: 'GET',
        params: {
            id: id
        }
    })
}