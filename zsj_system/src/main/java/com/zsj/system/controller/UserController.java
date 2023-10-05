package com.zsj.system.controller;

import java.util.Arrays;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.*;

import com.zsj.system.entity.UserEntity;
import com.zsj.system.service.UserService;
import com.zsj.common.utils.PageUtils;
import com.zsj.common.utils.R;



/**
 * 
 *
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
    RedisTemplate<String,String> redisTemplate;


    /**
     * 列表
     */
    @RequestMapping(value = "/t",method = RequestMethod.GET)
    public R list(){
        return R.ok().put("message", "success");
    }


    @PostMapping("/login")
    public R login(@RequestBody UserEntity user){
        String username = user.getUsername();
        if (userService.getOne(new QueryWrapper<UserEntity>().eq("username",username))!=null){
            ValueOperations<String, String> stringStringValueOperations =
                    redisTemplate.opsForValue();
            stringStringValueOperations.set("token","token");
            return R.ok("登陆成功我操死你的妈");
        }
        return R.error();
    }


    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = userService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
		UserEntity user = userService.getById(id);

        return R.ok().put("user", user);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody UserEntity user){
		userService.save(user);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody UserEntity user){
		userService.updateById(user);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
		userService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
