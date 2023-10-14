<template>
  <div>
    <el-form :inline="true" :model="formInline" class="demo-form-inline">
      <el-form-item label="标题模糊查询">
        <el-input
          v-model="formInline.artTitle"
          placeholder="标题内容"
          clearable
        />
      </el-form-item>
      <el-form-item label="作者查询" v-if="isROOT">
        <el-input v-model="formInline.user" placeholder="作者名称" clearable />
      </el-form-item>
      <el-form-item label="日访问>=">
        <el-input
          v-model="formInline.artRequestDay"
          placeholder="最小日访问"
          clearable
        />
      </el-form-item>
      <el-form-item label="月访问>=">
        <el-input
          v-model="formInline.artRequestMonth"
          placeholder="最小月访问"
          clearable
        />
      </el-form-item>
      <el-form-item label="总访问>=">
        <el-input
          v-model="formInline.artRequestTotal"
          placeholder="最小总访问"
          clearable
        />
      </el-form-item>
      <el-form-item label="分类名称">
        <el-tree-select
          v-model="value"
          :data="typeList"
          :render-after-expand="false"
          clearable
        />
      </el-form-item>
      <el-form-item>
        <el-button type="success" @click="openjoinArtListDrawer">
          新增
        </el-button>
        <el-button type="primary" @click="flushARTList">查询</el-button>
      </el-form-item>
    </el-form>

    <!-- 新增 -->
    <el-drawer
      size="80%"
      v-model="openjoinArtListDrawerstatus"
      :before-close="handleClose"
    >
      <el-input
        class="add_page_title"
        v-model="curmdaddArtTitile"
        placeholder="文章标题"
        clearable
      />
      <br />
      <el-tree-select
        class="add_page_title"
        placeholder="请选择分类"
        v-model="value"
        :data="typeList"
        :render-after-expand="false"
        clearable
      />
      <v-md-editor v-model="curAddContent" height="400px"></v-md-editor>
      <el-button class="md_submit" type="success" @click="addARTINFOSUBMITNOW">
        提交
      </el-button>
    </el-drawer>
    <!-- 预览 -->
    <el-drawer
      size="80%"
      v-model="openMd"
      :direction="btt"
      :before-close="handleClose"
    >
      <el-input
        v-model="curmdprivewTitile"
        placeholder="当前文章标题"
        clearable
      />
      <v-md-editor v-model="curContent" height="400px"></v-md-editor>
      <!-- 预览模式<v-md-editor
        v-model="curContent"
        height="400px"
        mode="preview"
      ></v-md-editor> -->
      <el-button class="md_submit" type="success" @click="rebuildArtinfo">
        修改
      </el-button>
    </el-drawer>

    <!-- 修改用户对话框 -->
    <el-dialog v-model="openUpType" title="修改文章分类" width="30%" draggable>
      <el-form label-width="120px">
        <el-form-item label="角色名称">
          <el-tree-select
            v-model="value"
            :data="typeList"
            :render-after-expand="false"
            clearable
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="resetArtType">提交</el-button>
          <el-button @click="cancelResetType">取消</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>

    <el-table :data="articleData" border style="width: 100%">
      <el-table-column prop="artTitle" label="标题" width="180" />
      <el-table-column prop="artClassName" label="分类" width="120" />
      <el-table-column prop="artAuther" label="作者" width="180" />
      <el-table-column prop="artRequestDay" label="日访问量" />
      <el-table-column prop="artRequestMonth" label="月问量" />
      <el-table-column prop="artRequestTotal" label="总访问量" />
      <el-table-column label="内容链接" width="100" #default="scope">
        <el-button type="success" @click="openMdPrivew(scope.row)">
          预览
        </el-button>
      </el-table-column>
      <el-table-column label="操作" width="200" #default="scope">
        <el-button type="primary" size="small" @click="resetType(scope.row)">
          修改分类
        </el-button>
        <el-button type="danger" size="small" @click="rmArt(scope.row)">
          删除
        </el-button>
      </el-table-column>
    </el-table>

    <!--分页条-->
    <el-pagination
      v-model:current-page="pageParams.cur"
      v-model:page-size="pageParams.size"
      :page-sizes="[1, 5, 10, 20, 50, 100]"
      @size-change="flushARTList"
      @current-change="flushARTList"
      layout="total, sizes, prev, pager, next"
      :total="total"
    />
  </div>
</template>

<script setup>
import { onMounted, reactive, ref } from 'vue'
import {
  getAllPageByIndex,
  rebuildArt,
  rebuildArtType,
  joinArtFamily,
  cancelArtFamilyById,
} from '@/api/article'
import { getAllClassForELE } from '@/api/type'
import { ElMessageBox, ElMessage } from 'element-plus'
import { async } from '@kangc/v-md-editor'

//分页页码以及当前页面显示页数
let pageParamsForm = {
  cur: 1,
  size: 10,
}
const total = ref(0)
//将分页信息封装为响应式对象
const openUpType = ref(false)
const pageParams = ref(pageParamsForm)
const value = ref()
const typeList = ref([])
const isROOT = ref(false)
const showLoading = ref(false)
const status = ref(true)
const articleData = ref([])
const curContent = ref('')
const curArtId = ref(0)
const curType = ref(0)
const openMd = ref(false)
const curmdprivewTitile = ref('')
//条件渲染对象
const formInline = reactive({
  artTitle: '',
  artClassId: value,
  artRequestDay: 0,
  artRequestMonth: 0,
  artRequestTotal: 0,
  artAuther: '',
})

onMounted(() => {
  const curUserName = localStorage.getItem('curUser')
  curUserName == 'zsj' ? (isROOT.value = true) : (isROOT.value = false)
  flushARTList()
  getAllClass()
})

const handleClose = () => {
  ElMessageBox.confirm('你确定要取消编辑吗?').then(() => {
    openMd.value = false
    openjoinArtListDrawerstatus.value = false
  })
}

const openMdPrivew = row => {
  //打开markdow预览功能 把文本传进去
  // console.log(row);
  openMd.value = true
  curContent.value = row.artContent
  curmdprivewTitile.value = row.artTitle
  curArtId.value = row.id
}

const getAllClass = async () => {
  const { msg, data, code } = await getAllClassForELE()
  typeList.value = data
  // console.log(typeList.value)
}

const flushARTList = async () => {
  const { msg, data, code } = await getAllPageByIndex(
    pageParams.value.cur,
    pageParams.value.size,
    formInline
  )
  articleData.value = data.records
  total.value = data.total
}
const rebuildArtinfo = async () => {
  const { msg, code } = await rebuildArt({
    id: curArtId.value,
    artContent: curContent.value,
    artTitle: curmdprivewTitile.value,
  })
  if (code == 200) {
    flushARTList()
    ElMessage({
      type: 'success',
      message: msg,
    })
  }
}

const resetType = async row => {
  openUpType.value = true //打开对话框
  curType.value = row.artClassName
  curArtId.value = row.id
}

const resetArtType = async () => {
  if (value.value != undefined && value.value != '' && value.value != null) {
    const { msg, code } = await rebuildArtType({
      id: curArtId.value,
      artClassId: value.value,
    })
    if (code == 200) {
      ElMessage({
        type: 'success',
        message: '修改成功',
        duration: 1000,
      })
      //在刷新之前清空条件
      value.value = undefined
      flushARTList()
      openUpType.value = false
    }
  } else {
    ElMessage({
      type: 'error',
      message: '分类不能为空',
      duration: 500,
    })
  }
}

const cancelResetType = () => {
  openUpType.value = false
  flushARTList()
}
const openjoinArtListDrawerstatus = ref(false)
const curmdaddArtTitile = ref('')
const curAddContent = ref('')
const openjoinArtListDrawer = () => {
  //这里只是打开抽屉 并且初始化一些值
  openjoinArtListDrawerstatus.value = true
  //curmdaddArtTitile 新增的文章标题
  //curAddContent 新增的文章实体
}

const addARTINFOSUBMITNOW = async () => {
  //需要先校验
  if (curmdaddArtTitile.value == null || curmdaddArtTitile.value === '') {
    ElMessage({
      type: 'error',
      message: '文章标题不能为空',
    })
  } else if (
    curAddContent.value == null ||
    curAddContent.value == undefined ||
    curAddContent.value == ''
  ) {
    ElMessage({
      type: 'error',
      message: '你好歹写个标题赛',
    })
  } else if (
    value.value == null ||
    value.value == undefined ||
    value.value == ''
  ) {
    ElMessage({
      type: 'error',
      message: '文章分类不能为空',
    })
  } else {
    //发送网络请求
    const { msg, code } = await joinArtFamily({
      artTitle: curmdaddArtTitile.value,
      artContent: curAddContent.value,
      artClassId: value.value,
    })
    if (code == 200) {
      ElMessage({
        type: 'success',
        message: msg,
      })
      //在刷新之前清空条件
      value.value = undefined
      flushARTList()
      openjoinArtListDrawerstatus.value = false
    } else {
      ElMessage({
        type: 'error',
        message: msg,
      })
    }
  }
}

const rmArt = async row => {
  ElMessageBox.confirm('您将要删除此文章,是否确认操作')
    .then(async () => {
      const { msg, code } = await cancelArtFamilyById({ id: row.id })
      if (code == 200) {
        ElMessage({
          type: 'success',
          message: msg,
        })
        flushARTList()
      } else {
        ElMessage({
          type: 'success',
          message: msg,
        })
      }
    })
    .catch()
}
</script>

<style>
.demo-form-inline .el-input {
  --el-input-width: 220px;
}

.md_submit {
  margin-top: 30px;
}
.md_page_title {
  height: 60px;
  margin-bottom: 20px;
}
.add_page_title {
  height: 60px;
  width: 400px;
  margin-bottom: 30px;
}
</style>
