<!--
 *                        _oo0oo_
 *                       o8888888o
 *                       88" . "88
 *                       (| -_- |)
 *                       0\  =  /0
 *                     ___/`---'\___
 *                   .' \\|     |// '.
 *                  / \\|||  :  |||// \
 *                 / _||||| -:- |||||- \
 *                |   | \\\  - /// |   |
 *                | \_|  ''\---/''  |_/ |
 *                \  .-\__  '-'  ___/-. /
 *              ___'. .'  /--.--\  `. .'___
 *           ."" '<  `.___\_<|>_/___.' >' "".
 *          | | :  `- \`.;`\ _ /`;.`/ - ` : | |
 *          \  \ `_.   \_ __\ /__ _/   .-` /  /
 *      =====`-.____`.___ \_____/___.-`___.-'=====
 *                        `=---='
 * 
 * 
 *      ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * 
 *            佛祖保佑       永不宕机     永无BUG
 * 
 * @Descripttion: 
 * @version: 1.1
 *
 * @LastEditors:  zsj
 * 
 * @Author:   zsj

 -->

<template>
  <h2>编辑页面 id:{{ $route.params.id }}</h2>
  <el-button type="primary" @click="closeTag">关闭</el-button>
  <el-button type="success" @click="closeTag({ reload: true })">
    关闭当前页面，更新上级页面（组件重新渲染）
  </el-button>
  <el-button type="warning" @click="closeTag({ f5: true })">
    关闭当前页面，刷新上级页面（浏览器刷新）
  </el-button>
</template>
<script>
import { defineComponent } from 'vue'
import useCloseTag from '@/hooks/useCloseTag'

export default defineComponent({
  setup() {
    const { closeTag } = useCloseTag()
    return {
      closeTag,
    }
  },
})
</script>
