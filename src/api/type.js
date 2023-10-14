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
