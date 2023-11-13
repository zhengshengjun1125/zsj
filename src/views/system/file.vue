<template>
  <div>
    <el-form :inline="true" :model="formInline" class="demo-form-inline">
      <el-form-item label="文件名称模糊">
        <el-input
          v-model="formInline.fileName"
          placeholder="文件名称"
          clearable
        />
      </el-form-item>
      <el-form-item label="所有者查询" v-if="isROOT">
        <el-input
          v-model="formInline.affiliation"
          placeholder="所有者名称"
          clearable
        />
      </el-form-item>
      <el-form-item label="文件后缀名查询">
        <el-input
          v-model="formInline.fileSuffix"
          placeholder="后缀名称"
          clearable
        />
      </el-form-item>
      <el-form-item label="文件类型">
        <el-tree-select
          v-model="curType"
          :data="fileType"
          :render-after-expand="false"
          clearable
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="flushFileList">查询</el-button>
      </el-form-item>
      <el-form-item>
        <el-text class="mx-1" style="color: red">
          文件大小为0的文件是老文件~都不会太大~请放心😜
        </el-text>
      </el-form-item>
    </el-form>

    <el-table :data="fileList" border style="width: 100%">
      <el-table-column prop="fileName" label="文件名称" width="250" />
      <el-table-column prop="upFileName" label="云文件名称" width="150" />
      <el-table-column prop="fileSuffix" label="格式" />
      <el-table-column prop="fileSize" label="大小(字节)" />
      <el-table-column prop="type" label="类型" />
      <el-table-column prop="affiliation" label="所有者" />
      <el-table-column prop="createTime" label="创建时间" />
      <el-table-column label="内容链接" width="100" #default="scope">
        <el-button type="success" @click="copyUrl(scope.row)">复制</el-button>
      </el-table-column>
    </el-table>

    <!--分页条-->
    <el-pagination
      v-model:current-page="pageParams.cur"
      v-model:page-size="pageParams.size"
      :page-sizes="[1, 5, 10, 20, 50, 100]"
      @size-change="flushFileList"
      @current-change="flushFileList"
      layout="total, sizes, prev, pager, next"
      :total="total"
    />
  </div>
</template>

<script setup>
import { useClipboard } from '@vueuse/core'
import { getfileList } from '@/api/system'
import { ref, reactive, onMounted, h } from 'vue'
import { ElNotification } from 'element-plus'
//分页页码以及当前页面显示页数
let pageParamsForm = {
  cur: 1,
  size: 10,
}

const fileType = ref([
  {
    value: '视频',
    label: '视频',
  },
  {
    value: '图片',
    label: '图片',
  },
  {
    value: '其它',
    label: '其它',
  },
  {
    value: '音乐',
    label: '音乐',
  },
  {
    value: '',
    label: '所有',
  },
])
const curUrl = ref('')
const { text, copy, copied, isSupported } = useClipboard({ curUrl })
const isROOT = ref(false)
const total = ref(0)
const fileList = ref([])
const pageParams = ref(pageParamsForm)
const curType = ref('')
//条件渲染对象
const formInline = reactive({
  fileName: '',
  fileSuffix: '',
  type: curType,
  affiliation: '',
})
const copyUrl = e => {
  copy(e.url)
  console.log(e)
  ElNotification({
    title: e.upFileName,
    message: h('i', { style: 'color: teal' }, '复制URL成功'),
    duration: 2000,
  })
  //   console.log(e.url)
}
onMounted(() => {
  const curUserName = localStorage.getItem('username')
  curUserName == 'zsj' ? (isROOT.value = true) : (isROOT.value = false)
  flushFileList()
})

//刷新文件列表的钩子
const flushFileList = async () => {
  const { data, code, msg } = await getfileList(
    pageParams.value.cur,
    pageParams.value.size,
    formInline
  )
  if (code == 200) {
    fileList.value = data.records
    total.value = data.total
  }
}
</script>

<style></style>
