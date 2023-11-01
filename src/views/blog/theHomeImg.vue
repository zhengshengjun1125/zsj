<template>
  <el-form :inline="true" :model="formInline" class="demo-form-inline">
    <el-form-item>
      <el-button type="success" @click="openinsertImgDialog = true">
        添加
      </el-button>
    </el-form-item>
  </el-form>

  <el-drawer size="80%" v-model="openinsertImgDialog" title="添加封面图信息">
    <div>
      <el-form label-width="120px">
        <el-form-item label="封面图">
          <el-upload
            :data="policyInfo"
            drag
            style="height: 800px; width: 500px"
            action="https://request-oss-zsj.oss-cn-beijing.aliyuncs.com"
            :show-file-list="false"
            :before-upload="beforceUpload"
            :on-success="handleAvatarSuccess"
            :on-progress="handleAvatarProgress"
          >
            <el-image
              v-if="insertInfo.photo"
              style="width: 95%; height: 30%"
              :src="insertInfo.photo"
              :zoom-rate="1.2"
              :max-scale="7"
              :min-scale="0.2"
              fit="cover"
              alt="点击更换"
            />
            <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
            <div class="el-upload__text">
              拖拽上传
              <em>点击上传</em>
            </div>
          </el-upload>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submitInsertImg">确认</el-button>
          <el-button @click="cancelInsertImg">取消</el-button>
        </el-form-item>
      </el-form>
    </div>
  </el-drawer>

  <el-table :data="imgList" border style="width: 100%">
    <el-table-column prop="id" label="Id" />
    <el-table-column prop="username" label="所属用户" />
    <el-table-column prop="photo" label="背景图">
      <template #default="scope">
        <el-image
          style="width: 70px; height: 70px"
          :src="scope.row.photo"
          alt="你的专属背景图"
          fit="fill"
          preview-teleported="true"
          :preview-src-list="[scope.row.photo]"
        ></el-image>
      </template>
    </el-table-column>
    <el-table-column prop="createTime" label="创建时间" />
    <el-table-column label="操作" width="150" #default="scope">
      <el-button type="primary" size="small" @click="upUserById(scope.row)">
        修改
      </el-button>
      <el-button type="danger" size="small" @click="rmUserById(scope.row)">
        删除
      </el-button>
    </el-table-column>
  </el-table>
</template>

<script setup>
import { getImgList, insertImg } from '@/api/system'
import { OssPolicyToPhoto } from '@/api/oss'
import { async } from '@kangc/v-md-editor'
import { onMounted, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { isAssetTypeAnImage } from '@/utils/format'

onMounted(() => {
  flushList()
  insertInfo.value.id = localStorage.getItem('cur_user_id')
})

const policyInfo = ref({
  key: '',
  policy: '',
  OSSAccessKeyId: '',
  success_action_status: '200',
  signature: '',
})

const imgList = ref([])
const openinsertImgDialog = ref(false)
const flushList = async () => {
  const { code, data } = await getImgList()
  imgList.value = data
}

const curImgUrl = ref()
const insertInfo = ref({})
const beforceUpload = async file => {
  let file_name = file.name
  const suffix = file_name.substring(file_name.lastIndexOf('.') + 1)
  console.log(suffix)
  if (!isAssetTypeAnImage(suffix)) {
    ElMessage.error('文件格式必须是图片哦！')
    return false
  }
  const response = await OssPolicyToPhoto()
  policyInfo.value.policy = response.data.policy
  policyInfo.value.signature = response.data.signature
  policyInfo.value.OSSAccessKeyId = response.data.accessid
  let timestamp = Date.parse(new Date())
  policyInfo.value.key = response.data.dir + timestamp + file.name
  const url = response.data.host + '/' + policyInfo.value.key
  curImgUrl.value = url
}

//上传成功之后的回调
const handleAvatarSuccess = (r, u) => {
  insertInfo.value.photo = curImgUrl.value
}
const submitInsertImg = async () => {
  const { data, code, msg } = await insertImg({
    photo: insertInfo.value.photo,
    belongUserId: insertInfo.value.id,
  })
  if (msg == '添加成功' && code == 200) {
    ElMessage.success(msg)
    flushList()
  } else {
    ElMessage.success(msg)
  }
}

const cancelInsertImg = () => {
  openinsertImgDialog.value = false
  flushList()
}
</script>

<style></style>
