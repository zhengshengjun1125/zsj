package com.zsj.article.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zsj.common.utils.PageUtils;
import com.zsj.common.utils.Query;

import com.zsj.article.dao.EntityDao;
import com.zsj.article.entity.EntityEntity;
import com.zsj.article.service.EntityService;


@Service("entityService")
public class EntityServiceImpl extends ServiceImpl<EntityDao, EntityEntity> implements EntityService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<EntityEntity> page = this.page(
                new Query<EntityEntity>().getPage(params),
                new QueryWrapper<EntityEntity>()
        );

        return new PageUtils(page);
    }

}