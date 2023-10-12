/*
 * @Descripttion:
 * @version:
 * @Date: 2023-10-12 14:16:04
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
