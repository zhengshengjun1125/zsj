package com.zsj.system.controller;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.zsj.common.utils.*;
import com.zsj.system.entity.RoleEntity;
import com.zsj.system.entity.UserTokenEntity;
import com.zsj.system.service.RoleService;
import com.zsj.system.service.UserTokenService;
import com.zsj.system.vo.LoginBody;
import com.zsj.system.vo.PassFormByUser;
import com.zsj.system.vo.Token;
import com.zsj.system.vo.UserVo;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

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
        List<UserVo> userList = getUserList();
        ops.set("userList", GsonUtil.gson.toJson(userList));//缓存用户集合
    }

    /**
     * 初始化角色信息列表
     * 每十分钟去更新一下
     */
    @Scheduled(fixedRate = 600000)
    public void initRoleList()  {
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
        //验证码过期了
        if (ops.get(key) != null) {
            //回答错了
            if (code.equals(ops.get(key))) {
                redisTemplate.delete(key);//删除对应key
                String username = user.getUsername();
                QueryWrapper<UserEntity> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("username", username);
                UserEntity entity = userService.getOne(queryWrapper);
                if (null != entity) {
                    if (entity.getStatus().equals(1)) {
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
                    } else return R.error("此账号已经被一个人派杀手杀死了");
                } else {
                    return R.error("没有此用户你个小笨蛋");
                }
            } else {
                return R.error("验证码错误").put("data", new Token());
            }
        }
        return R.error("验证码已经过期,请刷新").put("data", new Token());
    }


    /**
     * 全局注销
     */
    @GetMapping("/logout")
    public R logout(@RequestHeader("system_api_Authorize") String token,
                    @RequestHeader("system_api_Authorize_name") String name) {
        //删除所有值为name的key todo
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
        } else {
            //说明缓存没拿到
            UserTokenEntity one = userTokenService.getOne(new QueryWrapper<UserTokenEntity>().eq("token", token));
            Long userId = one.getUserId();
            UserEntity no_do_info = userService.getOne(new QueryWrapper<UserEntity>().eq("id", userId));
            RoleEntity role = roleService.getOne(new QueryWrapper<RoleEntity>().eq("id", no_do_info.getRoleId()));
            return R.ok().put("data", new UserVo(no_do_info, role.getRoleName()));
        }
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
                    GsonUtil.gson.fromJson(userList, UserVo[].class));
        } else {
            //说明缓存没查到 从db里去查
            List<UserVo> collect = getUserList();
            ops.set("userList", GsonUtil.gson.toJson(collect));
            return R.ok("获取成功").put("data", collect);
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
        return R.ok().put("data", userService.getAllUserByCondition(cur, size, user));
    }


    /**
     * 注册用户
     */
    @PostMapping("/register")
    public R register(@RequestHeader("system_api_Authorize_name") String register
            , @RequestBody UserEntity user) {
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
        //开始校验邮箱  手机号 是否合法
        if (!user.getMobile().matches(MatcherFormat.mobile_matcher)) return R.error("手机格式错误");
        if (!user.getEmail().matches(MatcherFormat.email_matcher)) return R.error("邮箱格式错误");
        QueryWrapper<UserEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("username", register);
        UserEntity one = userService.getOne(wrapper);//建造者
        Long register_role_id = one.getRoleId();
        if (user.getRoleId() == 1 && !register.equals("zsj")) {
            //如果注册的是超级管理员
            return R.error("越权操作");
        }
        RoleEntity register_role = roleService.getOne(new QueryWrapper<RoleEntity>().eq("id", register_role_id));
        Long register_roleLevel = register_role.getLevel();
        RoleEntity user_role = roleService.getOne(new QueryWrapper<RoleEntity>().eq("id", user.getRoleId()));
        Long user_roleLevel = user_role.getLevel();
        if (register_roleLevel >= user_roleLevel) {
            return R.error("越权操作");
        }
        user.setPassword(Encrypt.encrypt_md5(user.getPassword()));
        user.setCreateTime(new Date(System.currentTimeMillis()));
        user.setStatus(1);
        user.setCreatUserId(one.getId());
        if (userService.save(user)) {
            //添加成功
            initUserList();//重新初始化我们的用户列表缓存
            //注册成功后利用消息队列发送邮件给予注册成功的用户的邮箱账号 todo
//            String email = user.getEmail();
            return R.ok("注册成功");
        }
        return R.error("注册失败");
    }


    /**
     * 干掉某一个用户
     */
    @PostMapping("/cancellation")
    public R removeUserById(@RequestHeader("system_api_Authorize_name") String name,
                            @RequestBody UserEntity user) {
        //因为删除系统用户 只有能从表中点击 基本不太可能空指针
        if (Objects.isNull(user)) return R.error("要杀人你至少给个目标啊?");
        Long id = user.getId();
        QueryWrapper<UserEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("id", id);
        QueryWrapper<UserEntity> check = new QueryWrapper<>();
        check.eq("username", name);
        UserEntity one = userService.getOne(wrapper);//操作对象
        UserEntity two = userService.getOne(check);//当前对象
        //第一不可以删除zsj
        if (one.getUsername().equals("zsj")) return R.error("你不可以杀掉作者呜呜呜/(ㄒoㄒ)/~~");
        //自己不能干掉自己
        if (one.getUsername().equals(name)) return R.error("你不可以杀掉你自己 就像鲁迅不可以杀掉周树人");
        if (!name.equals("zsj")) {
            RoleEntity roleServiceOne = roleService.getOne(new QueryWrapper<RoleEntity>().eq("id", one.getRoleId()));
            RoleEntity roleServiceTwo = roleService.getOne(new QueryWrapper<RoleEntity>().eq("id", two.getRoleId()));
            //需要校验当前用户的角色等级是否高于它要删除的对象
            if (roleServiceTwo.getLevel() >= roleServiceOne.getLevel())
                return R.error("你不可以杀掉级别比您高或者一样的人");
        }
        //到这里说明杀掉就完事了
        UpdateWrapper<UserEntity> killer = new UpdateWrapper<>();
        killer.eq("id", id);
        killer.set("status", 0);
        boolean kill_is_success = userService.update(killer);
        initUserList();
        return kill_is_success ? R.ok("你干掉他/她了") : R.error("任务失败了");
    }


    /**
     * 修改用户的密码
     * 需要保证用户不会直接请求到用户服务这里 我们还是需要进行鉴权
     */
    @PostMapping("/resetPass")
    public R resetCurUserPass(@RequestHeader("system_api_Authorize_name") String name,
                              @RequestHeader("system_api_Authorize") String token,
                              @RequestBody PassFormByUser user) {
        //直接根据请求头中的用户名称来进行修改 因为 我们网关的写法 不会导致出现token和name不对应的情况
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        String sign = ops.get(name);
        if (!Objects.isNull(sign) && !sign.isEmpty()) {
            //如果redis中存在
            //校验token的合法性
            if (sign.equals(token)) {
                //说明token校验通过
                //还需要校验旧密码
                String oldPassword = user.getOldPassword();
                QueryWrapper<UserEntity> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("username", name);
                UserEntity one = userService.getOne(queryWrapper);
                if (Encrypt.encrypt_md5(oldPassword).equals(one.getPassword())) {
                    //旧密码正确
                    UpdateWrapper<UserEntity> wrapper = new UpdateWrapper<>();
                    wrapper.eq("username", name);//对指定账号进行修改 因为账号是唯一的
                    wrapper.set("password", Encrypt.encrypt_md5(user.getNewPassword()));
                    initUserList();
                    return userService.update(wrapper) ? R.ok("修改成功") : R.error("修改失败");
                } else {
                    return R.error("旧密码错误");
                }

            } else {
                return R.error(401, "请重新登录");
            }
        }
        return R.error("非法操作");
    }

    /**
     * 修改用户信息
     */
    @PostMapping("/upgradeUserInfo")
    public R upgradeUserInfo(@RequestHeader("system_api_Authorize_name") String name, @RequestBody UserVo vo) {
        String message = userService.updateUserInfoById(vo, name);
        //根据用户id修改用户信息
        if (message.equals("修改成功")) initUserList();
        return R.ok(message);
    }


    List<UserVo> getUserList(){
        List<UserEntity> list = userService.list(new QueryWrapper<UserEntity>().eq("status", 1));
        return list.stream().map(user ->
        {
            RoleEntity role = roleService.getOne(new QueryWrapper<RoleEntity>().eq("id", user.getRoleId()));
            return new UserVo(user, role.getRoleName());
        }).collect(Collectors.toList());
    }
    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = userService.queryPage(params);

        return R.ok().put("page", page);
    }

}
