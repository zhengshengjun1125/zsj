/*
 * @Descripttion:
 * @version:
 *
 * @LastEditors:  zsj
 *: 
 * @Author:   zsj

 */
// login.js
const Login = () => import('@/views/login/index.vue')

export default [
  {
    path: '/login',
    name: 'login',
    component: Login,
  },
]
