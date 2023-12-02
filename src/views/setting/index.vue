<template>
  <el-card class="box-card">
    <span style="color: purple; font-family: fantasy; font-size: 30px">
      {{ $t('email.cardEmailConfig') }}
    </span>
    <el-form :inline="true" :model="formInline" class="demo-form-inline">
      <el-form-item :label="$t('email.host')">
        <el-input
          style="width: 300px"
          v-model="emailProperties.host"
          :placeholder="$t('email.host_tips')"
          clearable
        />
      </el-form-item>
      <el-form-item :label="$t('email.username')">
        <el-input
          style="width: 300px"
          v-model="emailProperties.username"
          :placeholder="$t('email.username_tips')"
          clearable
        />
      </el-form-item>
      <el-form-item :label="$t('email.SMTP_pass')">
        <el-input
          style="width: 300px"
          type="password"
          v-model="emailProperties.smtp"
          :placeholder="$t('email.smtp_tips')"
          show-password
        />
      </el-form-item>
      <el-form-item>
        <el-button color="#626aef" type="primary" @click="apply">
          应用配置
        </el-button>
      </el-form-item>
    </el-form>
  </el-card>
</template>

<script setup>
import { initEmailProperties, getEmailProperties } from '@/api/system'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getCurrentInstance, onMounted, reactive } from 'vue'
const { ctx } = getCurrentInstance
const emailProperties = reactive({
  host: '',
  username: '',
  smtp: '',
})
onMounted(() => {
  initProps()
})
const apply = async () => {
  const { code, msg } = await initEmailProperties(emailProperties)
  if (code == 200) {
    ElMessage.success(msg)
  } else {
    ElMessage.success(msg)
  }
}
const initProps = async () => {
  const { data, msg, code } = await getEmailProperties()
  emailProperties.host = data.host
  emailProperties.username = data.username
  emailProperties.smtp = data.smtp
  localStorage.setItem('email_host', data.host)
  localStorage.setItem('email_username', data.username)
  localStorage.setItem('email_smtp', data.smtp)
}
</script>

<style>
.demo-form-inline .el-input {
  --el-input-width: 220px;
}
.demo-form-inline {
  margin-top: 5px;
}
.box-card {
  width: 480px;
}
</style>
