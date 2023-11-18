package com.zsj.Websocket.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zsj.common.utils.PageUtils;
import com.zsj.common.utils.Query;

import com.zsj.Websocket.dao.RelationMessageDao;
import com.zsj.Websocket.entity.RelationMessageEntity;
import com.zsj.Websocket.service.RelationMessageService;


@Service("relationMessageService")
public class RelationMessageServiceImpl extends ServiceImpl<RelationMessageDao, RelationMessageEntity> implements RelationMessageService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<RelationMessageEntity> page = this.page(
                new Query<RelationMessageEntity>().getPage(params),
                new QueryWrapper<RelationMessageEntity>()
        );

        return new PageUtils(page);
    }

}