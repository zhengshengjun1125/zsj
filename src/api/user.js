/*
 * @Descripttion:
 * @version:
 *
 * @LastEditors: zsjemail666@163.com
 */
import request from '@/utils/request'

//获取用户列表  分页
export const getAllUser = (cur, size, data) => {
  return request({
    url: '/api/system/user/getAllUserByPage/' + cur + '/' + size,
    method: 'post',
    data,
  })
}

//注册用户
export const registerUser = data => {
  return request({
    url: '/api/system/user/register',
    method: 'post',
    data,
  })
}

//干掉用户
export const cancellationUser = data => {
  return request({
    url: '/api/system/user/cancellation',
    method: 'post',
    data,
  })
}

//修改用户
export const upgradeUserInfo = data => {
  return request({
    url: '/api/system/user/upgradeUserInfo',
    method: 'post',
    data,
  })
}

//修改用户密码
export const resetPass = data => {
  return request({
    url: '/api/system/user/resetPass',
    method: 'post',
    data,
  })
}
