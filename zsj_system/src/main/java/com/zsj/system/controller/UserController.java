package com.zsj.system.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zsj.common.utils.Encrypt;
import com.zsj.common.utils.JwtUtil;
import com.zsj.system.entity.UserTokenEntity;
import com.zsj.system.service.UserTokenService;
import com.zsj.system.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.*;

import com.zsj.system.entity.UserEntity;
import com.zsj.system.service.UserService;
import com.zsj.common.utils.PageUtils;
import com.zsj.common.utils.R;


/**
 * @author zsj
 * @email zsjemail666@163.com
 * @date 2023-10-05 20:07:09
 */
@RestController
@RequestMapping("system/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    @Lazy
    private UserTokenService userTokenService;


    @Autowired
    RedisTemplate<String, String> redisTemplate;


    @PostMapping("/login")
    public R login(@RequestBody UserEntity user) {
        /*
            需要先判断是否登录成功
            1、密码加密后与数据库是否相同
            2、用户是否有效
            到这里说明各种信息正常
            1、使用用户id来获取token并且返回
            2、将token保存到redis中设置1天的过期时间
            3、将token保存到数据库中
            4、返回登录信息
         */
        String username = user.getUsername();
        QueryWrapper<UserEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        UserEntity entity = userService.getOne(queryWrapper);
        if (null != entity) {
            //说明查到了
            if (Encrypt.encrypt_md5(user.getPassword()).equals(entity.getPassword())) {
                //说明密码正确
                String token = JwtUtil.getJwtToken(username);
                ValueOperations<String, String> ops = redisTemplate.opsForValue();
                ops.setIfAbsent(username, token, 1, TimeUnit.DAYS);//用户账号对应的token  并且设置1天的过期时间

                UserTokenEntity tokenEntity = new UserTokenEntity();
                tokenEntity.setUserId(entity.getId());
                tokenEntity.setToken(token);
                tokenEntity.setExpireTime(new Date(System.currentTimeMillis()+86400000));
                tokenEntity.setUpdateTime(new Date(System.currentTimeMillis()));
                userTokenService.saveOrUpdateToken(tokenEntity);

                return R.ok("恭喜你登录成功！").put("token", token);
            } else return R.ok("不对哦~ 对哦~ 哦~");
        } else {
            return R.error("没有此用户你个小笨蛋");
        }
    }


    /**
     * 获取用户信息
     */
    @PostMapping("/info")
    public R getUserInfo(@RequestBody UserEntity entity) {
        String username = entity.getUsername();
        QueryWrapper<UserEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        UserEntity user = userService.getOne(queryWrapper);
        //不为空且状态为正常
        if (null != user && !user.getStatus().equals(0)) {
            return R.ok().put("info", new UserVo(user));
        }
        return R.ok("此用户信息错误或者不存在~");

    }


    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = userService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
        UserEntity user = userService.getById(id);

        return R.ok().put("user", user);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody UserEntity user) {
        userService.save(user);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody UserEntity user) {
        userService.updateById(user);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        userService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
