/*
 * @Descripttion:
 * @version:
 * @Date: 2023-10-12 14:16:04
 * @LastEditors: zsjemail666@163.com
 */
import request from '@/utils/request'

//获取所有分类树形结构
export const getAllClassForELE = () => {
  return request({
    url: '/api/article/class/getAllClassForELE',
    method: 'get',
  })
}

//新增根节点
export const setRootClass = data => {
  return request({
    url: '/api/article/class/setRootClass',
    method: 'post',
    data,
  })
}

//追加节点
export const appendClass = data => {
  return request({
    url: '/api/article/class/appendClass',
    method: 'post',
    data,
  })
}

//删除当前节点
export const cancelClassById = data => {
  return request({
    url: '/api/article/class/cancelClassById',
    method: 'post',
    data,
  })
}
