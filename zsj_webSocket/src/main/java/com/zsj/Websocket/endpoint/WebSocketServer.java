package com.zsj.Websocket.endpoint;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.zsj.Websocket.entity.RelationMessageEntity;
import com.zsj.Websocket.service.RelationMessageService;
import com.zsj.Websocket.util.BeanHelper;
import com.zsj.Websocket.vo.UserVo;
import com.zsj.common.utils.GsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author websocket服务
 * <p>
 * TODO
 * 记录聊天的过程
 * 必须是好友才可以聊天
 */
@ServerEndpoint(value = "/chat/{username}")
@Component
public class WebSocketServer {

    @Autowired
    @Lazy
    private RedisTemplate<String, String> redisTemplate;

    private static RedisTemplate<String, String> redis;

    @PostConstruct
    public void getRedisCli() {
        redis = this.redisTemplate;
    }


    private static final Logger log = LoggerFactory.getLogger(WebSocketServer.class);
    /**
     * 记录当前在线连接数
     * String 是当前在线的用户名称
     * Session会话对象 用来发消息
     */
    public static final Map<String, Session> sessionMap = new ConcurrentHashMap<>();

    /**
     * 记录未在线用户的消息 等他上线的时候推送给他
     */
    public static final Map<String, List<String>> notOnlineMessage = new ConcurrentHashMap<>();


    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username) {
        ValueOperations<String, String> ops = redis.opsForValue();
        sessionMap.put(username, session);
        log.info("有新用户加入，username={}, 当前在线人数为：{}", username, sessionMap.size());
        //得到这个username之后去缓存中查询列表中的用户信息 得到他的头像信息
        String jsonFromRedis = ops.get("userList");
        List<UserVo> collect = Arrays.stream(GsonUtil.gson.fromJson(jsonFromRedis, UserVo[].class)).collect(Collectors.toList());
        JSONObject result = new JSONObject();
        JSONArray array = new JSONArray();
        JSONObject global = new JSONObject();
        global.set("globalMessage", username + "上线了");
        for (Object key : sessionMap.keySet()) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.set("username", key);
            for (UserVo vo : collect) {
                if (vo.getUsername().equals(key)) {
                    jsonObject.set("avatar", vo.getAvatar());
                }
            }
            jsonObject.set("notreadC", 0);
            array.add(jsonObject);
        }
        result.set("users", array);
        sendAllMessage(JSONUtil.toJsonStr(result));  // 后台发送消息给所有的客户端
        sendAllMessage(GsonUtil.gson.toJson(global)); //后台通知消息
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(Session session, @PathParam("username") String username) {
        sessionMap.remove(username);
        JSONObject global = new JSONObject();
        global.set("globalMessage", username + "下线了");
        sendAllMessage(GsonUtil.gson.toJson(global)); //后台通知消息
        log.info("有一连接关闭，移除username={}的用户session, 当前在线人数为：{}", username, sessionMap.size());
    }

    /**
     * 收到客户端消息后调用的方法
     * 后台收到客户端发送过来的消息
     * onMessage 是一个消息的中转站
     * 接受 浏览器端 socket.send 发送过来的 json数据
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session, @PathParam("username") String username) {
        log.info("服务端收到用户username={}的消息:{}", username, message);
        JSONObject obj = JSONUtil.parseObj(message);
        String toUsername = obj.getStr("toUser"); // 表示发送给哪个用户
        String text = obj.getStr("message"); // 发送的消息文本
        Session toSession = sessionMap.get(toUsername); // 根据 to用户名来获取 session，再通过session发送消息文本
        if (toSession != null) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.set("fromUser", username);
            jsonObject.set("text", text);
            this.sendMessage(jsonObject.toString(), toSession);
            new Thread(() ->
                    BeanHelper.getBean(RelationMessageService.class)
                            .save(new RelationMessageEntity(username, toUsername, text, "yes",
                                    new Date(System.currentTimeMillis())))).start();

            log.info("发送给用户username={}，消息：{}", toUsername, jsonObject.toString());

        } else {
            log.info("发送失败，未找到用户username={}的session", toUsername);
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        log.error("发生错误");
        error.printStackTrace();
    }

    /**
     * 服务端发送消息给客户端
     */
    private void sendMessage(String message, Session toSession) {
        try {
            log.info("服务端给客户端[{}]发送消息{}", toSession.getId(), message);
            toSession.getBasicRemote().sendText(message);
        } catch (Exception e) {
            log.error("服务端发送消息给客户端失败", e);
        }
    }

    /**
     * 服务端发送消息给所有客户端
     */
    private void sendAllMessage(String message) {
        try {
            for (Session session : sessionMap.values()) {
                log.info("服务端给客户端[{}]发送消息{}", session.getId(), message);
                session.getBasicRemote().sendText(message);
            }
        } catch (Exception e) {
            log.error("服务端发送消息给客户端失败", e);
        }
    }
}
