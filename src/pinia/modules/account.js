/*
 * @Descripttion:
 * @version:
 *
 * @LastEditors:  zsj
 * 
 * @Author:   zsj

 */
import { defineStore } from 'pinia'
import { GetUserinfo } from '@/api/login'

export const useAccount = defineStore('account', {
  state: () => ({
    userinfo: null,
    permissionList: [],
  }),
  actions: {
    // 清除用户信息
    clearUserinfo() {
      this.userinfo = null
      localStorage.removeItem('cur_user_id')
      localStorage.removeItem('userAcc')
    },
    // 获取用户信息
    async getUserinfo() {
      const { code, data } = await GetUserinfo()
      if (+code === 200) {
        const cur_user_id = data.id
        const username = data.username
        localStorage.setItem('cur_user_id', cur_user_id)
        localStorage.setItem('username', username)
        this.userinfo = data
        return Promise.resolve(data)
      }
    },
  },
})
