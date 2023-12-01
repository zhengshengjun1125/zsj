<template>
  <audio
    class="audio animate__animated animate__bounceInDown"
    id="audio"
    controls=""
    preload="none"
    :src="curMusicUrl"
    autoplay
    @ended="next"
  ></audio>
  <span style="color: red">{{ $t('public.songtips') }}</span>
  <el-form
    :inline="true"
    :model="params"
    class="demo-form-inline animate__animated animate__bounceInRight"
  >
    <el-form-item :label="$t('public.musicname')">
      <el-input
        v-model="params.fileName"
        :placeholder="$t('public.musicname')"
        @input="getMusic"
        clearable
      />
    </el-form-item>
    <el-form-item>
      <el-switch
        v-model="isSelfValue"
        class="mb-2"
        :active-text="$t('public.mymusic')"
        :inactive-text="$t('public.allmusic')"
        @change="changeStatus"
      />
    </el-form-item>
  </el-form>

  <el-table
    class="animate__animated animate__bounceInRight"
    :data="songs"
    stripe
    style="width: 100%"
  >
    <el-table-column prop="fileName" :label="$t('public.songname')" />
    <el-table-column prop="affiliation" :label="$t('public.musicer')" />
    <el-table-column :label="$t('public.oper')" width="200" #default="scope">
      <el-button type="success" size="large" @click="play(scope.row)">
        {{ $t('public.play') }}
      </el-button>
    </el-table-column>
  </el-table>
</template>

<script setup>
import { getCurrentInstance, onMounted, reactive, ref } from 'vue'
import { getMusicList } from '@/api/system'
import { ElMessage } from 'element-plus'
const { proxy: ctx } = getCurrentInstance()
const curMusicUrl = ref('')
const songs = ref()
const isSelfValue = ref(false)
const params = ref({
  isSelf: 0,
  fileName: '',
})
const total = ref(0)
const curMusicId = ref()
const musics = reactive(new Map())
const musics_url = reactive(new Map())
onMounted(() => {
  getMusic()
})
const getMusic = async () => {
  const { data, msg } = await getMusicList(params.value)
  songs.value = data
  total.value = data.length
  musics.clear()
  musics_url.clear()
  for (let i = 0; i < data.length; i++) {
    musics.set(data[i].url, i)
  }
  for (let i = 0; i < data.length; i++) {
    musics_url.set(i, data[i].url)
  }
}
const play = e => {
  // console.log(e.url)
  const index = musics.get(e.url)
  curMusicUrl.value = e.url
  curMusicId.value = index
}

const changeStatus = e => {
  if (e) {
    params.value.isSelf = 1
  } else {
    params.value.isSelf = 0
  }
  getMusic()
}

const next = () => {
  let index = curMusicId.value
  let next = (index + 1) % total.value
  const next_url = musics_url.get(next)
  curMusicUrl.value = next_url
  curMusicId.value = next
  const audio = document.getElementById('audio')
  audio.play()
}
</script>
<style>
.audio {
  width: 100%;
}
</style>
