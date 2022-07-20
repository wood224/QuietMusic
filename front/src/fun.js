//毫秒转为时分格式
export function getTime(duration) {
    let ss = Math.floor(duration / 1000 % 60)
    ss = ss < 10 ? '0' + ss : ss
    let mm = Math.floor(duration / 1000 / 60)
    mm = mm < 10 ? '0' + mm : mm
    return mm + ':' + ss
}