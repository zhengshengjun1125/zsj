package com.zsj.system.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zsj.common.utils.PageUtils;
import com.zsj.common.utils.Query;

import com.zsj.system.dao.BlogHomeHpDao;
import com.zsj.system.entity.BlogHomeHpEntity;
import com.zsj.system.service.BlogHomeHpService;


@Service("blogHomeHpService")
public class BlogHomeHpServiceImpl extends ServiceImpl<BlogHomeHpDao, BlogHomeHpEntity> implements BlogHomeHpService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<BlogHomeHpEntity> page = this.page(
                new Query<BlogHomeHpEntity>().getPage(params),
                new QueryWrapper<BlogHomeHpEntity>()
        );

        return new PageUtils(page);
    }

}