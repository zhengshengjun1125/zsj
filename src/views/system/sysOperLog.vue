<template>
  <div>
    <el-form :inline="true" :model="formInline" class="demo-form-inline">
      <el-form-item label="用户查询">
        <el-input v-model="formInline.username" placeholder="用户" clearable />
      </el-form-item>
      <el-form-item label="方法查询">
        <el-input v-model="formInline.method" placeholder="方法" clearable />
      </el-form-item>
      <el-form-item label="操作查询">
        <el-input v-model="formInline.operation" placeholder="操作" clearable />
      </el-form-item>
      <el-form-item label="IP查询">
        <el-input v-model="formInline.ip" placeholder="IP地址" clearable />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="initLogList">查询</el-button>
      </el-form-item>
    </el-form>
    <el-table :data="loglist" border style="width: 100%">
      <el-table-column prop="id" label="Id" width="60" />
      <el-table-column prop="username" label="操作用户" width="180" />
      <el-table-column prop="operation" label="操作名称" width="180" />
      <el-table-column prop="method" label="请求方法" width="180" />
      <el-table-column prop="params" label="参数" width="180" />
      <el-table-column prop="ip" label="Ip地址" />
      <el-table-column prop="createDate" label="创建时间" />
      <el-table-column label="操作" width="150" #default="scope">
        <el-button type="danger" size="small" @click="rmlogById(scope.row)">
          删除
        </el-button>
      </el-table-column>
    </el-table>

    <!--分页条-->
    <el-pagination
      v-model:current-page="pageParams.cur"
      v-model:page-size="pageParams.size"
      :page-sizes="[1, 5, 10, 20, 50, 100]"
      @size-change="initLogList"
      @current-change="initLogList"
      layout="total, sizes, prev, pager, next"
      :total="total"
    />
  </div>
</template>

<script setup>
import { async } from '@kangc/v-md-editor'
import { onMounted, ref, reactive } from 'vue'
import { getLogList } from '@/api/system'
import { ElMessage } from 'element-plus'

//条件渲染对象
const formInline = reactive({
  ip: '',
  username: '',
  operation: '',
  method: '',
})

const loglist = ref([])
onMounted(() => {
  initLogList()
})

const rmlogById = () => {
  //删除日志信息根据id
  ElMessage.warning('不支持的操作')
}

const initLogList = async () => {
  const { data, msg } = await getLogList(
    pageParams.value.cur,
    pageParams.value.size,
    formInline
  )

  if (msg === 'success') {
    loglist.value = data.records
    total.value = data.total
  } else {
    ElMessage.error(msg)
  }
}

const total = ref(0)
//分页页码以及当前页面显示页数
let pageParamsForm = {
  cur: 1,
  size: 10,
}
//将分页信息封装为响应式对象
const pageParams = ref(pageParamsForm)
</script>

<style></style>
