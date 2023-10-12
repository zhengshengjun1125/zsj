package com.zsj.system.controller;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zsj.common.utils.*;
import com.zsj.system.entity.RoleEntity;
import com.zsj.system.entity.UserTokenEntity;
import com.zsj.system.service.RoleService;
import com.zsj.system.service.UserTokenService;
import com.zsj.system.vo.LoginBody;
import com.zsj.system.vo.Token;
import com.zsj.system.vo.UserVo;
import io.netty.util.internal.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.lang.Nullable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import com.zsj.system.entity.UserEntity;
import com.zsj.system.service.UserService;


/**
 * @author zsj
 * @email zsjemail666@163.com
 * @date 2023-10-05 20:07:09
 */
@RestController
@RequestMapping("system/user")
@Slf4j
public class UserController {
    public static Map<Long, String> roleMap = new HashMap<>();

    @Autowired
    private UserService userService;

    @Autowired
    @Lazy
    private UserTokenService userTokenService;


    @Autowired
    @Lazy
    private RoleService roleService;

    @Autowired
    RedisTemplate<String, String> redisTemplate;


    void initUserList() {
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        //将所有信息缓存到redis中
        List<UserEntity> list = userService.list(new QueryWrapper<UserEntity>().eq("status", 1));
        List<UserVo> curList = list.stream().map(user ->
        {
            RoleEntity role = roleService.getOne(new QueryWrapper<RoleEntity>().eq("id", user.getRoleId()));
            return new UserVo(user, role.getRoleName());
        }).collect(Collectors.toList());

        ops.set("userList", GsonUtil.gson.toJson(curList));//缓存用户集合
    }

    /**
     * 初始化角色信息列表
     * 每十分钟去更新一下
     */
    @Scheduled(fixedRate = 600000)
    public void initRoleList() throws InterruptedException {
        List<RoleEntity> list = roleService.list(new QueryWrapper<RoleEntity>().eq("status", 1));
        list.stream().map(roleEntity -> {
            //将对应的数据保存到我们的全局角色变量中
            return roleMap.put(roleEntity.getId(), roleEntity.getRoleName());
        });
        initUserList();
    }

    @PostMapping("/login")
    public R login(@RequestBody LoginBody user) {
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        //验证码的校验
        String key = user.getKey();
        String code = user.getCode();
        if (ops.get(key) != null) {
            if (code.equals(ops.get(key))) {
                redisTemplate.delete(key);//删除对应key
                String username = user.getUsername();
                QueryWrapper<UserEntity> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("username", username);
                UserEntity entity = userService.getOne(queryWrapper);
                if (null != entity) {
                    //说明查到了
                    if (Encrypt.encrypt_md5(user.getPassword()).equals(entity.getPassword())) {
                        //说明密码正确
                        String token = JwtUtil.getJwtToken(username);
                        ops.set(username, token, 1, TimeUnit.DAYS);
                        //用户账号对应的token  并且设置1天的过期时间
                        UserTokenEntity tokenEntity = new UserTokenEntity();
                        tokenEntity.setUserId(entity.getId());
                        tokenEntity.setToken(token);
                        tokenEntity.setExpireTime(new Date(System.currentTimeMillis() + 86400000));
                        tokenEntity.setUpdateTime(new Date(System.currentTimeMillis()));
                        userTokenService.saveOrUpdateToken(tokenEntity);
                        //缓存用户信息 token为键  设置一天的过期时间
                        ops.set(token, GsonUtil.gson.toJson(entity), 1, TimeUnit.DAYS);
                        return R.ok("恭喜你登录成功！").put("data", new Token(token));
                    } else return R.error("不对哦~ 对哦~ 哦~");
                } else {
                    return R.error("没有此用户你个小笨蛋");
                }
            }
        }
        return R.error("验证码错误").put("data", new Token());
    }


    /**
     * 全局注销
     */
    @GetMapping("/logout")
    public R logout(@RequestHeader("system_api_Authorize") String token,
                    @RequestHeader("system_api_Authorize_name") String name) {
        return R.ok(Boolean.TRUE.equals(redisTemplate.delete(token))
                && Boolean.TRUE.equals(redisTemplate.delete(name)) ? "退出登录成功" : "退出登录失败");
    }


    /**
     * 获取单体用户信息
     */
    @GetMapping("/info")
    public R getUserInfo(@RequestHeader("system_api_Authorize") String token) {
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        String userJSon = ops.get(token);
        if (!Objects.isNull(userJSon)) {
            UserEntity entity = GsonUtil.gson.fromJson(userJSon, UserEntity.class);
            RoleEntity role = roleService.getOne(new QueryWrapper<RoleEntity>().eq("id", entity.getRoleId()));
            return R.ok().put("data", new UserVo(entity, role.getRoleName()));
        }
        //todo think cache cant find user info
        return R.error("获取失败");
    }


    /**
     * 获取所有用户集合
     */

    @GetMapping("/getAllUser")
    public R GetAllUser() {
        //先从缓存去拿 拿不到再查db
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        String userList = ops.get("userList");
        if (StringUtils.isNotEmpty(userList)) {
            return R.ok("获取成功").put("data",
                    GsonUtil.gson.fromJson(userList,UserVo[].class));
        } else {
            //说明缓存没查到 从db里去查
            List<UserEntity> list = userService.list();
            return R.ok("获取成功").put("data", list.stream().map(user -> {
                RoleEntity role = roleService.getOne(new QueryWrapper<RoleEntity>().eq("id", user.getRoleId()));
                return new UserVo(user, role.getRoleName());
            }).collect(Collectors.toList()));
        }
    }


    /**
     * 所有用户信息 分页
     * 通过 dao层sql 进行判断  参数不为null时加上where条件
     */
    @PostMapping("/getAllUserByPage/{cur}/{size}")
    public R getAllUserByPage(@PathVariable("cur") Integer cur,
                              @PathVariable("size") Integer size,
                               @RequestBody UserEntity user) {
        return R.ok().put("data",userService.getAllUserByCondition(cur, size, user));
    }


    /**
     * 注册用户
     */
    @PostMapping("/register")
    public R register(@RequestBody UserEntity user) {
        String count = user.getUsername();
        UserEntity userEntity = userService.getOne(new QueryWrapper<UserEntity>().eq("username", count));
        if (!Objects.isNull(userEntity)) {
            return R.error("用户账号已经被使用");
        }
        UserEntity entity = userService.getOne(new QueryWrapper<UserEntity>().eq("mobile", user.getMobile()));
        //到这里说明账号没有被使用 但是我们需要检查手机号有没有被使用过
        if (!Objects.isNull(entity)) {
            return R.error("手机号已经被使用了");
        }
        user.setPassword(Encrypt.encrypt_md5(user.getPassword()));
        user.setCreateTime(new Date(System.currentTimeMillis()));
        user.setStatus(1);
        if (userService.save(user)) {
            //添加成功
            initUserList();//重新初始化我们的用户列表缓存
            return R.ok("注册成功");
        }
        return R.error("注册失败");
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
