/*
 * @Descripttion:
 * @version:
 * @Date: 2021-04-20 11:06:21
 * @LastEditors: huzhushan@126.com
 * @LastEditTime: 2021-04-21 09:36:55
 * @Author: huzhushan@126.com
 * @HomePage: https://huzhushan.gitee.io/vue3-element-admin
 * @Github: https://github.com/huzhushan/vue3-element-admin
 * @Donate: https://huzhushan.gitee.io/vue3-element-admin/donate/
 */
import request from '@/utils/request'

// 登录接口
export const Login = data => {
  return request({
    // url: '/api/login',
    url: '/api/system/user/login',
    method: 'post',
    data,
  })
}

// 登录接口
export const Logout = () => {
  return request({
    url: '/api/system/user/logout',
    method: 'get',
  })
}

//获取验证码
export const GetValidateCode = () => {
  return request({
    url: '/api/system/captcha/get',
    method: 'get',
  })
}

// 获取登录用户信息
export const GetUserinfo = () => {
  return request({
    url: '/api/system/user/info',
    method: 'get',
  })
}