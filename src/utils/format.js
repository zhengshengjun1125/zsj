/*
 * @Descripttion:对一些 参数进行格式判断
 * @version:1.0
 *
 * @LastEditors: zsjiscoder@foxmail.com
 * @Author: zsjiscoder@foxmail.com
 * @HomePage: https://gitee.com/zhengshengjun/zsj/tree/admin/
 */

// 判断后缀格式是否为图片
export function isAssetTypeAnImage(cipherText) {
  return (
    ['png', 'jpg', 'jpeg', 'bmp', 'gif', 'webp', 'psd', 'svg', 'tiff'].indexOf(
      cipherText.toLowerCase()
    ) !== -1
  )
}
