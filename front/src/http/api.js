import request from './request'

//获取轮播图
export function getBannerApi() {
    return request.get('/banner')
}

//搜索
export function getSearchApi(keywords, type) {
    return request.get('/search', {
        params: {
            keywords: keywords,
            type: type
        }
    })
}

//获取歌词
export function getLyricApi(id) {
    return request.get('/lyric', {
        params: {
            id: id
        }
    })
}

//检查音乐是否可用
export function getCheckMusic(id) {
    return request.get('/check/music', {
        params: {
            id: id
        }
    })
}

//获取音乐详情
export function getMusicDetail(ids) {
    return request.get('/song/detail', {
        params: {
            ids: ids
        }
    })
}

//获取音乐Url
export function getMusicUrl(id) {
    return request.get('/song/url', {
        params: {
            id: id
        }
    })
}

//获取歌单详情
export function getPlaylistDetail(id) {
    return request('/playlist/detail', {
        params: {
            id: id
        }
    })
}

//获取歌手详情
export function getSingerDetail(id) {
    return request('/artists', {
        params: {
            id: id
        }
    })
}

//获取专辑详情
export function getAlbumDetail(id) {
    return request('/album', {
        params: {
            id: id
        }
    })
}