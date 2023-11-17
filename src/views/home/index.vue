<!--
 * @Descripttion: 
 * @version: 1.1
 *
 * @LastEditors:  zsj
 * 
 * @Author:   zsj

-->
<template>
  <div>
    <h1 class="animate__animated animate__backInRight">
      你好!我的朋友
      <span>（HELLO! MY FRIEND）&#128512;&#128512;&#128512;</span>
      (●'◡'●)
    </h1>
    <!-- <el-card class="box-card" shadow="always">
      <template #header>
        <div class="card-header">
          <span>最近日志</span>
        </div>
        <el-table :data="logList" border style="width: 100%">
          <el-table-column prop="operation" label="操作名称" width="180" />
          <el-table-column prop="method" label="请求方法" width="180" />
          <el-table-column prop="ip" label="Ip地址" />
        </el-table>
      </template>
    </el-card> -->
  </div>
</template>
<script setup>
import { gettenLogList } from '@/api/system'
import { async } from '@kangc/v-md-editor'
import { onMounted, ref } from 'vue'
const logList = ref([])

onMounted(() => {
  intTenLogList()
})

const intTenLogList = async () => {
  const { data } = await gettenLogList()
  logList.value = data
}
</script>

<style scoped>
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card-header span {
  font-weight: bold;
  margin-left: 180px;
  margin-bottom: 20px;
}
.text {
  font-size: 8px;
}

.item {
  margin-bottom: 18px;
}

.box-card {
  width: 480px;
}
</style>
