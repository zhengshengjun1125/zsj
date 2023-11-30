<template>
  <div v-loading="loading">
    <el-radio-group v-model="Operation" class="ml-4" @change="showItem">
      <el-radio label="compress" size="large" border>图片字节压缩</el-radio>
      <el-radio label="watermark" size="large" border>添加水印</el-radio>
    </el-radio-group>
    <br />
    <div v-show="showwatermarkLocationOperation">
      <el-radio-group
        style="margin-top: 20px"
        v-model="watermarkLocationOperation"
        class="ml-4"
        @change="watermarkLocationBind"
      >
        <el-radio label="mid" size="large" border>中间</el-radio>
        <el-radio label="leftup" size="large" border>左上</el-radio>
        <el-radio label="rightup" size="large" border>右上</el-radio>
        <el-radio label="leftdown" size="large" border>左下</el-radio>
        <el-radio label="rightdown" size="large" border>右下</el-radio>
        <el-radio label="full" size="large" border>铺满</el-radio>
        <el-input
          style="margin-top: 20px"
          v-model="watermarkText"
          maxlength="10"
          placeholder="输入水印文字(默认ZSJ_BLOG)"
          clearable
          show-word-limit
        />
        <el-input
          class="tipsWaterMark"
          v-model="watermarkColor"
          disabled
          placeholder="水印颜色"
          clearable
        />
        <el-color-picker
          v-model="watermarkColor"
          show-alpha
          size="large"
          @change="changeWaterMarkColor"
        />
      </el-radio-group>
    </div>
    <!-- 压缩上传组件 -->
    <el-upload
      v-show="!showwatermarkLocationOperation"
      style="margin-top: 20px"
      :headers="baseRequest"
      drag
      :show-file-list="false"
      :response-type="'blob'"
      class="baseUpload"
      action="http://localhost:88/api/system/file/compress"
      :http-request="handleFileCompress"
      :on-progress="handleFileProgress"
      :on-error="handleFileError"
    >
      <el-icon class="el-icon--upload"><upload-filled /></el-icon>
      <div class="el-upload__text">
        <span style="color: green">拖拽文件到这里上传</span>
        <span style="color: red">或者</span>
        <em>点击上传文件</em>
      </div>
    </el-upload>
    <!-- 水印上传组件 -->
    <el-upload
      v-show="showwatermarkLocationOperation"
      style="margin-top: 20px"
      :headers="baseRequest"
      drag
      :show-file-list="false"
      :response-type="'blob'"
      class="baseUpload"
      action="http://localhost:88/api/system/file/watermark"
      :http-request="handleFilewatermark"
      :on-progress="handleFileProgress"
      :on-error="handleFileError"
    >
      <el-icon class="el-icon--upload"><upload-filled /></el-icon>
      <div class="el-upload__text">
        <span style="color: green">拖拽文件到这里上传</span>
        <span style="color: red">或者</span>
        <em>点击上传文件</em>
      </div>
    </el-upload>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { exportImageCompress, exportImageWaterMark } from '@/api/system'
import { generateRandomString, getRgbaBYstring } from '@/utils/encrypt'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useAccount } from '@/pinia/modules/account'
const watermarkColor = ref('rgba(86, 88, 93,1)')
const Operation = ref('compress')
const showwatermarkLocationOperation = ref(Operation.value == 'watermark')
//水印位置参数
const watermarkLocationOperation = ref('mid')
const watermarkText = ref('')
const loading = ref(false)
const showItem = v => {
  if (v == 'watermark') showwatermarkLocationOperation.value = true
  else showwatermarkLocationOperation.value = false
}
const baseRequest = ref({
  system_api_Authorize_name: localStorage.getItem('username'),
  system_api_Authorize: JSON.parse(localStorage.getItem('VEA-TOKEN')).token,
})

const watermarkLocationBind = v => {
  ElMessage.success('切换水印位置成功')
}

//自定义压缩图片上传组件
const handleFileCompress = async f => {
  const { file } = f
  let name = file.name
  const suffix = name.substring(name.lastIndexOf('.') + 1, name.length)
  if (suffix.toLowerCase() == 'jpg' || suffix.toLowerCase() == 'png') {
    ElMessageBox.confirm(`你将加工${file.name}. 确定吗?`, '温馨提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'success',
    })
      .then(async () => {
        const fileObj = new FormData()
        loading.value = true
        fileObj.append('file', file)
        const res = await exportImageCompress(fileObj)
        loading.value = false
        if (res.size != 0) {
          initFile(res)
          ElMessage.success('压缩成功')
        } else {
          ElMessage.warning('压缩失败,请检查余额')
        }
        initUserInfo()
      })
      .catch(() => {
        return false
      })
  } else {
    ElMessage.error('文件只能是jpg或者png格式')
  }
}

const handleFilewatermark = async f => {
  const { file } = f
  let name = file.name
  const suffix = name.substring(name.lastIndexOf('.') + 1, name.length)
  if (suffix.toLowerCase() == 'jpg' || suffix.toLowerCase() == 'png') {
    ElMessageBox.confirm(`你将加工${file.name}. 确定吗?`, '温馨提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'success',
    })
      .then(async () => {
        const fileObj = new FormData()
        loading.value = true
        //读取rgba的值 watermarkColor
        let arr = getRgbaBYstring(watermarkColor.value)
        fileObj.append('file', file)
        const res = await exportImageWaterMark(fileObj, {
          location: watermarkLocationOperation.value,
          text: watermarkText.value,
          r: arr[0],
          g: arr[1],
          b: arr[2],
          a: arr[3],
        })
        loading.value = false
        if (res.size != 0) {
          initFile(res)
          ElMessage.success('添加水印成功')
        } else {
          ElMessage.warning('添加水印失败,检查余额')
        }
        initUserInfo()
      })
      .catch(() => {
        return false
      })
  } else {
    ElMessage.error('文件只能是jpg或者png格式')
  }
}

const initFile = b => {
  const blob = new Blob([b], { type: 'image/jpeg' })
  const url = window.URL.createObjectURL(blob)
  const id = generateRandomString()
  let a = document.createElement('a')
  a.style.display = 'none'
  a.href = url
  a.setAttribute('download', `${id}.jpg`)
  document.body.appendChild(a)
  a.click()
  window.URL.revokeObjectURL(url)
  document.body.removeChild(a)
}

const changeWaterMarkColor = c => {
  console.log(c)
}

const initUserInfo = async () => {
  const { userinfo, getUserinfo } = useAccount()
  await getUserinfo()
}
</script>

<style scoped>
.tipsWaterMark {
  float: left;
  width: 200px;
}
</style>
