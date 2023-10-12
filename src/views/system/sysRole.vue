<template>
  <div class="search-div">
    <!-- 搜索表单 -->
    <el-form label-width="70px" size="small">
      <el-form-item label="角色名称">
        <el-input
          v-model="queryDto.roleName"
          style="width: 100%"
          placeholder="角色名称"
        ></el-input>
      </el-form-item>
      <el-row style="display: flex">
        <el-button type="primary" size="small" @click="searchRole">
          搜索
        </el-button>
        <el-button size="small" @click="resetData">重置</el-button>
      </el-row>
    </el-form>

    <!-- 添加按钮 -->
    <div class="tools-div">
      <el-button
        type="success"
        size="small"
        @click="addRoledialogVisible = true"
      >
        添 加
      </el-button>
    </div>

    <!-- 添加用户对话框 -->
    <el-dialog
      v-model="addRoledialogVisible"
      title="添加角色"
      width="30%"
      draggable
    >
      <el-form label-width="120px">
        <el-form-item label="角色名称">
          <el-input v-model="addRoleName" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="addRole">提交</el-button>
          <el-button @click="addRoledialogVisible = false">取消</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>

    <!-- 修改用户对话框 -->
    <el-dialog
      v-model="updateRoledialogVisible"
      title="修改角色"
      width="30%"
      draggable
    >
      <el-form label-width="120px">
        <el-form-item label="角色名称">
          <el-input v-model="curRole.roleName" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="updateRoleSubmit">提交</el-button>
          <el-button @click="cancelUpdateRole">取消</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>

    <!--- 角色表格数据 -->
    <el-table :data="list" style="width: 100%">
      <el-table-column prop="roleName" label="角色名称" width="180" />
      <el-table-column prop="createTime" label="创建时间" />
      <el-table-column label="操作" align="center" width="280" #default="scope">
        <el-button type="primary" size="small" @click="updateRole(scope.row)">
          修改
        </el-button>
        <el-button type="danger" size="small" @click="removeRole(scope.row)">
          删除
        </el-button>
      </el-table-column>
    </el-table>

    <!--分页条-->
    <el-pagination
      v-model:current-page="pageParams.cur"
      v-model:page-size="pageParams.size"
      :page-sizes="[5, 10, 20, 50, 100]"
      @size-change="flushRoleList"
      @current-change="flushRoleList"
      layout="total, sizes, prev, pager, next"
      :total="total"
    />
  </div>
</template>
<script setup>
import { getCurrentInstance, onMounted, ref } from 'vue'
import {
  getrole,
  addRoleInfo,
  removeRoleById,
  updateRoleById,
} from '@/api/system'
import { ElMessage, ElMessageBox } from 'element-plus'

const curRole = ref({})
const addRoledialogVisible = ref(false)
const updateRoledialogVisible = ref(false)
const { proxy: ctx } = getCurrentInstance()

onMounted(() => {
  //执行数据读取
  flushRoleList()
})

const cancelUpdateRole = () => {
  updateRoledialogVisible.value = false
  flushRoleList()
}

//删除
const removeRole = row => {
  ElMessageBox.confirm('此操作将永久删除该记录, 是否继续?', 'Warning', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning',
  }).then(async () => {
    const { code } = await removeRoleById({
      id: row.id,
    })
    if (code === 200) {
      ElMessage.success('删除成功')
      pageParams.value.page = 1
      flushRoleList()
    }
  })
}
const updateRoleSubmit = async row => {
  const { code, msg } = await updateRoleById({
    id: curRole.value.id,
    roleName: curRole.value.roleName,
  })
  if (code == 200) {
    //说明修改成功了
    ctx.$message.success('修改成功')
    updateRoledialogVisible.value = false // 关闭对话框
  } else {
    ctx.$message.error(msg)
  }
}
//修改
const updateRole = async row => {
  updateRoledialogVisible.value = true //开启对话框
  curRole.value = row
  const curName = row.roleName
  curRole.value.roleName = curName
}

//重置
const resetData = () => {
  queryDto.value.roleName = ''
  flushRoleList()
}

let addRoleName = ref()

let pageParamsForm = {
  cur: 1,
  size: 10,
}

const queryDto = ref({
  roleName: '',
})
//添加角色信息
const addRole = async () => {
  const { msg, code } = await addRoleInfo({
    roleName: addRoleName.value,
  })
  if (msg == '添加成功' && code == 200) {
    flushRoleList() //重新刷新列表
    addRoledialogVisible.value = false // 关闭添加对话框
    ctx.$message.success(msg)
  } else {
    ctx.$message.error(msg)
  }
}
//搜索用户信息
const searchRole = () => {
  flushRoleList()
}
//将分页信息封装为响应式对象
const pageParams = ref(pageParamsForm)

// 分页条总记录数
let total = ref(0)

// 定义表格数据模型
let list = ref([])

const flushRoleList = async () => {
  const { data, msg, code } = await getrole(
    pageParams.value.cur,
    pageParams.value.size,
    queryDto.value
  )
  list.value = data.records
  total.value = data.total
}
</script>

<style scoped>
.search-div {
  margin-bottom: 10px;
  padding: 10px;
  border: 1px solid #ebeef5;
  border-radius: 3px;
  background-color: #fff;
}

.tools-div {
  margin: 10px 0;
  padding: 10px;
  border: 1px solid #ebeef5;
  border-radius: 3px;
  background-color: #fff;
}
</style>
