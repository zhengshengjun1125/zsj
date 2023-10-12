<template>
  <div>
    <el-form :inline="true" :model="formInline" class="demo-form-inline">
      <el-form-item label="用户名称">
        <el-input
          v-model="formInline.username"
          placeholder="用户名称模糊查询"
          clearable
        />
      </el-form-item>
      <el-form-item label="用户邮箱">
        <el-input
          v-model="formInline.email"
          placeholder="用户邮箱模糊查询"
          clearable
        />
      </el-form-item>

      <el-form-item label="用户电话">
        <el-input
          v-model="formInline.mobile"
          placeholder="用户电话模糊查询"
          clearable
        />
      </el-form-item>
      <el-form-item label="用户角色">
        <el-select v-model="formInline.roleId" placeholder="用户角色" clearable>
          <el-option
            v-for="item in rolesList"
            :key="item.value"
            :label="item.roleName"
            :value="item.id"
          />
        </el-select>
      </el-form-item>

      <el-form-item>
        <el-button type="primary" @click="flushUserList">查询</el-button>
      </el-form-item>

      <el-form-item>
        <el-button type="success" @click="addUserdialogVisible = true">
          添加
        </el-button>
      </el-form-item>
    </el-form>

    <!-- 添加用户对话框 
    todo
   -->
    <el-dialog
      v-model="addUserdialogVisible"
      title="添加用户"
      width="30%"
      draggable
    >
      <el-form label-width="120px">
        <el-form-item label="角色名称">
          <el-input v-model="addUserInfo.username" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="addUser">提交</el-button>
          <el-button @click="addUserdialogVisible = false">取消</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>

    <el-table :data="userList" style="width: 100%">
      <el-table-column prop="id" label="Id" width="180" />
      <el-table-column prop="username" label="账号" width="180" />
      <el-table-column prop="avatar" label="头像" width="180">
        <template #default="scope">
          <el-image
            style="width: 70px; height: 70px"
            :src="scope.row.avatar"
            alt=""
            :fit="fill"
          ></el-image>
        </template>
      </el-table-column>
      <el-table-column prop="roleName" label="角色名称" width="180" />
      <el-table-column prop="email" label="邮箱" width="180" />
      <el-table-column prop="mobile" label="手机号" />
      <el-table-column prop="createTime" label="创建时间" />
    </el-table>

    <!--分页条-->
    <el-pagination
      v-model:current-page="pageParams.cur"
      v-model:page-size="pageParams.size"
      :page-sizes="[1, 5, 10, 20, 50, 100]"
      @size-change="flushUserList"
      @current-change="flushUserList"
      layout="total, sizes, prev, pager, next"
      :total="total"
    />
  </div>
</template>

<script setup>
import { onMounted, reactive, ref } from 'vue'
import { getAllUser } from '@/api/user'
import { getAllRoleByIndex } from '@/api/system'

const addUserInfo = ref({
  username: '',
})

const userList = ref([])
//分页总条数
let total = ref(0)
const addUserdialogVisible = ref(false)
const rolesList = ref([])

//分页页码以及当前页面显示页数
let pageParamsForm = {
  cur: 1,
  size: 10,
}
//将分页信息封装为响应式对象
const pageParams = ref(pageParamsForm)

onMounted(() => {
  flushUserList()
  flushRoleList()
})

//条件渲染对象
const formInline = reactive({
  username: '',
  email: '',
  mobile: '',
  roleId: null,
})

const addUser = async () => {
  console.log('添加用户')
}

//发起请求获取数据刷新列表的方法
const flushUserList = async () => {
  const { msg, code, data } = await getAllUser(
    pageParams.value.cur,
    pageParams.value.size,
    formInline
  )
  if (code == 200) {
    userList.value = data.list.map(user => {
      user.createTime = user.createTime.substring(0, 10)
      return user
    })
    total.value = data.totalCount
  }
}
//刷新角色列表
const flushRoleList = async () => {
  const { msg, data, code } = await getAllRoleByIndex()
  rolesList.value = data
}
</script>

<style>
.demo-form-inline .el-input {
  --el-input-width: 220px;
}
</style>
