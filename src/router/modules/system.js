// 导入组件
const Layout = () => import('@/layout/index.vue')
const sysRole = () => import('@/views/system/sysRole.vue')
const sysUser = () => import('@/views/system/sysUser.vue')
const sysMenu = () => import('@/views/system/sysMenu.vue')
const sysOperLog = () => import('@/views/system/sysOperLog.vue')
const article = () => import('@/views/blog/article.vue')
const classType = () => import('@/views/blog/classType.vue')
const theHomeImg = () => import('@/views/blog/theHomeImg.vue')
const chat = () => import('@/views/fun/chat.vue')
const upFile = () => import('@/views/fun/upFile.vue')
const sysFile = () => import('@/views/system/file.vue')
const imageUtil = () => import('@/views/fun/imageUtil.vue')
const song = () => import('@/views/fun/song.vue')
const emailList = () => import('@/views/sms/emailList.vue')
const sendEmail = () => import('@/views/sms/sendEmail.vue')
const setting = () => import('@/views/setting/index.vue')
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
          title: 'menu.rolem',
          noCache: true,
        },
        hidden: false,
      },
      {
        path: '/sysUser',
        name: 'sysUser',
        component: sysUser,
        meta: {
          title: 'menu.userm',
          noCache: false,
        },
        hidden: false,
      },
      {
        path: '/menu',
        name: 'sysMenu',
        component: sysMenu,
        meta: {
          title: 'menu.menum',
        },
        hidden: false,
      },
      {
        path: '/log',
        name: 'sysOperLog',
        component: sysOperLog,
        meta: {
          title: 'menu.logm',
        },
        hidden: false,
      },

      {
        path: '/file',
        name: 'sysFile',
        component: sysFile,
        meta: {
          title: 'menu.filem',
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
          title: 'menu.articlem',
          noCache: false,
        },
        hidden: false,
      },
      {
        path: '/type',
        name: 'type',
        component: classType,
        meta: {
          title: 'menu.classm',
        },
        hidden: false,
      },
      {
        path: '/theHomeImg',
        name: 'theHomeImg',
        component: theHomeImg,
        meta: {
          title: 'menu.homeimgm',
        },
        hidden: false,
      },
    ],
  },
  {
    path: '/fun',
    component: Layout,
    name: 'fun',
    meta: {
      title: 'menu.fun',
    },
    icon: 'StarFilled',

    children: [
      {
        path: '/chat',
        name: 'chat',
        component: chat,
        meta: {
          title: 'menu.gochat',
          keepAlive: true,
        },
        hidden: false,
      },
      {
        path: '/upFile',
        name: 'upFile',
        component: upFile,
        meta: {
          title: 'menu.upfile',
          keepAlive: true,
        },
        hidden: false,
      },
      {
        path: '/imageUtil',
        name: 'imageUtil',
        component: imageUtil,
        meta: {
          title: 'menu.imageUtil',
        },
        hidden: false,
      },
      {
        path: '/song',
        name: 'song',
        component: song,
        meta: {
          title: 'menu.song',
        },
        hidden: false,
      },
    ],
  },
  {
    path: '/sms',
    component: Layout,
    name: 'sms',
    meta: {
      title: 'menu.sms',
    },
    icon: 'Document',
    children: [
      {
        path: '/emailList',
        name: 'emailList',
        component: emailList,
        meta: {
          title: 'menu.emailList',
          noCache: false,
        },
      },
      {
        path: '/sendEmail',
        name: 'sendEmail',
        component: sendEmail,
        meta: {
          title: 'menu.sendEmail',
          noCache: false,
        },
      },
    ],
  },
  {
    path: '/setting',
    component: Layout,
    name: 'setting',
    meta: {
      title: 'menu.setting',
    },
    icon: 'Setting',
    children: [
      {
        path: '/properties',
        name: 'properties',
        component: setting,
        meta: {
          title: 'menu.setting',
          noCache: false,
        },
      },
    ],
  },
]
