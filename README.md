###  :kissing_heart: 简介 
~~~
    这是一个基于vue3+vite+elementplus的后台管理系统前端项目
    
~~~
- [基于此分支的后端系统开发维护](https://gitee.com/zhengshengjun/zsj/tree/develop/)
###   :heart: 项目亮点
~~~
    1、混和使用了markdown编辑器 可以在自己的博客中插入各种各样的表情图片以及对markdown语法的更好支持
    2、实现了oss服务端签名直传,不会对服务器造成过大压力并且提高渲染速度（前端直接可以通过拼接的方法得到上传后的链接 不用等服务器端返回）
    3、权限系统的校验 根据不同登陆角色来决定登录的权限
~~~
###  :laughing: 项目技术栈
- vuex+pinia 实现全局对象处理
- vue-router 动态路由功能
- vue3  更好的ts语法支持
- 项目还提供了基础设施支持，包括按钮级别的权限控制、国际化支持、代码规范、Git 提交规范以及常用组件的封装，以便开发人员更高效地开发和维护项目。

###  :sunglasses: 特性

```
    1、项目足够简单 可以让刚接触微服务或者刚使用vue3的同学进行学习使用
    2、issues必回
```

## 开发

```bash
# 克隆项目
git https://gitee.com/zhengshengjun/zsj.git

# 进入项目目录
cd zsj

# 安装依赖
npm install

# 建议不要直接使用 cnpm 安装依赖，会有各种诡异的 bug。可以通过如下操作解决 npm 下载速度慢的问题
npm install --registry=https://registry.npm.taobao.org

# 启动服务
npm run dev
```

浏览器访问 http://localhost:9527

## 发布

```bash
# 构建测试环境
npm run build:stage

# 构建生产环境
npm run build:prod
```

## 其它

```bash
# 预览发布环境效果
npm run preview

# 预览发布环境效果 + 静态资源分析
npm run preview -- --report

# 代码格式检查
npm run lint

# 代码格式检查并自动修复
npm run lint -- --fix
```

### 鸣谢开源框架作者
- [国内访问](https://panjiachen.gitee.io/vue-element-admin-site/zh/)
- [国外链接](https://github.com/PanJiaChen/vue-element-admin/)