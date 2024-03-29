import { getCheckMusic, getMusicDetail } from './http/api'

//毫秒转为时分格式
export function getTime(duration) {
	let ss = Math.floor(duration / 1000 % 60)
	ss = ss < 10 ? '0' + ss : ss
	let mm = Math.floor(duration / 1000 / 60)
	mm = mm < 10 ? '0' + mm : mm
	return mm + ':' + ss
}

export function getHash() {
	return location.hash.slice(1) || '/'
}

//获取服务器图片
export async function getImg(url) {
	const { data } = await axios.get(window.BASEURL.baseURL + '/file/download', {
		params: {
			img: url
		},
		headers: {},
		responseType: 'blob',
	})
	const blob = data;
	return new Promise((resolve, reject) => {
		const reader = new FileReader();
		reader.readAsDataURL(blob);
		reader.onload = async () => {
			resolve(reader.result);
		}
	})
}

//检测歌曲是否可用
export async function checkMusic(id) {
	const { data } = await getCheckMusic(id)
	if (data.success !== true) {
		ElMessage.warning('抱歉, 该歌暂无版权')
		return {
			result: false,
			info: null,
		}
	}
	const { data: info } = await getMusicDetail(id)
	return {
		result: true,
		info: info.songs[0]
	}
}
