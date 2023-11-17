/*
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
 * @version:
 * @LastEditors:  zsj
 * @Author:   zsj
 */

import { createApp } from 'vue'
import App from './App.vue'

const app = createApp(App)

// 引入element-plus
import ElementPlus from 'element-plus'
import './assets/style/element-variables.scss'

// 国际化
import i18n from '@/i18n'

// 全局注册element-plus/icons-vue
import * as ICONS from '@element-plus/icons-vue'
Object.entries(ICONS).forEach(([key, component]) => {
  // app.component(key === 'PieChart' ? 'PieChartIcon' : key, component)
  app.component(key, component)
})

// 引入路由
import router from './router'

// 引入pinia
import pinia from './pinia'

// 权限控制
import './permission'

// 引入svg图标注册脚本
import 'vite-plugin-svg-icons/register'

// 注册全局组件
import * as Components from './global-components'
Object.entries(Components).forEach(([key, component]) => {
  app.component(key, component)
})

// 注册自定义指令
import * as Directives from '@/directive'
Object.values(Directives).forEach(fn => fn(app))

// 错误日志
import useErrorHandler from './error-log'

//引入文档编辑器
import VueMarkdownEditor from '@kangc/v-md-editor'
import '@kangc/v-md-editor/lib/style/base-editor.css'
import vuepressTheme from '@kangc/v-md-editor/lib/theme/vuepress.js'
import '@kangc/v-md-editor/lib/theme/style/vuepress.css'
import Prism from 'prismjs'
//表情支持
import createEmojiPlugin from '@kangc/v-md-editor/lib/plugins/emoji/index'
import '@kangc/v-md-editor/lib/plugins/emoji/emoji.css'

//引入预览组件
import VMdPreview from '@kangc/v-md-editor/lib/preview'
import '@kangc/v-md-editor/lib/style/preview.css'
import githubTheme from '@kangc/v-md-editor/lib/theme/github.js'
import '@kangc/v-md-editor/lib/theme/style/github.css'
import VMdPreviewHtml from '@kangc/v-md-editor/lib/preview-html'
import '@kangc/v-md-editor/lib/style/preview-html.css'

// highlightjs
import hljs from 'highlight.js'

import 'animate.css'

//clipboard3.js
// import clipboard3 from 'vue-clipboard3';

VMdPreview.use(githubTheme, {
  Hljs: hljs,
})
VueMarkdownEditor.use(vuepressTheme, {
  Prism,
})
VueMarkdownEditor.use(createEmojiPlugin())

useErrorHandler(app)
app
  .use(VMdPreviewHtml)
  .use(VMdPreview)
  .use(VueMarkdownEditor)
  .use(i18n)
  .use(ElementPlus)
  .use(pinia)
  .use(router)
  .mount('#app')
