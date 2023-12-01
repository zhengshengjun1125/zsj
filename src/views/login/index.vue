<!--
 * @Descripttion: 
 * @version: 1.1
 *
 * @LastEditors: zsj
 * 
 * @Author:   zsj
-->
<template>
  <div class="login">
    <el-form class="form" :model="model" :rules="rules" ref="loginForm">
      <h1 class="title">
        {{ $t('login.title') }}
      </h1>
      <!-- 做一个邮箱登录的方法 -->
      <div v-show="emailLogin">
        <el-form-item prop="email">
          <el-input
            class="text"
            v-model="model.email"
            prefix-icon="User"
            clearable
            :placeholder="$t('login.email')"
          />
        </el-form-item>
        <el-form-item prop="captcha">
          <div class="captcha">
            <el-input
              class="text"
              v-model="model.code"
              prefix-icon="Picture"
              :placeholder="$t('login.emailtips')"
            ></el-input>
            <el-button
              type="primary"
              style="margin-left: 20px; width: 262px"
              size="large"
              :disabled="GetCodeStatus"
              @click="getEmailCode"
            >
              <span v-show="!GetCodeStatus">{{ $t('login.getCode') }}</span>
              <span v-show="GetCodeStatus">
                {{ $t('login.senedCode') }}({{ recount }})
              </span>
            </el-button>
          </div>
        </el-form-item>
        <el-form-item>
          <el-button
            :loading="loading"
            type="primary"
            class="btn"
            size="large"
            @click="submitEmail"
          >
            {{ btnText }}
          </el-button>
          <a
            @click="changeEmailLogin"
            style="font-size: 15px; margin-left: 320px; margin-top: 10px"
          >
            {{ $t('login.changeAl') }}
          </a>
        </el-form-item>
      </div>
      <div v-show="simpleLogin">
        <el-form-item prop="username">
          <el-input
            class="text"
            v-model="model.username"
            prefix-icon="User"
            clearable
            :placeholder="$t('login.username')"
          />
        </el-form-item>
        <el-form-item prop="password">
          <el-input
            class="text"
            v-model="model.password"
            prefix-icon="Lock"
            show-password
            clearable
            :placeholder="$t('login.password')"
          />
        </el-form-item>

        <el-form-item prop="captcha">
          <div class="captcha">
            <el-input
              class="text"
              v-model="model.code"
              prefix-icon="Picture"
              :placeholder="$t('login.tips')"
            ></el-input>
            <img :src="captchaSrc" @click="refreshCaptcha" />
          </div>
        </el-form-item>
        <el-form-item>
          <el-button
            :loading="loading"
            type="primary"
            class="btn"
            size="large"
            @click="submit"
          >
            {{ btnText }}
          </el-button>
          <a
            @click="changeEmailLogin"
            class="animate__animated animate__rotateInUpRight"
            style="font-size: 15px; margin-left: 320px; margin-top: 10px"
          >
            {{ $t('login.changeEl') }}
          </a>
        </el-form-item>
      </div>
    </el-form>
  </div>
  <div class="change-lang">
    <change-lang />
  </div>
</template>

<script>
import {
  defineComponent,
  getCurrentInstance,
  reactive,
  toRefs,
  ref,
  computed,
  watch,
  onMounted,
} from 'vue'
import {
  Login,
  emailLoginF,
  GetValidateCode,
  getEmailLoginCode,
} from '@/api/login'
import { useRouter, useRoute } from 'vue-router'
import ChangeLang from '@/layout/components/Topbar/ChangeLang.vue'
import useLang from '@/i18n/useLang'
import { useApp } from '@/pinia/modules/app'
import { ElMessage } from 'element-plus'

export default defineComponent({
  components: { ChangeLang },
  name: 'login',
  setup() {
    const { proxy: ctx } = getCurrentInstance() // 可以把ctx当成vue2中的this
    const router = useRouter()
    const route = useRoute()
    const recount = ref(60)
    const GetCodeStatus = ref(false)
    const simpleLogin = ref(true)
    const emailLogin = ref(false)
    const { lang } = useLang()
    const changeEmailLogin = () => {
      simpleLogin.value = !simpleLogin.value
      emailLogin.value = !emailLogin.value
    }
    watch(lang, () => {
      state.rules = getRules()
    })
    const getRules = () => ({
      userName: [
        {
          required: true,
          message: ctx.$t('login.rules-username'),
          trigger: 'blur',
        },
      ],
      password: [
        {
          required: true,
          message: ctx.$t('login.rules-password'),
          trigger: 'blur',
        },
        {
          min: 6,
          max: 100,
          message: ctx.$t('login.rules-regpassword'),
          trigger: 'blur',
        },
      ],
    })

    // onMounted钩子函数
    onMounted(() => {
      state.refreshCaptcha()
    })
    const state = reactive({
      model: {
        email: '',
        username: '',
        password: '',
        code: '', // 用户输入的验证码
        key: '', // 后端返回的验证码key
      },
      captchaSrc: '',
      refreshCaptcha: async () => {
        const { msg, code, captcha, key_id } = await GetValidateCode()
        if (code == 200) {
          state.model.key = key_id
          state.captchaSrc = captcha
        } else {
          ElMessage.error(msg)
        }
      },
      rules: getRules(),
      loading: false,
      btnText: computed(() =>
        state.loading ? ctx.$t('login.logining') : ctx.$t('login.login')
      ),
      //获取邮箱验证码
      getEmailCode: async () => {
        const emailAC = state.model.email
        if (emailAC === '' || emailAC == null || emailAC == undefined) {
          ElMessage.error(ctx.$t('login.emailillegality'))
        } else {
          const { code, msg } = await getEmailLoginCode(state.model)
          if (code == 200) {
            GetCodeStatus.value = true
            ElMessage.success(msg)
            const timer = setInterval(() => {
              recount.value--
              if (recount.value == 0) {
                clearInterval(timer)
                recount.value = 60
                GetCodeStatus.value = false
              }
            }, 1000)
          } else {
            ElMessage.error(msg)
          }
        }
      },
      loginForm: ref(null),
      submitEmail: async () => {
        //提交邮箱登录
        if (state.loading) {
          return
        }
        if (state.model.email == '' || state.model.code == '') {
          ElMessage.error('邮箱或者验证码不能为空')
        } else {
          state.loading = true
          const { code, data, msg } = await emailLoginF(state.model)
          if (+code === 200) {
            ctx.$message.success({
              message: ctx.$t('login.loginsuccess'),
              duration: 1000,
            })
            const targetPath = decodeURIComponent(route.query.redirect)
            if (targetPath.startsWith('http')) {
              // 如果是一个url地址
              window.location.href = targetPath
            } else if (targetPath.startsWith('/')) {
              // 如果是内部路由地址
              router.push(targetPath)
            } else {
              router.push('/')
            }
            // localStorage.setItem('username', state.model.username)
            useApp().initToken(data)
          } else {
            ctx.$message.error(msg)
          }
          state.loading = false
        }
      },
      submit: () => {
        if (state.loading) {
          return
        }
        state.loginForm.validate(async valid => {
          if (valid) {
            state.loading = true
            const { code, data, msg } = await Login(state.model)
            if (+code === 200) {
              ctx.$message.success({
                message: ctx.$t('login.loginsuccess'),
                duration: 1000,
              })
              const targetPath = decodeURIComponent(route.query.redirect)
              if (targetPath.startsWith('http')) {
                // 如果是一个url地址
                window.location.href = targetPath
              } else if (targetPath.startsWith('/')) {
                // 如果是内部路由地址
                router.push(targetPath)
              } else {
                router.push('/')
              }
              // localStorage.setItem('username', state.model.username)
              useApp().initToken(data)
            } else {
              ctx.$message.error(msg)
            }
            state.loading = false
          }
        })
      },
    })

    return {
      ...toRefs(state),
      simpleLogin,
      emailLogin,
      changeEmailLogin,
      GetCodeStatus,
      recount,
    }
  },
})
</script>

<style lang="scss" scoped>
.login {
  transition: transform 1s;
  transform: scale(1);
  width: 100%;
  height: 100%;
  overflow: hidden;
  background: #2d3a4b;
  .form {
    width: 520px;
    max-width: 100%;
    padding: 0 24px;
    box-sizing: border-box;
    margin: 160px auto 0;
    :deep {
      .el-input__wrapper {
        box-shadow: 0 0 0 1px rgba(255, 255, 255, 0.1) inset;
        background: rgba(0, 0, 0, 0.1);
      }
      .el-input-group--append > .el-input__wrapper {
        border-top-right-radius: 0;
        border-bottom-right-radius: 0;
      }
      .el-input-group--prepend > .el-input__wrapper {
        border-top-left-radius: 0;
        border-bottom-left-radius: 0;
      }
    }
    .title {
      color: #fff;
      text-align: center;
      font-size: 24px;
      margin: 0 0 24px;
    }
    .text {
      font-size: 16px;
      :deep(.el-input__inner) {
        color: #fff;
        height: 48px;
        line-height: 48px;
        &::placeholder {
          color: rgba(255, 255, 255, 0.2);
        }
      }
    }
    .btn {
      width: 100%;
    }
  }
}

.captcha {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 10px;
}

.captcha img {
  width: 200px;
  cursor: pointer;
  margin-left: 20px;
}

.change-lang {
  position: fixed;
  right: 20px;
  top: 20px;
  :deep {
    .change-lang {
      height: 24px;
      &:hover {
        background: none;
      }
      .icon {
        color: #fff;
      }
    }
  }
}
</style>
