package com.zsj.system.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zsj.common.utils.PageUtils;
import com.zsj.common.utils.Query;

import com.zsj.system.dao.UserTokenDao;
import com.zsj.system.entity.UserTokenEntity;
import com.zsj.system.service.UserTokenService;


@Service("userTokenService")
public class UserTokenServiceImpl extends ServiceImpl<UserTokenDao, UserTokenEntity> implements UserTokenService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<UserTokenEntity> page = this.page(
                new Query<UserTokenEntity>().getPage(params),
                new QueryWrapper<UserTokenEntity>()
        );

        return new PageUtils(page);
    }

}