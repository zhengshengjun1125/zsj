<template>
  <el-upload
    :headers="baseRequest"
    drag
    :show-file-list="false"
    class="baseUpload"
    action="http://localhost:88/api/system/oss/uploadOssFileSingle"
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
  <el-table :data="fileList" style="width: 100%">
    <el-table-column prop="uid" label="uid" />
    <el-table-column prop="name" label="文件名称" />
    <el-table-column prop="size" label="文件大小" />
    <el-table-column label="进度条" #default="scope">
      <!-- 
        进度条
         :percentage="perList.get(scope.row.uid)"
         -->
      <el-progress
        striped
        striped-flow
        :stroke-width="24"
        :percentage="100"
        :text-inside="true"
        status="success"
        :indeterminate="perList.get(scope.row.uid) != 100"
        :duration="5"
      />
    </el-table-column>
    <el-table-column label="操作" #default="scope">
      <el-button type="danger" @click="review(scope.row)">查看</el-button>
      <!-- <el-button type="danger" @click="rmF(scope.row)">删除</el-button> -->
    </el-table-column>
  </el-table>
</template>

<script setup>
import { async } from '@kangc/v-md-editor'
import { ElMessage, ElMessageBox } from 'element-plus'
import { onMounted, reactive, ref } from 'vue'
//文件列表
const fileList = ref([])

//进度条集合 根据文件的uid进行存储
const perList = reactive(new Map())

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
  perList.set(u.uid, 100)
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

const handleFileError = () => {}

const review = e => {
  console.log(perList.get(e.uid))
  alert(successFile.get(e.uid))
}
</script>

<style>
.baseUpload {
  width: 100%;
  height: 200px;
}
</style>
