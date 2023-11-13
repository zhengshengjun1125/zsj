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
          </div>
          <el-table :data="users" style="width: 100%">
            <el-table-column prop="username" label="用户名" width="80" />
            <el-table-column label="头像" width="80">
              <template #default="scope">
                <el-avatar :size="50" :src="scope.row.avatar" />
              </template>
            </el-table-column>
            <el-table-column prop="notreadC" label="未读" width="60" />
            <el-table-column label="操作" width="100" #default="scope">
              <el-button type="success" @click="toChat(scope.row)">
                聊天
              </el-button>
              <!-- <el-button type="danger" @click="rmF(scope.row)">删除</el-button> -->
            </el-table-column>
          </el-table>
        </el-card>
        <!-- 做系统消息 -->
        <el-card
          style="width: 100%; min-height: 300px; color: #333; margin-top: 10px"
        >
          <h3>系统消息</h3>
          <el-table :data="globalMessages" style="width: 100%">
            <el-table-column prop="message" />
          </el-table>
        </el-card>
      </el-col>
      <el-col :span="16">
        <el-card
          style="
            height: 680px;
            width: 800px;
            margin: 0 auto;
            background-color: white;
            border-radius: 5px;
            box-shadow: 0 0 10px #ccc;
          "
        >
          <div style="text-align: center; line-height: 50px">
            <h3>{{ chatUser }}</h3>
          </div>
          <div
            style="height: 350px; overflow: auto; border-top: 1px solid #ccc"
            v-html="content"
          ></div>
          <div style="height: 200px">
            <el-popover placement="top" :width="600" trigger="click">
              <template #reference>
                <img
                  src="@/assets/images/emoji.svg"
                  style="margin-left: 10px"
                />
                <!-- <el-button style="margin-right: 16px">添加表情</el-button> -->
              </template>
              <ul class="ulclass">
                <li
                  style="list-style: none; text-align: center"
                  v-for="(item, index) in emoji"
                  :key="index"
                >
                  <span style="" @click="appendEmoji(index)">{{ index }}</span>
                </li>
              </ul>
            </el-popover>

            <div style="text-align: right; padding-right: 10px">
              <el-button type="success" size="mini" @click="send">
                发送
              </el-button>
            </div>

            <el-input
              @keyup.enter="send"
              style="font-size: 18px"
              v-model="text"
              :rows="3"
              placeholder="开始聊天吧 输入之后回车发送~"
              :autosize="{ minRows: 6, maxRows: 6 }"
              type="textarea"
            />
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>
<script setup>
import emoji from '@/utils/emoji'
import request from '@/utils/request'
import { async } from '@kangc/v-md-editor'
import { ElMessageBox } from 'element-plus'
import { ref, getCurrentInstance, onMounted } from 'vue'
// const inputTextArea = document.getElementById('inputTextArea')
const { proxy: ctx } = getCurrentInstance()
const user = ref({})
const isCollapse = ref(false)
const users = ref([])
const chatUser = ref('')
const text = ref('')
const messages = ref([])
const content = ref('')
const name_avatar = new Map()
const unReadMessageList = ref([])
const globalMessages = ref([])
onMounted(() => {
  init()
})
let socket

function showList() {
  document.getElementById('emojiList').style.display = 'block'
}

function hideList() {
  document.getElementById('emojiList').style.display = 'none'
}

const changeChatUser = async user => {
  chatUser.value = user.username
}
const init = async () => {
  //获取到当前登录的用户账号
  user.value = localStorage.getItem('username')
  let username = user.value
  let _this = ctx
  if (typeof WebSocket == 'undefined') {
    alert('您的浏览器不支持WebSocket')
  } else {
    // console.log('您的浏览器支持WebSocket')
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
      // console.log('websocket已打开')
    }
    //  浏览器端收消息，获得从服务端发送过来的文本消息
    socket.onmessage = function(msg) {
      // console.log('收到msg数据====' + msg.data)
      let data = JSON.parse(msg.data) // 对收到的json数据进行解析， 类似这样的： {"users": [{"username": "zhang"},{ "username": "admin"}]}
      if (data.users || data.globalMessage) {
        const users = data.users
        // console.log(data.globalMessage)
        globalMessages.value.push({ message: data.globalMessage })
        users.forEach(element => {
          name_avatar.set(element.username, element.avatar)
        })
        // console.log(name_avatar)
        // 获取在线人员信息
        _this.users = data.users.filter(user => user.username !== username) // 获取当前连接的所有用户信息，并且排除自身，自己不会出现在自己的聊天列表里
      } else {
        console.log(data)
        // 如果服务器端发送过来的json数据 不包含 users 这个key，那么发送过来的就是聊天文本json数据
        //  // {"from": "zhang", "text": "hello"}
        if (data.fromUser === _this.chatUser) {
          _this.messages.push(data)
          // 构建接收消息内容 todo  这里应该不着急直接生成html内容
          _this.createContent(data.fromUser, null, data.text)
        } else {
          ElMessageBox.confirm(
            '用户' + data.fromUser + '发消息给你了,是否查看?',
            '提示',
            {
              type: 'info',
              cancelButtonText: '取消',
              confirmButtonText: '确认',
              beforeClose: (action, instance, done) => {
                if (action === 'confirm') {
                  instance.onclick = (function() {
                    let type = window.event.type
                    if (type !== 'keydown') {
                      done()
                    }
                  })()
                } else {
                  done()
                }
              },
            }
          )
            .then(() => {
              _this.chatUser = data.fromUser
              _this.messages.push(data)
              _this.createContent(data.fromUser, null, data.text)
            })
            .catch(() => {
              //将未读消息放入到列表中
              //并且将users的notreadC ++
              users.value.forEach(e => {
                if (e.username === data.fromUser) {
                  e.notreadC++
                }
              })
              unReadMessageList.value.push({
                fromUser: data.fromUser,
                text: data.text,
                toUser: username,
              })
            })
        }
      }
    }
    //关闭事件
    socket.onclose = function() {
      // console.log('websocket已关闭')
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
      // console.log('您的浏览器支持WebSocket')
      // 组装待发送的消息 json
      // {"from": "zhang", "to": "admin", "text": "聊天文本"}
      let message = {
        fromUser: user.value,
        toUser: chatUser.value,
        message: text.value,
      }
      socket.send(JSON.stringify(message)) // 将组装好的json发送给服务端，由服务端进行转发
      messages.value.push({ user: user.value, text: text.value })
      // 构建消息内容，本人消息
      createContent(null, user.value, text.value)
      text.value = ''
    }
  }
}

const appendEmoji = e => {
  text.value += e
}
const createContent = async (remoteUser, nowUser, text) => {
  // 这个方法是用来将 json的聊天消息数据转换成 html的。
  let html
  // 当前用户消息
  if (nowUser) {
    // console.log(nowUser)
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
      '    <img src="' +
      name_avatar.get(nowUser) +
      '" style="object-fit: cover;">\n' +
      '  </span>\n' +
      '  </div>\n' +
      '</div>'
  } else if (remoteUser) {
    // console.log(remoteUser)
    // remoteUser表示远程用户聊天消息，蓝色的气泡
    html =
      '<div class="el-row" style="padding: 5px 0;font: bold;">\n' +
      '  <div class="el-col el-col-2" style="text-align: right">\n' +
      '  <span class="el-avatar el-avatar--circle" style="height: 40px; width: 40px; line-height: 40px;">\n' +
      '    <img src="' +
      name_avatar.get(remoteUser) +
      '" style="object-fit: cover;">\n' +
      '  </span>\n' +
      '  </div>\n' +
      '  <div class="el-col el-col-22" style="text-align: left; padding-left: 10px">\n' +
      '    <div class="tip right">' +
      text +
      '</div>\n' +
      '  </div>\n' +
      '</div>'
  }
  // console.log(html)
  content.value += html
}

const toChat = async row => {
  // console.log(row.username)
  chatUser.value = row.username
  //这里进行数据的渲染
  const list = unReadMessageList.value
  users.value.forEach(e => {
    if (e.username === row.username) {
      e.notreadC = 0
    }
  })
  for (let i = 0; i < list.length; i++) {
    if (row.username === list[i].fromUser) {
      //说明这个就是你未读的消息 将其渲染上去
      createContent(list[i].fromUser, null, list[i].text)
    }
  }
}

const rmF = () => {}
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
.ulclass {
  width: 500px;
  list-style: none;
}
.ulclass li {
  width: 30px;
  float: left;
  margin-right: 15px;
  line-height: 20px;
}
.ulclass li:hover {
  background-color: forestgreen;
}
</style>
