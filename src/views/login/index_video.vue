<!--
视频背景
-->
<template>
  <div class="login">
    <div class="video-box">
      <video
        class="video-background"
        preload="auto"
        loop
        playsinline
        autoplay
        src="https://request-oss-zsj.oss-cn-beijing.aliyuncs.com/login_video/Chainsaw%20Man%20Girls%201080p.mp4"
        tabindex="-1"
        muted="muted"
      ></video>
      <el-form class="form" :model="model" :rules="rules" ref="loginForm">
        <h1 class="title">{{ $t('login.title') }}</h1>
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
        </el-form-item>
      </el-form>
    </div>
    <div class="change-lang">
      <change-lang />
    </div>
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
import { Login, GetValidateCode } from '@/api/login'
import { useRouter, useRoute } from 'vue-router'
import ChangeLang from '@/layout/components/Topbar/ChangeLang.vue'
import useLang from '@/i18n/useLang'
import { useApp } from '@/pinia/modules/app'

export default defineComponent({
  components: { ChangeLang },
  name: 'login',
  setup() {
    const { proxy: ctx } = getCurrentInstance() // 可以把ctx当成vue2中的this
    const router = useRouter()
    const route = useRoute()
    const { lang } = useLang()
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
          min: 3,
          max: 12,
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
        username: '',
        password: '',
        code: '', // 用户输入的验证码
        key: '', // 后端返回的验证码key
      },
      captchaSrc: '',
      refreshCaptcha: async () => {
        const data = await GetValidateCode()
        state.model.key = data.key_id
        state.captchaSrc = data.captcha
      },
      rules: getRules(),
      loading: false,
      btnText: computed(() =>
        state.loading ? ctx.$t('login.logining') : ctx.$t('login.login')
      ),

      loginForm: ref(null),
      submit: () => {
        if (state.loading) {
          return
        }
        state.loginForm.validate(async valid => {
          if (valid) {
            state.loading = true
            const { code, data, msg } = await Login(state.model)
            if (+code === 200) {
              ///我们需要将用户账号保存到localstorge中
              localStorage.setItem('username', state.model.username)
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
  .form {
    color: black;
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
      z-index: 2;
      color: red;
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

video {
  width: 100%;
  height: 100vh;
  object-fit: cover;
}

.video-box {
  position: relative;
  height: 100vh;
  /*进行视频裁剪*/
  overflow: hidden;
  z-index: -1;
}

.video-box .video-background {
  position: absolute;
  left: 50%;
  top: 50%;
  /*保证视频内容始终居中*/
  transform: translate(-50%, -50%);
  width: 100%;
  height: 100%;
  /*保证视频充满屏幕*/
  object-fit: cover;
  min-height: 800px;
}
</style>
