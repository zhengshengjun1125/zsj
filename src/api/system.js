/*
 * @Descripttion:
 * @version:
 *
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

// 查询指定角色所对应的菜单id
export const GetSysRoleMenuIds = roleId => {
  return request({
    url: '/api/system/rolemenu/findSysRoleMenuByRoleId/' + roleId,
    method: 'get',
  })
}

// 根据角色分配菜单请求方法
export const DoAssignMenuIdToSysRole = assignMenuDto => {
  return request({
    url: '/api/system/rolemenu/doAssign',
    method: 'post',
    data: assignMenuDto,
  })
}

// 获取日志集合
export const getLogList = (cur, size) => {
  return request({
    url: '/api/system/log/list/' + cur + '/' + size,
    method: 'get',
  })
}

// 获取日志集合
export const gettenLogList = () => {
  return request({
    url: '/api/system/log/listTen',
    method: 'get',
  })
}

//获取背景图列表
export const getImgList = () => {
  return request({
    url: 'api/system/bloghomehp/getImgByName',
    method: 'get',
  })
}

//添加背景图列表
export const insertImg = data => {
  return request({
    url: '/api/system/bloghomehp/insert',
    method: 'post',
    data,
  })
}

//获取文件列表
export const getfileList = (cur, size, data) => {
  return request({
    url: '/api/system/file/list/' + cur + '/' + size,
    method: 'post',
    data,
  })
}
