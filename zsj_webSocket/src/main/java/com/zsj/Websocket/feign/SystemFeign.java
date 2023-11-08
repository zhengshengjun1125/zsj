package com.zsj.Websocket.feign;


import com.zsj.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;


/**
 * @author https://gitee.com/zhengshengjun
 * @date 2023/11/8.
 * 从这里调取系统接口
 */

@FeignClient("zsjSystem")
public interface SystemFeign {

    //调用已经登录的用户的好友集合
    @PostMapping("/system/homie/list/{username}")
    public R getCurLoginUserFriendsList(@PathVariable("username") String username);

}
