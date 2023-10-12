/*
 * @Descripttion:
 * @version:
 * @Date: 2023-10-12 14:16:04
 * @LastEditors: zsjemail666@163.com
 */
import request from '@/utils/request'

//获取角色列表  分页
export const getrole = (cur, size, data) => {
  return request({
    url: '/api/system/role/page' + '/' + cur + '/' + size,
    method: 'post',
    data,
  })
}

//获取所有角色
export const getAllRoleByIndex = () => {
  return request({
    url: '/api/system/role/getAll',
    method: 'get',
  })
}

//添加角色信息
export const addRoleInfo = data => {
  return request({
    url: '/api/system/role/add',
    method: 'post',
    data,
  })
}

//删除角色信息
export const removeRoleById = data => {
  return request({
    url: '/api/system/role/remove',
    method: 'post',
    data,
  })
}

//修改角色信息

export const updateRoleById = data => {
  return request({
    url: '/api/system/role/update',
    method: 'post',
    data,
  })
}
