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

const api_name = '/api/system/menu'
// 分页列表
export const FindNodes = () => {
  return request({
    url: `${api_name}/findNodes`,
    method: 'get',
  })
}

// 保存信息
export const SaveMenu = sysMenu => {
  return request({
    url: `${api_name}/save`,
    method: 'post',
    data: sysMenu,
  })
}

// 修改信息
export const UpdateSysMenuById = sysMenu => {
  return request({
    url: `${api_name}/updateById`,
    method: 'put',
    data: sysMenu,
  })
}

// 根据id删除数据
export const RemoveSysMenuById = id => {
  return request({
    url: `${api_name}/removeById/${id}`,
    method: 'delete',
  })
}
