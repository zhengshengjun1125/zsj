package com.zsj.system.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rabbitmq.client.Channel;
import com.zsj.common.utils.GlobalValueToExchange;
import com.zsj.common.utils.ObjectUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zsj.common.utils.PageUtils;
import com.zsj.common.utils.Query;

import com.zsj.system.dao.LogDao;
import com.zsj.system.entity.LogEntity;
import com.zsj.system.service.LogService;


@Service("logService")
@RabbitListener(queues = {GlobalValueToExchange.LOG_QUEUE})//监听日志队列
@Slf4j
public class LogServiceImpl extends ServiceImpl<LogDao, LogEntity> implements LogService {


    @Autowired
    private LogDao logDao;


    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<LogEntity> page = this.page(
                new Query<LogEntity>().getPage(params),
                new QueryWrapper<LogEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<LogEntity> getTen() {
        return logDao.getTen();
    }

    @Override
    public Page<LogEntity> pageByCondition(int cur, int size, LogEntity entity) {
        Page<LogEntity> entityPage = new Page<>(cur, size);
        QueryWrapper<LogEntity> queryWrapper = new QueryWrapper<>();
        if (!ObjectUtil.isNullOrEmpty(entity.getIp())) {
            queryWrapper.like("ip", entity.getIp());
        }
        if (!ObjectUtil.isNullOrEmpty(entity.getUsername())) {
            queryWrapper.like("username", entity.getUsername());
        }
        if (!ObjectUtil.isNullOrEmpty(entity.getMethod())) {
            queryWrapper.like("method", entity.getMethod().toUpperCase());
        }
        if (!ObjectUtil.isNullOrEmpty(entity.getOperation())) {
            queryWrapper.like("operation", entity.getOperation());
        }
        return baseMapper.selectPage(entityPage, queryWrapper);
    }


    @RabbitHandler
    public void saveLog(Message message, com.zsj.common.utils.LogEntity logEntity, Channel channel) {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();//当前消息id
        this.save(new com.zsj.system.entity.LogEntity(logEntity));
        try {
            //单个应答
            channel.basicAck(deliveryTag, false);
            log.info("接收到的消息为{},且保存成功", logEntity);
        } catch (IOException e) {
            log.error("出现异常 Delivertag为{}", deliveryTag);
            throw new RuntimeException(e);
        }
    }
}