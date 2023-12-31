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
 *        佛曰:  
 *                写字楼里写字间，写字间里程序员；  
 *                程序人员写程序，又拿程序换酒钱。  
 *                酒醒只在网上坐，酒醉还来网下眠；  
 *                酒醉酒醒日复日，网上网下年复年。  
 *                但愿老死电脑间，不愿鞠躬老板前；  
 *                奔驰宝马贵者趣，公交自行程序员。  
 *                别人笑我忒疯癫，我笑自己命太贱；  
 *                不见满街漂亮妹，哪个归得程序员？
 * 
 * @Descripttion: 
 * @version: 1.1
 * @LastEditors:  zsj
 * @Author:   zsj
 -->

<template>
  <el-dialog v-model="openResetPassDig" title="修改密码" width="30%" draggable>
    <el-form label-width="120px">
      <el-form-item label="旧密码">
        <el-input
          type="password"
          show-password
          v-model="ordPass"
          placeholder="请输入旧密码"
        />
      </el-form-item>
      <el-form-item label="新密码">
        <el-input
          type="password"
          show-password
          v-model="newPass"
          placeholder="请输入新密码"
        />
      </el-form-item>
      <el-form-item label="确认新密码">
        <el-input
          type="password"
          show-password
          v-model="checkNewPass"
          placeholder="请在输入一次新密码"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="submitResetPassword">提交</el-button>
        <el-button @click="openResetPassDig = false">取消</el-button>
      </el-form-item>
    </el-form>
  </el-dialog>

  <el-dialog v-model="openRechargeDialog" title="充值Z币" width="30%" draggable>
    <el-form label-width="120px">
      <span style="color: red">每次扫码都可以获得1000的Z币哦~</span>
      <br />
      <span style="color: red">如果扫码了,请您刷新页面来进行余额查询~</span>
      <el-image
        style="width: 300px; height: 300px"
        :src="qrcodeUrl"
        fit="fill"
      />
      <el-form-item>
        <el-button type="primary" @click="openRecharge">完成</el-button>
        <el-button type="success" @click="getQR">刷新二维码</el-button>
      </el-form-item>
    </el-form>
  </el-dialog>
  <el-dropdown trigger="hover">
    <div class="userinfo">
      <template v-if="!userinfo">
        <i class="el-icon-user" />
        admin
      </template>
      <template v-else>
        <img
          class="avatar animate__animated animate__rollIn"
          :src="userinfo.avatar"
        />
        {{ userinfo.name }}
      </template>
    </div>
    <template #dropdown>
      <el-dropdown-menu>
        <el-dropdown-item @click="openSelfCenter">
          {{ $t('topbar.center') }}
        </el-dropdown-item>
        <el-dropdown-item @click="openResetPass">
          {{ $t('topbar.password') }}
        </el-dropdown-item>
        <el-dropdown-item @click="openRecharge">
          {{ $t('topbar.recharge') }}
        </el-dropdown-item>
        <lock-modal />
        <el-dropdown-item @click="logout">
          {{ $t('topbar.logout') }}
        </el-dropdown-item>
      </el-dropdown-menu>
    </template>
  </el-dropdown>
</template>
<script>
import { defineComponent, getCurrentInstance, onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { useUserinfo } from '@/components/Avatar/hooks/useUserinfo'
import LockModal from './LockModal.vue'
import { useApp } from '@/pinia/modules/app'
import { Logout } from '@/api/login'
import { resetPass } from '@/api/user'
import { getQRcode } from '@/api/system'
import { useAccount } from '@/pinia/modules/account'
export default defineComponent({
  components: {
    LockModal,
  },
  setup() {
    const qrcodeUrl = ref('')
    const ordPass = ref('')
    const newPass = ref('')
    const checkNewPass = ref('')
    const openRechargeDialog = ref(false)
    const openResetPassDig = ref(false)
    const router = useRouter()

    const { userinfo } = useUserinfo()

    const { proxy: ctx } = getCurrentInstance()

    onMounted(() => {
      getQR()
    })
    const openResetPass = () => {
      openResetPassDig.value = true
    }

    const openRecharge = async () => {
      openRechargeDialog.value = !openRechargeDialog.value
      getQR()
      const { getUserinfo } = useAccount()
      await getUserinfo()
    }

    //重新设置密码的前置验证
    const submitResetPassword = async () => {
      //提交之前的验证
      //新密码的长度最少为6个
      if (
        ordPass.value == null ||
        ordPass.value == '' ||
        ordPass.value == undefined
      ) {
        ctx.$message.error('旧密码不能为空')
      } else if (
        newPass.value == null ||
        newPass.value == '' ||
        newPass.value == undefined ||
        newPass.value.length < 6
      ) {
        ctx.$message.error('新密码不能为空并且长度最短为6位')
      } else if (
        checkNewPass.value == null ||
        checkNewPass.value == '' ||
        checkNewPass.value == undefined
      ) {
        ctx.$message.error('确认密码框不能为空')
      } else if (checkNewPass.value != newPass.value) {
        ctx.$message.error('两次输入的密码不一致')
      } else {
        //验证通过
        const { msg, code } = await resetPass({
          oldPassword: ordPass.value,
          newPassword: newPass.value,
        })
        if (code == 200) {
          //提示修改成功后将路由跳转到login
          ctx.$message.success(msg)
          router.push('/login')
        } else {
          ctx.$message.error(msg)
        }
      }
    }

    const openSelfCenter = () => {
      router.push('/')
    }

    // 退出
    const logout = async () => {
      const { code, msg } = await Logout()
      if (code == 200 && msg == '退出登录成功') {
        // 清除token
        ctx.$message.info(msg)
        localStorage.removeItem('username') //删除用户账号名称
        localStorage.removeItem('VEA-TOKEN') //清楚token
        localStorage.removeItem('cur_user_id') //删除用户账号名称
        localStorage.removeItem('email_host') //删除用户账号名称
        localStorage.removeItem('email_username') //删除用户账号名称
        localStorage.removeItem('email_smtp') //删除用户账号名称
        useApp().clearToken()
        router.push('/login')
      } else {
        ctx.$message.error(msg)
      }
    }
    const getQR = async () => {
      const res = await getQRcode()
      qrcodeUrl.value = window.URL.createObjectURL(res)
    }

    return {
      useAccount,
      onMounted,
      userinfo,
      logout,
      openResetPass,
      openResetPassDig,
      openRechargeDialog,
      ordPass,
      newPass,
      qrcodeUrl,
      checkNewPass,
      submitResetPassword,
      openSelfCenter,
      openRecharge,
      getQR,
    }
  },
})
</script>

<style lang="scss" scoped>
.userinfo {
  padding: 0 16px;
  line-height: 48px;
  cursor: pointer;
  display: flex;
  align-items: center;
  &:hover {
    background: #f5f5f5;
  }
  .el-icon-user {
    font-size: 20px;
    margin-right: 8px;
  }
  .avatar {
    margin-right: 8px;
    width: 32px;
    height: 32px;
    border-radius: 50%;
  }
}
</style>
