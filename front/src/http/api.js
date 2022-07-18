import request from './request'

//搜索
export function getSearch(keywords, type) {
    return request({
        url: '/search',
        method: 'GET',
        params: {
            keywords: keywords,
            type: type
        }
    })
}