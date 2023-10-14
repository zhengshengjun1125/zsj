<template>
  <div>
    <el-tree
      :data="TypeData"
      :props="defaultProps"
      @node-click="handleNodeClick"
      node-key="id"
      :expand-on-click-node="false"
    >
      <template #default="{ node, data }">
        <div class="box"></div>
        <span class="custom-tree-node">
          <span>{{ node.label }}</span>
          <span>
            <a style="margin-left: 900px" @click="append(data)">追加</a>
            <a style="margin-left: 8px;color:red" @click="remove(node, data)">
              删除
            </a>
          </span>
        </span>
      </template>
    </el-tree>
  </div>
</template>

<script setup>
import { getAllClassForELE } from '@/api/type'
import { ElMessageBox } from 'element-plus'
import { onMounted, ref } from 'vue'

const handleNodeClick = data => {
  console.log(data)
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
  console.log(data)
  //发送请求 点出来一个对话框然后设置值 以当前用户作为作者
}
const remove = (d, n) => {
  ElMessageBox.confirm('如果当前树下有子节点也会被一并删除,是否确认操作?')
  console.log(d)
  console.log(n)
  //同样是发送请求 直接删除当前id的分类 包括所有他的子分类 都将status改为0
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
