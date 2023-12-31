<template>
  <el-form :inline="true" :model="baseRequest" class="demo-form-inline">
    <el-form-item :label="$t('email.id')">
      <el-input
        v-model="baseRequest.id"
        :placeholder="$t('email.id_tips')"
        clearable
      />
    </el-form-item>
    <el-form-item :label="$t('email.title')">
      <el-input
        v-model="baseRequest.title"
        :placeholder="$t('email.title_tips')"
        clearable
      />
    </el-form-item>
    <el-form-item :label="$t('email.sender')">
      <el-input
        v-model="baseRequest.sender"
        :placeholder="$t('email.sender_tips')"
        clearable
      />
    </el-form-item>
    <el-form-item :label="$t('email.recipient')">
      <el-input
        v-model="baseRequest.recipient"
        :placeholder="$t('email.recipient_tips')"
        clearable
      />
    </el-form-item>
    <el-form-item :label="$t('email.system')">
      <el-switch
        v-model="isSystemStatus"
        inline-prompt
        :active-text="$t('email.yes')"
        :inactive-text="$t('email.no')"
        @change="changeSystemStatus"
      />
    </el-form-item>
    <el-form-item>
      <el-button color="#626aef" type="success" @click="flushEmailList">
        {{ $t('email.query') }}
      </el-button>
    </el-form-item>
    <el-form-item>
      <el-button type="success" @click="flushEmailListAndRest">
        {{ $t('public.Reset') }}
      </el-button>
    </el-form-item>
  </el-form>

  <el-dialog v-model="ShowEmailContent" :show-close="false">
    <template #header="{ close, titleId, titleClass }">
      <div class="my-header">
        <h4 :id="titleId" :class="titleClass">{{ emailtitle }}</h4>
        <el-button type="danger" @click="close">
          <el-icon class="el-icon--left"><CircleCloseFilled /></el-icon>
          {{ $t('public.close') }}
        </el-button>
      </div>
    </template>
    <div v-html="emailcontent"></div>
  </el-dialog>

  <el-table :data="emailData" border style="width: 100%">
    <el-table-column prop="id" :label="$t('email.id')" />
    <el-table-column prop="title" :label="$t('email.title')" />
    <el-table-column prop="sender" :label="$t('email.sender')" />
    <el-table-column prop="recipient" :label="$t('email.recipient')" />
    <el-table-column
      prop="isSystem"
      :label="$t('email.isSystem')"
      width="115"
    />
    <el-table-column prop="createTime" :label="$t('public.createTime')" />
    <el-table-column
      :label="$t('email.emailContent')"
      width="100"
      #default="scope"
    >
      <el-button type="success" @click="openMdPrivew(scope.row)">
        {{ $t('public.preview') }}
      </el-button>
    </el-table-column>
    <el-table-column :label="$t('public.oper')" width="90" #default="scope">
      <el-button type="danger" size="small" @click="rmEmailByid(scope.row)">
        {{ $t('public.delete') }}
      </el-button>
    </el-table-column>
  </el-table>

  <!--分页条-->
  <el-pagination
    v-model:current-page="pageParams.cur"
    v-model:page-size="pageParams.size"
    :page-sizes="[1, 5, 10, 20, 50, 100]"
    @size-change="flushEmailList"
    @current-change="flushEmailList"
    layout="total, sizes, prev, pager, next"
    :total="total"
  />
</template>

<script setup>
import { getCurrentInstance, onMounted, reactive, ref } from 'vue'
import { getEmailList, rmEmailPojo } from '@/api/system'
import { ElMessage, ElMessageBox } from 'element-plus'
let pageParamsForm = {
  cur: 1,
  size: 10,
}
const isSystemStatus = ref(true)
const baseRequest = reactive({
  sender: '',
  recipient: '',
  isSystem: 1,
  title: '',
  id: '',
})
const emailData = ref()
onMounted(() => {
  flushEmailList()
})
const emailtitle = ref('')
const emailcontent = ref('')
const ShowEmailContent = ref(false)
const total = ref(0)
const pageParams = ref(pageParamsForm)
const { ctx } = getCurrentInstance

const flushEmailList = async () => {
  const { msg, data, code } = await getEmailList(
    pageParams.value.cur,
    pageParams.value.size,
    baseRequest
  )
  if (code == 200) {
    data.records.forEach(element => {
      element.isSystem = element.isSystem == 1 ? 'Yes' : 'No'
    })
    emailData.value = data.records
    total.value = data.total
  } else {
    ElMessage.error(msg)
  }
}

const openMdPrivew = e => {
  ShowEmailContent.value = true
  emailcontent.value = e.content
  emailtitle.value = e.title
}

const rmEmailByid = async e => {
  // console.log(e.id)
  ElMessageBox.confirm('确定吗?', '温馨提示').then(async () => {
    const { msg, code } = await rmEmailPojo({ id: e.id })
    if (code == 200) {
      ElMessage.success(msg)
      flushEmailList()
    } else {
      ElMessage.error(msg)
    }
  })
}

const changeSystemStatus = e => {
  if (!e) baseRequest.isSystem = 0
  else baseRequest.isSystem = 1
}

const flushEmailListAndRest = () => {
  baseRequest.sender = ''
  baseRequest.recipient = ''
  baseRequest.isSystem = 1
  baseRequest.title = ''
  baseRequest.id = ''
  isSystemStatus.value = true
  flushEmailList()
}
</script>
