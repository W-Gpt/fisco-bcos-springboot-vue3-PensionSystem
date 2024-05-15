import axios from 'axios'
const request = axios.create({
	// baseURL: 'http://111.67.201.180',
	baseURL: 'http://192.168.47.100:8080',
	timeout: 5000
})

//request 拦截器
//可以自请求发送前对请求做一些处理
//比如统一加入token，对请求进行加密
request.interceptors.request.use(config => {
	config.headers['Content-Type'] = 'application/json;charset=utf-8';
	config.headers['token']=encodeURIComponent(localStorage.getItem('username'));
	config.headers['userAddress']=encodeURIComponent(localStorage.getItem('userAddress'));
	return config
},	error =>{
	return Promise.reject(error)
});

//response拦截器
//在接口响应后统一处理结果

request.interceptors.response.use(

	response => {
		let res = response.data;
		//兼容服务端返回的字符串数据
		if (typeof res == 'string') {
			res = res ? JSON.parse(res) : res
		}
		
		return res;
	},
		error => {
			console.log('err' + error) //debug
			return Promise.reject(error)
		}
		
)

export default request
