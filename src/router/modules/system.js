// 导入组件
const Layout = () => import('@/layout/index.vue')
const sysRole = () => import('@/views/system/sysRole.vue')
const sysUser = () => import('@/views/system/sysUser.vue')
const sysMenu = () => import('@/views/system/sysMenu.vue')
const article = () => import('@/views/blog/article.vue')
const classType = () => import('@/views/blog/classType.vue')
const theHomeImg = () => import('@/views/blog/theHomeImg.vue')
// 导出该组件
export default [
  {
    path: '/system',
    component: Layout,
    name: 'system',
    meta: {
      title: 'menu.system',
    },
    icon: 'Operation',
    children: [
      {
        path: '/sysRole',
        name: 'sysRole',
        component: sysRole,
        meta: {
          title: '角色管理',
        },
        hidden: false,
      },
      {
        path: '/sysUser',
        name: 'sysUser',
        component: sysUser,
        meta: {
          title: '用户管理',
        },
        hidden: false,
      },
      {
        path: '/menu',
        name: 'sysMenu',
        component: sysMenu,
        meta: {
          title: '菜单管理',
        },
        hidden: false,
      },
    ],
  },
  {
    path: '/blog',
    component: Layout,
    name: 'blog',
    meta: {
      title: 'menu.blog',
    },
    icon: 'HomeFilled',
    children: [
      {
        path: '/article',
        name: 'article',
        component: article,
        meta: {
          title: '文章管理',
        },
        hidden: false,
      },
      {
        path: '/type',
        name: 'type',
        component: classType,
        meta: {
          title: '分类管理',
        },
        hidden: false,
      },
      {
        path: '/theHomeImg',
        name: 'theHomeImg',
        component: theHomeImg,
        meta: {
          title: '封面管理',
        },
        hidden: false,
      },
    ],
  },
]
