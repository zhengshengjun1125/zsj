package com.zsj.system.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zsj.common.utils.PageUtils;
import com.zsj.common.utils.Query;

import com.zsj.system.dao.UserFQDao;
import com.zsj.system.entity.UserFQEntity;
import com.zsj.system.service.UserFQService;


@Service("userFQService")
public class UserFQServiceImpl extends ServiceImpl<UserFQDao, UserFQEntity> implements UserFQService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<UserFQEntity> page = this.page(
                new Query<UserFQEntity>().getPage(params),
                new QueryWrapper<UserFQEntity>()
        );

        return new PageUtils(page);
    }

}