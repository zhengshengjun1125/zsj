package com.zsj.system.controller;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zsj.system.entity.UserEntity;
import com.zsj.system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.*;

import com.zsj.system.entity.UserTokenEntity;
import com.zsj.system.service.UserTokenService;
import com.zsj.common.utils.PageUtils;
import com.zsj.common.utils.R;


/**
 * 系统用户Token
 *
 * @author zsj
 * @email zsjemail666@163.com
 * @date 2023-10-05 20:07:09
 */
@RestController
@RequestMapping("system/token")
public class UserTokenController {
    @Autowired
    private UserTokenService userTokenService;

    @Autowired
    @Lazy
    private UserService userService;


    @Autowired
    @Lazy
    private RedisTemplate<String, String> redisTemplate;


    @PostMapping("/check")
    public R checkTokenIsLegal(@RequestBody UserTokenEntity tokenEntity) {
        if (Objects.isNull(tokenEntity)) return R.error();
        UserTokenEntity one = userTokenService.getOne(new QueryWrapper<UserTokenEntity>().eq("user_id", tokenEntity.getUserId()));
        if (null !=one){
            long time = one.getExpireTime().getTime();
            long l = System.currentTimeMillis();
            if (l >= time) return R.ok("token已经过期");
            else return R.ok("token暂时正常");
        }
        return R.error("出现异常？尊都假都？");
    }



    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = userTokenService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{userId}")
    public R info(@PathVariable("userId") Long userId) {
        UserTokenEntity userToken = userTokenService.getById(userId);

        return R.ok().put("userToken", userToken);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody UserTokenEntity userToken) {
        userTokenService.save(userToken);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody UserTokenEntity userToken) {
        userTokenService.updateById(userToken);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] userIds) {
        userTokenService.removeByIds(Arrays.asList(userIds));

        return R.ok();
    }

}
