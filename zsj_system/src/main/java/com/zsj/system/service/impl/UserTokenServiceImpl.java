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

    @Override
    public boolean saveOrUpdateToken(UserTokenEntity entity) {
        Long userId = entity.getUserId();
        UserTokenEntity one = baseMapper.selectOne(new QueryWrapper<UserTokenEntity>().eq("user_id", userId));
        try {
            if (null != one) {
                //修改
                int i = baseMapper.updateToken(entity);
            }else {
                int i = baseMapper.saveToken(entity);
            }
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }


}