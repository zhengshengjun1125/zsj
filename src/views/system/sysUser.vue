<template>
  <div>
    <el-form :inline="true" :model="formInline" class="demo-form-inline">
      <el-form-item label="用户名称">
        <el-input
          v-model="formInline.username"
          placeholder="用户名称模糊查询"
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
        <el-button
          type="primary"
          @click="flushUserList"
          class="animate__animated animate__backInRight"
        >
          查询
        </el-button>
      </el-form-item>

      <el-form-item>
        <el-button
          type="success"
          @click="addUserdialogVisible = true"
          class="animate__animated animate__backInRight"
        >
          添加
        </el-button>
      </el-form-item>

      <el-form-item>
        <el-button
          type="primary"
          @click="exportUser"
          class="animate__animated animate__backInRight"
        >
          导出
        </el-button>
      </el-form-item>
    </el-form>

    <el-dialog v-model="dialogVisible">
      <img w-full :src="dialogImageUrl" alt="Preview Image" />
    </el-dialog>

    <!-- 抽屉 修改用户信息 -->
    <el-drawer v-model="osdateDrawer" title="修改用户信息">
      <div>
        <el-form label-width="120px">
          <el-form-item label="头像">
            <el-upload
              :data="policyInfo"
              class="avatar-uploader"
              action="https://request-oss-zsj.oss-cn-beijing.aliyuncs.com"
              :show-file-list="false"
              :before-upload="beforceUpload"
              :on-success="handleAvatarSuccess"
              :on-progress="handleAvatarProgress"
            >
              <img
                v-if="curUpObject.avatar"
                :src="curUpObject.avatar"
                class="avatar"
              />
              <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
            </el-upload>
          </el-form-item>

          <el-form-item label="角色名称">
            <el-select v-model="curUpObject.roleName" placeholder="角色">
              <el-option
                v-for="item in rolesListOptions"
                :key="item.value"
                :label="item.roleName"
                :value="item.id"
              />
            </el-select>
            <el-text class="mx-1 up_tips" type="danger">
              ↑此操作降级后无法撤回
            </el-text>
          </el-form-item>

          <el-form-item label="邮箱地址">
            <el-input
              v-model="curUpObject.email"
              placeholder="请输入邮箱地址"
            />
          </el-form-item>

          <el-form-item label="手机号">
            <el-input
              v-model="curUpObject.mobile"
              placeholder="请输入手机号码"
              maxlength="11"
              show-word-limit
            />
          </el-form-item>

          <el-form-item>
            <el-button type="primary" @click="submitUpdateUser">确认</el-button>
            <el-button @click="cancelUpdateUserInfo">取消</el-button>
          </el-form-item>
        </el-form>
      </div>
    </el-drawer>

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
        <el-form-item label="头像">
          <el-upload
            :data="policyInfo"
            class="avatar-uploader"
            action="https://request-oss-zsj.oss-cn-beijing.aliyuncs.com"
            :show-file-list="false"
            :before-upload="beforceUpload"
            :on-success="handleAvatarSuccess"
            :on-progress="handleAvatarProgress"
          >
            <img
              v-if="addUserInfo.avatar"
              :src="addUserInfo.avatar"
              class="avatar"
            />
            <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
          </el-upload>
        </el-form-item>

        <el-form-item label="账号">
          <el-input
            v-model="addUserInfo.username"
            placeholder="请输入未注册账号"
            minlength="3"
          />
        </el-form-item>
        <el-form-item label="密码">
          <el-input
            minlength="5"
            v-model="addUserInfo.password"
            type="password"
            placeholder="请输入你的账号密码"
            show-password
          />
        </el-form-item>
        <el-form-item label="确认密码">
          <el-input
            minlength="5"
            v-model="checkPass"
            type="password"
            placeholder="再次输入确认密码"
            show-password
          />
        </el-form-item>

        <el-form-item label="角色名称">
          <el-select v-model="addUserInfo.roleId" placeholder="角色" clearable>
            <el-option
              v-for="item in rolesListOptions"
              :key="item.value"
              :label="item.roleName"
              :value="item.id"
            />
          </el-select>
        </el-form-item>

        <el-form-item label="邮箱地址">
          <el-input v-model="addUserInfo.email" placeholder="请输入邮箱地址" />
        </el-form-item>

        <el-form-item label="手机号">
          <el-input
            v-model="addUserInfo.mobile"
            placeholder="请输入手机号码"
            maxlength="11"
            show-word-limit
          />
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="addUser">添加</el-button>
          <el-button @click="addUserdialogVisible = false">取消</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>

    <!--   class="animate__animated animate__backInUp" -->
    <el-table
      @selection-change="handleSelectionChange"
      :data="userList"
      border
      style="width: 100%"
    >
      <el-table-column type="selection" width="55" />
      <el-table-column prop="id" label="Id" width="100" />
      <el-table-column prop="username" label="账号" width="180" />
      <el-table-column prop="avatar" label="头像" width="180">
        <template #default="scope">
          <el-image
            style="width: 70px; height: 70px"
            :src="scope.row.avatar"
            alt="帅气头像哦"
            fit="fill"
            preview-teleported="true"
            :preview-src-list="[scope.row.avatar]"
          ></el-image>
        </template>
      </el-table-column>
      <el-table-column prop="roleName" label="角色名称" width="180" />
      <el-table-column prop="email" label="邮箱" width="180" />
      <el-table-column prop="mobile" label="手机号" />
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

    <!--分页条class="animate__animated animate__flip"-->
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
import { getCurrentInstance, onMounted, reactive, ref } from 'vue'
import {
  getAllUser,
  registerUser,
  cancellationUser,
  upgradeUserInfo,
  exportUserListExcel,
} from '@/api/user'
import { getAllRoleByIndex } from '@/api/system'
import { OssPolicyToPhoto } from '@/api/oss'
import { isAssetTypeAnImage } from '@/utils/format'
import { ElMessage, ElMessageBox } from 'element-plus'
//当前实例
const { proxy: ctx } = getCurrentInstance()
//抽屉
const osdateDrawer = ref(false)
const curUpObject = ref({})
//分页页码以及当前页面显示页数
let pageParamsForm = {
  cur: 1,
  size: 10,
}
//将分页信息封装为响应式对象
const pageParams = ref(pageParamsForm)
const policyInfo = ref({
  // key表示上传到Bucket内的Object的完整路径，例如exampledir/exampleobject.txtObject，完整路径中不能包含Bucket名称。
  // filename表示待上传的本地文件名称。
  key: '',
  policy: '',
  OSSAccessKeyId: '',
  // 设置服务端返回状态码为200，不设置则默认返回状态码204。
  success_action_status: '200',
  signature: '',
})
const addUserInfo = ref({
  username: '',
  roleId: 1,
  avatar: '',
  email: '',
  mobile: '',
  password: '',
})
const previewAvatar = ref([])
const curAvatarUrl = ref('')
const checkPass = ref()
const userList = ref([])
//分页总条数
let total = ref(0)
const addUserdialogVisible = ref(false)
let rolesListOptions = ref([])
const rolesList = ref([])
//上传头像之前的接口
const beforceUpload = async file => {
  let file_name = file.name
  const suffix = file_name.substring(file_name.lastIndexOf('.') + 1)
  if (!isAssetTypeAnImage(suffix)) {
    ElMessage.error('文件格式必须是图片哦！')
    return false
  }
  const response = await OssPolicyToPhoto()
  policyInfo.value.policy = response.data.policy
  policyInfo.value.signature = response.data.signature
  policyInfo.value.OSSAccessKeyId = response.data.accessid
  const uuid = response.data.uuid
  policyInfo.value.key = response.data.dir + uuid + file.name
  const url = response.data.host + '/' + policyInfo.value.key
  curAvatarUrl.value = url
}

//取消之后的回调
const cancelUpdateUserInfo = () => {
  osdateDrawer.value = false
  flushUserList()
}
//上传成功之后的回调
const handleAvatarSuccess = (r, u) => {
  addUserInfo.value.avatar = curAvatarUrl.value
  curUpObject.value.avatar = curAvatarUrl.value
}

onMounted(() => {
  flushUserList()
  flushRoleList()
  flushRoleOptionsList()
})

//条件渲染对象
const formInline = reactive({
  username: '',
  email: '',
  mobile: '',
  roleId: null,
})

//按下添加按钮后的回调
const addUser = async () => {
  //先对数据进行校验判空 前端不负责对数据的合法性检验 因为我不会哈哈
  //八重金锁 if大法
  if (addUserInfo.value.username == null || addUserInfo.value.username == '') {
    //说明用户名称为空了
    ctx.$message.error('账号不能为空')
  } else if (addUserInfo.value.roleId == null) {
    ctx.$message.error('用户角色不能为空')
  } else if (
    addUserInfo.value.avatar == null ||
    addUserInfo.value.avatar == ''
  ) {
    ctx.$message.error('请上传头像')
  } else if (addUserInfo.value.email == null || addUserInfo.value.email == '') {
    ctx.$message.error('请输入邮箱')
  } else if (
    addUserInfo.value.mobile == null ||
    addUserInfo.value.mobile == ''
  ) {
    ctx.$message.error('请输入手机号码')
  } else if (
    addUserInfo.value.password == null ||
    addUserInfo.value.password == ''
  ) {
    ctx.$message.error('请输入密码')
  } else if (checkPass.value != addUserInfo.value.password) {
    ctx.$message.error('两次输入的密码不同')
  } else {
    //你过了 你终于过来了！！！！
    //所有数据到后端校验合法性
    const { code, msg } = await registerUser(addUserInfo.value)
    if (code == 200) {
      ctx.$message.success('添加成功')
      addUserdialogVisible.value = false // 关闭对话框
      flushUserList()
    } else {
      ctx.$message.error(msg)
    }
  }
}

//发起请求获取数据刷新列表的方法
const flushUserList = async () => {
  const { code, data } = await getAllUser(
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
  const { data, code } = await getAllRoleByIndex()
  rolesList.value = data
  rolesList.value.push({
    id: null,
    roleName: '所有',
  })
}

//刷新角色列表
const flushRoleOptionsList = async () => {
  const { msg, data, code } = await getAllRoleByIndex()
  rolesListOptions.value = data
  // console.log(rolesListOptions.value);
}

const rmUserById = async row => {
  ElMessageBox.confirm(
    '您将指派Killer杀死此名用户,此操作会永远抹除此名用户,是否确认操作?',
    '高危操作提醒',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    }
  )
    .then(async () => {
      const { msg, code } = await cancellationUser({ id: row.id })
      if (code == 200) {
        flushUserList()
        ElMessage({
          type: 'success',
          message: msg,
        })
      } else {
        ElMessage({
          type: 'error',
          message: msg,
        })
      }
    })
    .catch(() => {
      ElMessage({
        type: 'info',
        message: '杀手已经回家~',
      })
    })
}

const upUserById = async row => {
  osdateDrawer.value = true
  curUpObject.value = row
  // console.log(curUpObject.value)
}
const submitUpdateUser = async () => {
  //将表单数据提交
  //说明当前角色没变过
  if (typeof curUpObject.value.roleName == 'string') {
    const rolelist = rolesListOptions.value
    for (let i = 0; i < rolelist.length; i++) {
      const cur = rolelist[i]
      if (curUpObject.value.roleName == cur.roleName) {
        //修改为这个id
        curUpObject.value.roleName = cur.id
      }
    }
  }
  const { msg } = await upgradeUserInfo(curUpObject.value)
  if (msg == '修改成功') {
    //关闭弹窗并且更新列表
    ElMessage({
      type: 'success',
      message: msg,
    })
    osdateDrawer.value = false
    flushUserList()
  } else {
    ElMessage({
      type: 'error',
      message: msg,
    })
  }
}
const selectionUserList = ref([])
//选项改变的时候
const handleSelectionChange = e => {
  selectionUserList.value = e
}
//导出当前选择的用户列表
const exportUser = async () => {
  if (selectionUserList.value.length == 0) {
    ElMessage.warning('请选中之后再进行导出')
  } else {
    ElMessageBox.confirm('您将会导出选择用户信息列表Excel,确认?', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    })
      .then(async () => {
        const res = await exportUserListExcel({
          list: selectionUserList.value,
        })
        // 生成地址，形如图 2
        const url = window.URL.createObjectURL(
          new Blob([res], { type: '.xlsx' })
        )
        // 创建标签a
        let a = document.createElement('a')
        // 设置隐藏
        a.style.display = 'none'
        // 给a标签地址
        a.href = url
        // 设置属性download和值，这是个特殊属性，参照图 3
        a.setAttribute('download', `用户列表.xlsx`)
        // 把a标签放入body，创建后的 a 标签，形如图 4
        document.body.appendChild(a)
        // 调用手动点击事件，相当于你做了一次点击
        a.click()
        // 释放该文件引用地址，当你结束使用某个 URL 对象之后，应该调用这个方法来让浏览器知道不用在内存中继续保留对这个文件的引用了(如果该页面有很多文件通过这种方法下载，这一步清理引用对性能优化尤为重要)
        window.URL.revokeObjectURL(url)
        // 移除a标签
        document.body.removeChild(a)
        ElMessage({
          type: 'success',
          message: '导出成功',
        })
      })
      .catch(() => {
        ElMessage({
          type: 'info',
          message: '取消导出',
        })
      })
  }
}
</script>

<style>
.demo-form-inline .el-input {
  --el-input-width: 220px;
}

.avatar-uploader .avatar {
  width: 178px;
  height: 178px;
  display: block;
}

.avatar-uploader .el-upload {
  border: 1px dashed var(--el-border-color);
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: var(--el-transition-duration-fast);
}

.avatar-uploader .el-upload:hover {
  border-color: var(--el-color-primary);
}

.el-icon.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  text-align: center;
}
.up_tips {
  color: red;
}
</style>
