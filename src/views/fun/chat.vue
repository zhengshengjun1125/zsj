<template>
  <div style="padding: 10px; margin-bottom: 50px">
    <el-row>
      <el-col :span="8">
        <!--
           TODO
           需要有一个添加好友的功能
           做成用户列表 支持滚动 由左边头像 右边名称来进行显示
           消息发送支持表情
         -->
        <el-card style="width: 100%; min-height: 300px; color: #333">
          <div style="padding-bottom: 10px; border-bottom: 1px solid #ccc">
            在线用户
            <span style="font-size: 12px">（点击聊天气泡开始聊天）</span>
          </div>
          <div style="padding: 10px 0" v-for="user in users" :key="user">
            <el-button
              type="primary"
              class="btn"
              size="large"
              @click="changeChatUser(user)"
            >
              {{ user.username }}
            </el-button>
            <!-- <i
              class="el-icon-chat-dot-round"
              style="margin-left: 10px; font-size: 16px; cursor: pointer"
              @click="changeChatUser(user)"
            ></i> -->
            <span
              style="font-size: 12px; color: limegreen; margin-left: 5px"
              v-if="user === chatUser"
            >
              chatting...
            </span>
          </div>
        </el-card>
      </el-col>
      <el-col :span="16">
        <div
          style="
            width: 800px;
            margin: 0 auto;
            background-color: white;
            border-radius: 5px;
            box-shadow: 0 0 10px #ccc;
          "
        >
          <div style="text-align: center; line-height: 50px">
            Web聊天室（{{ chatUser }}）
          </div>
          <div
            style="height: 350px; overflow: auto; border-top: 1px solid #ccc"
            v-html="content"
          ></div>
          <div style="height: 200px">
            <textarea
              v-model="text"
              style="
                height: 160px;
                width: 100%;
                padding: 20px;
                border: none;
                border-top: 1px solid #ccc;
                border-bottom: 1px solid #ccc;
                outline: none;
              "
            ></textarea>
            <div style="text-align: right; padding-right: 10px">
              <el-button type="primary" size="mini" @click="send">
                发送
              </el-button>
            </div>
          </div>
        </div>
      </el-col>
    </el-row>
  </div>
</template>
<script setup>
import request from '@/utils/request'
import { async } from '@kangc/v-md-editor'
import { ref, getCurrentInstance, onMounted } from 'vue'

const { proxy: ctx } = getCurrentInstance() // 可以把ctx当成vue2中的this
const circleUrl = ref(
  'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'
)
const user = ref({})
const isCollapse = ref(false)
const users = ref([])
const chatUser = ref('')
const text = ref('')
const messages = ref([])
const content = ref('')
onMounted(() => {
  init()
})
let socket

const changeChatUser = async user => {
  chatUser.value = user.username
}
const init = async () => {
  //获取到当前登录的用户账号
  user.value = localStorage.getItem('username')
  let username = user.value
  let _this = ctx
  if (typeof WebSocket == 'undefined') {
    console.log('您的浏览器不支持WebSocket')
  } else {
    console.log('您的浏览器支持WebSocket')
    let socketUrl = 'ws://localhost:88/api/chat/' + username
    if (socket != null) {
      socket.close()
      socket = null
    }
    // 开启一个websocket服务
    socket = new WebSocket(socketUrl)
    console.log(socket)
    //打开事件
    socket.onopen = function() {
      console.log('websocket已打开')
    }
    //  浏览器端收消息，获得从服务端发送过来的文本消息
    socket.onmessage = function(msg) {
      console.log('收到数据====' + msg.data)
      let data = JSON.parse(msg.data) // 对收到的json数据进行解析， 类似这样的： {"users": [{"username": "zhang"},{ "username": "admin"}]}
      if (data.users) {
        // 获取在线人员信息
        _this.users = data.users.filter(user => user.username !== username) // 获取当前连接的所有用户信息，并且排除自身，自己不会出现在自己的聊天列表里
      } else {
        // 如果服务器端发送过来的json数据 不包含 users 这个key，那么发送过来的就是聊天文本json数据
        //  // {"from": "zhang", "text": "hello"}
        if (data.from === _this.chatUser) {
          _this.messages.push(data)
          // 构建消息内容
          _this.createContent(data.from, null, data.text)
        }
      }
    }
    //关闭事件
    socket.onclose = function() {
      console.log('websocket已关闭')
    }
    //发生了错误事件
    socket.onerror = function() {
      console.log('websocket发生了错误')
    }
  }
}

const send = async () => {
  if (!chatUser.value) {
    ctx.$message({ type: 'warning', message: '请选择聊天对象' })
    return
  }
  if (!text.value) {
    ctx.$message({ type: 'warning', message: '请输入内容' })
  } else {
    if (typeof WebSocket == 'undefined') {
      console.log('您的浏览器不支持WebSocket')
    } else {
      console.log('您的浏览器支持WebSocket')
      // 组装待发送的消息 json
      // {"from": "zhang", "to": "admin", "text": "聊天文本"}
      let message = {
        from: user.value,
        to: chatUser.value,
        text: text.value,
      }
      socket.send(JSON.stringify(message)) // 将组装好的json发送给服务端，由服务端进行转发
      messages.value.push({ user: user.value, text: text.value })
      // 构建消息内容，本人消息
      createContent(null, user.value, text.value)
      text.value = ''
    }
  }
}

const createContent = async (remoteUser, nowUser, text) => {
  // 这个方法是用来将 json的聊天消息数据转换成 html的。
  let html
  // 当前用户消息
  if (nowUser) {
    // nowUser 表示是否显示当前用户发送的聊天消息，绿色气泡
    html =
      '<div class="el-row" style="padding: 5px 0">\n' +
      '  <div class="el-col el-col-22" style="text-align: right; padding-right: 10px">\n' +
      '    <div class="tip left">' +
      text +
      '</div>\n' +
      '  </div>\n' +
      '  <div class="el-col el-col-2">\n' +
      '  <span class="el-avatar el-avatar--circle" style="height: 40px; width: 40px; line-height: 40px;">\n' +
      '    <img src="https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png" style="object-fit: cover;">\n' +
      '  </span>\n' +
      '  </div>\n' +
      '</div>'
  } else if (remoteUser) {
    // remoteUser表示远程用户聊天消息，蓝色的气泡
    html =
      '<div class="el-row" style="padding: 5px 0">\n' +
      '  <div class="el-col el-col-2" style="text-align: right">\n' +
      '  <span class="el-avatar el-avatar--circle" style="height: 40px; width: 40px; line-height: 40px;">\n' +
      '    <img src="https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png" style="object-fit: cover;">\n' +
      '  </span>\n' +
      '  </div>\n' +
      '  <div class="el-col el-col-22" style="text-align: left; padding-left: 10px">\n' +
      '    <div class="tip right">' +
      text +
      '</div>\n' +
      '  </div>\n' +
      '</div>'
  }
  console.log(html)
  content.value += html
}
</script>
<style>
.tip {
  color: white;
  text-align: center;
  border-radius: 10px;
  font-family: sans-serif;
  padding: 10px;
  width: auto;
  display: inline-block !important;
  display: inline;
}
.right {
  background-color: deepskyblue;
}
.left {
  background-color: forestgreen;
}
</style>
