<template>
  <div v-loading="loading">
    <el-form :inline="true" :model="emailBody" class="demo-form-inline">
      <el-form-item :label="$t('email.recipient')">
        <el-input
          v-model="emailBody.receiver"
          :placeholder="$t('email.recipient_tips')"
          clearable
        />
      </el-form-item>
      <el-form-item :label="$t('email.system')">
        <el-switch
          v-model="emailBody.isSystemMessage"
          inline-prompt
          :active-text="$t('email.yes')"
          :inactive-text="$t('email.no')"
          @change="changeSystemStatus"
        />
      </el-form-item>
      <el-form-item>
        <el-button
          color="#626aef"
          :dark="isDark"
          type="success"
          @click="sendEmailF"
        >
          {{ $t('email.send') }}
        </el-button>
      </el-form-item>
    </el-form>
    <span v-show="emailBody.isSystemMessage" style="color: red">
      系统级别的消息将会收取您更多的Z币,此条邮件将会使用系统邮箱进行发送~~~
    </span>
    <v-md-editor
      v-model="emailBody.content"
      height="400px"
      left-toolbar="undo redo | h bold italic  quote ul ol table hr link image code"
    ></v-md-editor>
  </div>
</template>

<script setup>
import { sendEmail } from '@/api/system'
import { ElMessage } from 'element-plus'
import { reactive, ref } from 'vue'

const emailBody = reactive({
  content: '',
  isSystemMessage: false,
  receiver: '',
  host: '',
  username: '',
  smtp: '',
})
const loading = ref(false)
const sendEmailF = async () => {
  loading.value = true
  if (emailBody.receiver == '') {
    ElMessage.warning('接收者不能为空')
  } else if (emailBody.isSystemMessage) {
    //系统级别的消息不需要三件套
    if (emailBody.content == '') {
      ElMessage.warning('您的邮件内容不能为空')
    } else {
      const { msg, code } = await sendEmail(emailBody)
      if (code == 200) {
        ElMessage.success(msg)
      } else ElMessage.error(msg)
    }
  } else {
    emailBody.host = localStorage.getItem('email_host')
    emailBody.username = localStorage.getItem('email_username')
    emailBody.smtp = localStorage.getItem('email_smtp')
    if (
      emailBody.host == undefined ||
      emailBody.username == undefined ||
      emailBody.smtp == undefined
    ) {
      ElMessage.error('非系统消息需要自己的邮件配置,在设置中~')
    } else {
      const { msg, code } = await sendEmail(emailBody)
      if (code == 200) ElMessage.success(msg)
      else ElMessage.error(msg)
    }
  }
  loading.value = false
}
</script>

<style></style>
