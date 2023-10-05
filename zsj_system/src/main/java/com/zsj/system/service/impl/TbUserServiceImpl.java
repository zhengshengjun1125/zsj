package com.zsj.system.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zsj.common.utils.PageUtils;
import com.zsj.common.utils.Query;

import com.zsj.system.dao.TbUserDao;
import com.zsj.system.entity.TbUserEntity;
import com.zsj.system.service.TbUserService;


@Service("tbUserService")
public class TbUserServiceImpl extends ServiceImpl<TbUserDao, TbUserEntity> implements TbUserService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<TbUserEntity> page = this.page(
                new Query<TbUserEntity>().getPage(params),
                new QueryWrapper<TbUserEntity>()
        );

        return new PageUtils(page);
    }

}