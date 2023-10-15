<template>
  <div>
    <el-button
      style="margin-bottom: 20px"
      type="success"
      @click="clientRootType"
    >
      添加分类
    </el-button>

    <!-- 添加根分类 -->
    <el-dialog
      v-model="OpenaddRootTypeDialog"
      title="添加根节点"
      width="50%"
      draggable
    >
      <el-form label-width="120px">
        <el-form-item label="节点名称">
          <el-input v-model="addroottypeName"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="setRootType">提交</el-button>
          <el-button @click="OpenaddRootTypeDialog = false">取消</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>

    <!-- 追加分类 -->
    <el-dialog
      v-model="openappendTypeByCurIDDialog"
      title="追加节点"
      width="50%"
      draggable
    >
      <el-form label-width="120px">
        <el-form-item label="节点名称">
          <el-input v-model="appendtypeName"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submitAppend">提交</el-button>
          <el-button @click="openappendTypeByCurIDDialog = false">
            取消
          </el-button>
        </el-form-item>
      </el-form>
    </el-dialog>

    <el-tree
      :data="TypeData"
      :props="defaultProps"
      @node-click="handleNodeClick"
      node-key="id"
      default-expand-all
      :expand-on-click-node="false"
    >
      <template #default="{ node, data }">
        <div class="box"></div>
        <span class="custom-tree-node">
          <span>{{ node.label }}</span>
          <span>
            <a style="margin-left: 900px" @click="append(data)">追加</a>
            <a style="margin-left: 8px; color: red" @click="remove(node, data)">
              删除
            </a>
          </span>
        </span>
      </template>
    </el-tree>
  </div>
</template>

<script setup>
import {
  getAllClassForELE,
  setRootClass,
  cancelClassById,
  appendClass,
} from '@/api/type'
import { async } from '@kangc/v-md-editor'
import { ElMessage, ElMessageBox } from 'element-plus'
import { onMounted, ref } from 'vue'
const addroottypeName = ref('')
const appendtypeName = ref('')
const OpenaddRootTypeDialog = ref(false)
const openappendTypeByCurIDDialog = ref(false)
const appendFatherId = ref(0)

const handleNodeClick = data => {
  // console.log(data)
}
const TypeData = ref([])
onMounted(() => {
  gtl()
})

const gtl = async () => {
  const { data, code, msg } = await getAllClassForELE()
  TypeData.value = data
}
const defaultProps = {
  children: 'children',
  label: 'label',
}
// 在当前节点下面添加他的子节点
const append = data => {
  openappendTypeByCurIDDialog.value = true
  appendFatherId.value = data.value
  console.log(data.value) //我们要添加的父亲节点的id 也就是设置他的父亲节点为这个value 并且设置classname为用户输入的值 并且设置作者为当前请求用户
}

//提交追加数据
const submitAppend = async () => {
  if (
    appendtypeName.value == '' ||
    appendtypeName.value == null ||
    appendtypeName.value == undefined
  ) {
    ElMessage({
      type: 'error',
      message: '内容不能为空',
    })
  } else {
    const { msg, code } = await appendClass({
      classFatherId: appendFatherId.value,
      className: appendtypeName.value,
    })
    if (code == 200) {
      ElMessage({
        type: 'success',
        message: msg,
      })
      //清空并且刷新列表
      appendtypeName.value = ''
      gtl()
      openappendTypeByCurIDDialog.value = false
    } else {
      ElMessage({
        type: 'error',
        message: msg,
      })
    }
  }
}

const remove = (d, n) => {
  ElMessageBox.confirm('如果当前树下有子节点也会被一并删除,是否确认操作?').then(
    async () => {
      //同样是发送请求 直接删除当前id的分类 包括所有他的子分类 都将status改为0
      const { msg, code } = await cancelClassById({
        id: n.value,
      })
      if (code == 200) {
        ElMessage({
          type: 'success',
          message: msg,
        })
        gtl()
      } else {
        ElMessage(msg)
      }
    }
  )
}

const clientRootType = () => {
  OpenaddRootTypeDialog.value = true
}

const setRootType = async () => {
  //发送请求 然后刷新列表
  if (
    addroottypeName.value == '' ||
    addroottypeName.value == null ||
    addroottypeName.value == undefined
  ) {
    ElMessage({
      type: 'error',
      message: '名称不能为空',
    })
  } else {
    const { msg, code } = await setRootClass({
      className: addroottypeName.value,
    })
    if (code == 200) {
      OpenaddRootTypeDialog.value = false
      gtl()
    } else {
      ElMessage(msg)
    }
  }
}
</script>

<style>
.custom-tree-node {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-size: 14px;
  padding-right: 8px;
}
.options {
  margin-left: 20px;
}
.box {
  width: 50px;
}
</style>
