import axios from 'axios'
import { ElMessage } from 'element-plus'
import router from '@/router'
import { useApp } from '@/pinia/modules/app'

// const base = 'http://localhost:81' nginx

// const prod = 'http://124.70.34.218:88'

const base = 'http://localhost:88'

const service = axios.create({
  // baseURL: '/',
  baseURL: base,
  timeout: 10000,
  withCredentials: true,
})

// 拦截请求
service.interceptors.request.use(
  config => {
    const { authorization } = useApp()
    if (authorization) {
      // config.headers.Authorization = `Bearer ${authorization.token}`
      config.headers.system_api_Authorize = `${authorization.token}`
      config.headers.system_api_Authorize_name = `${localStorage.getItem(
        'username'
      )}`
      // config.headers.system_api_Authorize_name = 'zsj'
    }
    return config
  },
  error => {
    // console.log(error);
    return Promise.reject(error)
  }
)

// 拦截响应
service.interceptors.response.use(
  // 响应成功进入第1个函数，该函数的参数是响应对象
  response => {
    console.log(response)
    return response.data
  },
  // 响应失败进入第2个函数，该函数的参数是错误对象
  async error => {
    // 如果响应码是 401 ，则请求获取新的 token
    // 响应拦截器中的 error 就是那个响应的错误对象
    if (error.response && error.response.status === 401) {
      // 校验是否有 refresh_token
      const { authorization, clearToken } = useApp()
      if (!authorization || !authorization.refresh_token) {
        if (router.currentRoute.value.name === 'login') {
          return Promise.reject(error)
        }
        const redirect = encodeURIComponent(window.location.href)
        router.push(`/login?redirect=${redirect}`)
        // 清除token
        clearToken()
        setTimeout(() => {
          ElMessage.closeAll()
          try {
            ElMessage.error(error.response.data.msg)
          } catch (err) {
            ElMessage.error(error.message)
          }
        })
        // 代码不要往后执行了
        return Promise.reject(error)
      }
    }
    // console.dir(error) // 可在此进行错误上报
    ElMessage.closeAll()
    try {
      ElMessage.error(error.response.data.msg)
    } catch (err) {
      ElMessage.error(error.message)
    }

    return Promise.reject(error)
  }
)

export default service
