package com.zsj.system.controller.v1;


import java.util.List;
import java.util.concurrent.TimeUnit;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.gson.reflect.TypeToken;
import com.zsj.common.utils.*;
import com.zsj.system.entity.UserEntity;
import com.zsj.system.service.UserService;
import com.zsj.system.vo.Friend;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.*;
import com.zsj.system.entity.UserFEntity;
import com.zsj.system.service.UserFService;


/**
 * @author zsj
 * @email zsjemail666@163.com
 * @date 2023-11-08 20:18:47
 */
@RestController
@RequestMapping("system/homie")
public class UserFController {

    @Autowired
    StringRedisTemplate redisTemplate;


    @Autowired
    private UserFService userFService;

    @Autowired
    @Lazy
    private UserService userService;

    //此接口暂时无法接收到header中的name 因为暂时提供给websocket模块使用
    @PostMapping("/list/{username}")
    public R getCurLoginUserFriendsList(@PathVariable("username") String name) {
        //根据当前使用对象 获取到他的好友列表 并且加入到缓存中
        ValueOperations<String, String> ops =
                redisTemplate.opsForValue();
        String json = ops.get(name + "friendsList");
        if (json != null) {
            return R.ok().put("data", GsonUtil.gson.fromJson(json,
                    new TypeToken<List<Friend>>() {
                    }.getType()));
        } else {
            //根据当前用户id去拿到好友表中属于他好友的所有人的id 然后在查询这些人的信息封装到一个集合中
            UserEntity one = userService.getOne(new QueryWrapper<UserEntity>().eq("username", name));
            if (ObjectUtil.objectIsNotNull(one)) {
                Long cur_user_id = one.getId();
                QueryWrapper<UserFEntity> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("user_id", cur_user_id);
                queryWrapper.eq("user_f_status", 1);
                List<UserFEntity> list = userFService.list(queryWrapper);
                List<Friend> data = userFService.getCurUserFVo(list);
                if (data.size() != 0) {
                    //缓存
                    String key = name + "friendsList";
                    ops.set(key, GsonUtil.gson.toJson(data),1, TimeUnit.HOURS);
                }
                return R.ok().put("data", data);
            }
            return R.error(SystemMessage.WARING_DANGER_OPERATION_CN);
        }

    }


}
