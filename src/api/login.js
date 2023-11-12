/*
 * @Descripttion:
 * @version:
 *
 * @LastEditors:  zsj
 * 
 * @Author:   zsj

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
