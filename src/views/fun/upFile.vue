<template>
  <!--     action="http://124.70.34.218:88/api/system/oss/uploadOssFileSingle" -->
  <el-upload
    :headers="baseRequest"
    drag
    :show-file-list="false"
    class="baseUpload"
    action="http://124.70.34.218:88/api/system/oss/uploadOssFileSingle"
    :before-upload="beforceUpload"
    :on-success="handleFileSuccess"
    :on-progress="handleFileProgress"
    :on-error="handleFileError"
  >
    <el-icon class="el-icon--upload"><upload-filled /></el-icon>
    <div class="el-upload__text">
      拖拽到这里 or
      <em>点击上传</em>
    </div>
  </el-upload>
  <el-text class="mx-1" style="color: red">视频文件限制---50M</el-text>
  &ensp;&ensp;
  <el-text class="mx-1" style="color: blue">音乐文件限制---20M</el-text>
  &ensp;&ensp;
  <el-text class="mx-1" style="color: black">其它文件限制---10M</el-text>
  &ensp;&ensp;
  <el-text class="mx-1" style="color: green">图片文件限制---3M</el-text>
  <el-table :data="fileList" style="width: 100%">
    <el-table-column prop="uid" label="uid" />
    <el-table-column prop="name" label="文件名称" />
    <el-table-column prop="size" label="文件大小" />
    <el-table-column label="进度条" #default="scope">
      <!-- 
        进度条
         :text-inside="true"
         :percentage="perList.get(scope.row.uid)"
         -->
      <el-progress
        striped
        striped-flow
        :stroke-width="24"
        :percentage="100"
        status="success"
        :indeterminate="perList.get(scope.row.uid) != 100"
      />
    </el-table-column>
    <el-table-column label="操作" #default="scope">
      <el-button type="danger" @click="review(scope.row)">查看</el-button>
      <!-- <el-button type="danger" @click="rmF(scope.row)">删除</el-button> -->
    </el-table-column>
  </el-table>
  <el-drawer
    v-model="showData"
    title="上传结果"
    :direction="direction"
    :before-close="handleClose"
  >
    <el-result icon="success" title="上传成功" sub-title="图片地址如下">
      <template #extra>
        <el-text class="mx-1" type="success" style="color: green">
          URL地址为:{{ curUrl }}
        </el-text>
        <br />
        <el-button
          type="success"
          style="margin-top: 10px"
          @click="copyValue(curUrl)"
        >
          复制
        </el-button>
      </template>
    </el-result>
  </el-drawer>
</template>

<script setup>
import { ElMessage, ElMessageBox, ElNotification } from 'element-plus'
import { h, onMounted, reactive, ref } from 'vue'
import { useClipboard } from '@vueuse/core'
//文件列表
const fileList = ref([])
const showData = ref(false)
const curUrl = ref('aaa')
const { text, copy, copied, isSupported } = useClipboard({ curUrl })
//进度条集合 根据文件的uid进行存储
const perList = reactive(new Map())

onMounted(() => {
  // console.log(useClipboard)
})
const successFile = new Map()
const baseRequest = ref({
  system_api_Authorize_name: localStorage.getItem('username'),
  system_api_Authorize: localStorage.getItem('VEA-TOKEN').token,
})
//上传前的钩子
const beforceUpload = f => {
  fileList.value.push(f)
  //默认初始化进度条
}
//上传成功的钩子
const handleFileSuccess = (r, u, s) => {
  //完成进度条
  successFile.set(u.uid, r.url)
  if (r.code == 200) {
    ElMessage.success(r.msg)
  } else {
    ElMessage.warning(r.msg)
  }
}
//上传时的钩子
const handleFileProgress = async (e, u, s) => {
  //变化进度条
  perList.set(u.uid, e.percent)
}

const handleFileError = (e, f) => {
  perList.set(f.uid, 0)
  ElMessage.error('上传出现了错误 请联系开发者')
  ElMessage.error('请按照文件要求进行上传')
}

const review = e => {
  if (perList.get(e.uid) != 100) {
    ElNotification({
      title: '提示',
      message: h('i', { style: 'color: teal' }, '请等待上传成功'),
    })
  } else {
    showData.value = true
    curUrl.value = successFile.get(e.uid)
  }
}

const copyValue = v => {
  copy(v)
  ElNotification({
    title: '提示',
    message: h('i', { style: 'color: teal' }, '拷贝成功'),
  })
}
</script>

<style>
.baseUpload {
  width: 100%;
  height: 200px;
}
</style>
