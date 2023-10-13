/*
 * @Descripttion:
 * @version:
 * @Date: 2023-10-12 14:16:04
 * @LastEditors: zsjemail666@163.com
 */
import request from '@/utils/request'

//图片服务端签名直传
export const OssPolicyToPhoto = () => {
  return request({
    url: '/api/system/oss/policyToPhoto',
    method: 'get',
  })
}
