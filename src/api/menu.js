/*
 * @Descripttion:
 * @version:
 *
 * @LastEditors:  zsj
 * 
 * @Author:   zsj

 */
import request from '@/utils/request'

// 获取菜单
export const GetMenus = params => {
  return request({
    url: '/api/system/menu/menus',
    method: 'get',
    params,
  })
}
