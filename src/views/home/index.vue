<template>
  <div>
    <h1
      style="text-align: center"
      class="animate__animated animate__backInRight"
      v-show="sayHello"
    >
      你好!我的朋友
      <span style="color: red">({{ curUser.username }})</span>
      <span>（HELLO! MY FRIEND）&#128512;&#128512;&#128512;</span>
      (●'◡'●)
    </h1>
    <div v-show="!sayHello" class="animate__animated animate__slideInUp">
      <el-row :gutter="20">
        <el-col :span="12">
          <div class="grid-content ep-bg-purple">
            <el-avatar
              class="userAvatar"
              :size="300"
              :src="curUser.avatar"
              @error="errorHandler"
              style="margin-left: 100px"
            >
              <img :src="curUser.avatar" />
            </el-avatar>
            <el-card class="box-card" style="margin-left: 10px">
              <div style="font-size: 25px" class="userTextInfo">
                <span style="color: red">
                  <span style="color: blue">{{ $t('public.account') }}</span>
                  : {{ curUser.username }}
                </span>
                <br />
                <span style="color: #73bb5e">
                  <span style="color: black">{{ $t('public.balance') }}</span>
                  : {{ curUser.balance }}
                </span>
                {{ $t('public.zcoin') }}
                <br />
                <span style="color: green">
                  <span style="color: #d8d41b">{{ $t('public.role') }}</span>
                  : {{ curUser.roleName }}
                </span>
                <br />
                <span style="color: pink">
                  <span style="color: #5aa8e3">{{ $t('public.email') }}</span>
                  : {{ curUser.email }}
                </span>
                <br />
                <span style="color: #1f1f1f">
                  <span style="color: #e4c79e">{{ $t('public.curTime') }}</span>
                  : {{ time }}
                </span>
              </div>
            </el-card>
            <el-timeline style="margin-top: 10px">
              <el-timeline-item
                v-for="(activity, index) in activities"
                :key="index"
                :icon="activity.icon"
                :type="activity.type"
                :color="activity.color"
                :size="activity.size"
                :hollow="activity.hollow"
                :timestamp="activity.timestamp"
              >
                {{ activity.content }}
              </el-timeline-item>
            </el-timeline>
          </div>
        </el-col>
        <el-col :span="12">
          <div class="grid-content ep-bg-purple" />
          <el-carousel :interval="4000" type="card" height="200px">
            <el-carousel-item v-for="item in canner" :key="item">
              <img
                style="background-size: cover; width: 320px; height: 200px"
                :src="item"
                @click="jump(item)"
              />
            </el-carousel-item>
          </el-carousel>
          <el-calendar v-model="curDate" />
        </el-col>
      </el-row>
    </div>
  </div>
</template>
<script setup>
import { gettenLogList } from '@/api/system'
import { async } from '@kangc/v-md-editor'
import { onMounted, ref, getCurrentInstance } from 'vue'
import { useAccount } from '@/pinia/modules/account'
import { MoreFilled } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
const { proxy: ctx } = getCurrentInstance()
const logList = ref([])
const curUser = ref({})
const sayHello = ref(true)
const curDate = ref(new Date())
const time = ref()
const canner = [
  'https://image-zsj.oss-cn-beijing.aliyuncs.com/giteeSvg.svg',
  'https://image-zsj.oss-cn-beijing.aliyuncs.com/githubsvg.svg',
  'https://image-zsj.oss-cn-beijing.aliyuncs.com/logo.svg',
]
const activities = [
  {
    content: ctx.$t('public.startTime'),
    timestamp: '2023-11-12',
    size: 'large',
    type: 'primary',
    icon: MoreFilled,
  },
  {
    content: ctx.$t('public.author'),
    timestamp: ctx.$t('public.authorname'),
    type: 'primary',
    hollow: true,
  },
  {
    content: ctx.$t('public.Projectprototype'),
    timestamp: '2023-11-18',
    size: 'large',
    color: '#0bbd87',
    type: 'primary',
  },
]
var timer = 0
const getTime = () => {
  time.value = new Date().toLocaleString()
}
onMounted(() => {
  time.value = new Date().toLocaleString()
  const { userinfo } = useAccount()
  // console.log(userinfo)
  curUser.value = userinfo
  setInterval(() => {
    getTime()
  }, 1000)
  setTimeout(() => {
    sayHello.value = false
  }, 3000)
})

const jump = e => {
  if (e.search('gitee') != -1) {
    window.open('https://gitee.com/zhengshengjun/zsj')
  }
  if (e.search('github') != -1) {
    window.open('https://github.com/zhengshengjun1125/zsj')
  }
  if (e.search('logo') != -1) {
    ElMessage.success(ctx.$t('public.tkutips'))
  }
}
</script>

<style scoped>
.userTextInfo {
  font-family: fantasy;
}
.box-card {
  width: 480px;
}
.el-row {
  margin-bottom: 20px;
}
.el-row:last-child {
  margin-bottom: 0;
}
.el-col {
  border-radius: 4px;
}

.grid-content {
  border-radius: 4px;
  min-height: 36px;
}
.userAvatar:hover {
  transform: rotateY(180deg);
}
.el-carousel__item h3 {
  color: #475669;
  opacity: 0.75;
  line-height: 200px;
  margin: 0;
  text-align: center;
}

.el-carousel__item:nth-child(2n) {
  background-color: #99a9bf;
}

.el-carousel__item:nth-child(2n + 1) {
  background-color: #d3dce6;
}
</style>
