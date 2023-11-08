/*
 * @Descripttion:
 * @version:
 *
 * @LastEditors: zsjemail666@163.com
 */
import request from '@/utils/request'

//获取文章列表  分页
export const getAllPageByIndex = (cur, size, data) => {
  return request({
    url: '/api/article/entity/getAllPageByIndex' + '/' + cur + '/' + size,
    method: 'post',
    data,
  })
}

//修改内容实体和标题
export const rebuildArt = data => {
  return request({
    url: '/api/article/entity/rebuildArt',
    method: 'post',
    data,
  })
}

//修改文章分类
export const rebuildArtType = data => {
  return request({
    url: '/api/article/entity/rebuildArtType',
    method: 'post',
    data,
  })
}

//删除文章
export const cancelArtFamilyById = data => {
  return request({
    url: '/api/article/entity/cancelArtFamilyById',
    method: 'post',
    data,
  })
}
//新增文章实体
export const joinArtFamily = data => {
  return request({
    url: '/api/article/entity/joinArtFamily',
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
