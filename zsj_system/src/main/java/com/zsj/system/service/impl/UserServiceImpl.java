package com.zsj.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rabbitmq.client.Channel;
import com.zsj.common.utils.*;
import com.zsj.common.vo.BalanceVoProperties;
import com.zsj.common.vo.EmailVoProperties;
import com.zsj.system.controller.FileController;
import com.zsj.system.entity.RoleEntity;
import com.zsj.system.service.RoleService;
import com.zsj.system.vo.UserVo;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.zsj.system.dao.UserDao;
import com.zsj.system.entity.UserEntity;
import com.zsj.system.service.UserService;
import org.springframework.transaction.annotation.Transactional;


@Service("userService")
@RabbitListener(queues = {GlobalValueToExchange.DEDUCT_QUEUE})
public class UserServiceImpl extends ServiceImpl<UserDao, UserEntity> implements UserService {

    @Autowired
    @Lazy
    private UserDao userDao;


    @Autowired
    @Lazy
    private RoleService roleService;


    @Autowired
    @Lazy
    private RedissonClient redissonClient;

    @Autowired
    @Lazy
    private RabbitTemplate rabbitTemplate;


    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<UserEntity> page = this.page(
                new Query<UserEntity>().getPage(params),
                new QueryWrapper<>()
        );

        return new PageUtils(page);
    }

    @RabbitHandler
    @Transactional
    public void deduct(Message message, BalanceVoProperties properties, Channel channel) throws IOException {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        //到这里再检验一次余额度是否足够
        UserEntity entity = userDao.selectOne(new QueryWrapper<UserEntity>().eq("username", properties.getUser()));
        if (ObjectUtil.objectIsNull(entity)) return;
        if (entity.getBalance() < properties.getDeduct()) {
            //发送邮件提醒余额不足
            EmailVoProperties emailVoProperties =
                    new EmailVoProperties(EmailVoProperties.INSUFFICIENT_BALANCE, entity.getEmail());
            rabbitTemplate.convertAndSend(GlobalValueToExchange.EMAIL_EXCHANGE, GlobalValueToExchange.EMAIL_QUEUE,
                    emailVoProperties, new CorrelationData(UUID.randomUUID().toString()));
            return;
        }
        RLock lock = redissonClient.getLock(properties.getUser() + FileController.DEDUCT_LOCK_NAME);
        lock.lock();//阻塞锁
        try {
            boolean update = this.update(new UpdateWrapper<UserEntity>()
                    .eq("username", properties.getUser())
                    .set("balance", entity.getBalance() - properties.getDeduct()));
            if (update) {
                EmailVoProperties emailVoProperties = new EmailVoProperties(entity.getEmail(), EmailVoProperties.CONSUMPTION, properties.getDeduct(), entity.getUsername(), BaseUtil.currentTime());
                rabbitTemplate.convertAndSend(GlobalValueToExchange.EMAIL_EXCHANGE, GlobalValueToExchange.EMAIL_QUEUE, emailVoProperties, new CorrelationData(UUID.randomUUID().toString()));
            }
        } finally {
            channel.basicAck(deliveryTag,false);
            lock.unlock();
        }
    }

    @Override
    public PageUtils getAllUserByCondition(Integer cur, Integer size, UserEntity user) {
        Page<UserEntity> page = new Page<>(cur, size);
        QueryWrapper<UserEntity> queryWrapper = new QueryWrapper<>();
        //模拟sql的写法
        //select * from sys_user where username =''
        if (user.getUsername() != null) {
            queryWrapper.like("username", user.getUsername());
        }
        if (user.getRoleId() != null) {
            queryWrapper.eq("role_id", user.getRoleId());
        }
        if (user.getEmail() != null) {
            queryWrapper.like("email", user.getEmail());
        }
        if (user.getMobile() != null) {
            queryWrapper.like("mobile", user.getMobile());
        }
        queryWrapper.eq("status", 1);
        //根据条件查询出来的分页信息对象
        Page<UserEntity> selectPage = userDao.selectPage(page, queryWrapper);
        List<UserEntity> records = selectPage.getRecords();
        PageUtils pageUtils = new PageUtils();
        pageUtils.setList(records.stream().map(e -> {
            RoleEntity role = roleService.getOne(new QueryWrapper<RoleEntity>().eq("id", e.getRoleId()));
            return new UserVo(e, role.getRoleName());
        }).collect(Collectors.toList()));
        pageUtils.setCurrPage((int) selectPage.getCurrent());

        pageUtils.setPageSize((int) selectPage.getSize());

        pageUtils.setTotalCount((int) selectPage.getTotal());

        pageUtils.setTotalPage((int) selectPage.getPages());
        return pageUtils;
    }

    @Override
    public String updateUserInfoById(UserVo vo, String name) {
        Long id = vo.getId();
        //当前操作数据的人信息
        UserEntity entity = userDao.selectOne(new QueryWrapper<UserEntity>().eq("username", name));

        UpdateWrapper<UserEntity> wrapper = new UpdateWrapper<>();
        List<UserEntity> info = userDao.getUpgradeExistInfo(vo);
        //设置修改条件的同时我们需要设置一个查询条件 也就是 库中没有人的数据和你相同
        if (!vo.getAvatar().isEmpty()) {
            wrapper.set("avatar", vo.getAvatar());
        }
        if (!vo.getMobile().isEmpty()) {
            wrapper.set("mobile", vo.getMobile());
        }
        if (!vo.getEmail().isEmpty()) {
            wrapper.set("email", vo.getEmail());
        }
        if (info.size() > 0) {
            return "含有相同数据";
        }
        //角色是否越权操作
        if (!vo.getRoleName().isEmpty()) {
            //这个vo是被操作对象
            Long bcz_role_id = Long.parseLong(vo.getRoleName());
            //如果当前操作对象的角色level 大于了 被操作对象的角色level 相同也不行 不允许这种操作  除非是最高级人物
            //我们需要查询操作对象roleid所对应的level和被操纵的对象对应的level来判断
            Long cz_role_id = entity.getRoleId();
            RoleEntity bcz_role = roleService.getOne(new QueryWrapper<RoleEntity>().eq("id", bcz_role_id));
            RoleEntity cz_role = roleService.getOne(new QueryWrapper<RoleEntity>().eq("id", cz_role_id));
            if (!name.equals("zsj")) {
                if (cz_role.getLevel() > bcz_role.getLevel()) {
                    return "越权操作!";
                }
                if (vo.getUsername().equals("zsj")) {
                    //当被操纵人是作者且操作人不为作者时 拒绝此请求
                    return "越权操作!";
                }
                wrapper.set("role_id", bcz_role_id);
            } else wrapper.set("role_id", bcz_role_id);
        }
        wrapper.eq("id", id);//最终归置
        return update(wrapper) ? "修改成功" : "修改失败";

    }

    @Override
    public void setLoginStatus(String username, boolean b) {
        if (b) this.update(new UpdateWrapper<UserEntity>().eq("username", username).set("login_status", "yeap"));
        else this.update(new UpdateWrapper<UserEntity>().eq("username", username).set("login_status", "out"));
    }

}