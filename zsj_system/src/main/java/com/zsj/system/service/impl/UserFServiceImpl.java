package com.zsj.system.service.impl;

import com.zsj.common.utils.ObjectUtil;
import com.zsj.system.dao.UserDao;
import com.zsj.system.entity.UserEntity;
import com.zsj.system.vo.Friend;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zsj.common.utils.PageUtils;
import com.zsj.common.utils.Query;

import com.zsj.system.dao.UserFDao;
import com.zsj.system.entity.UserFEntity;
import com.zsj.system.service.UserFService;


@Service("userFService")
public class UserFServiceImpl extends ServiceImpl<UserFDao, UserFEntity> implements UserFService {


    @Autowired
    @Lazy
    UserDao userDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<UserFEntity> page = this.page(
                new Query<UserFEntity>().getPage(params),
                new QueryWrapper<UserFEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<Friend> getCurUserFVo(List<UserFEntity> list) {
        //这个list已经过滤过了 不需要在对status进行过滤
        List<Friend> ans = new ArrayList<>();
        for (UserFEntity userFEntity : list) {
            Long userFId = userFEntity.getUserFId();//好友id
            UserEntity entity = userDao.selectOne(new QueryWrapper<UserEntity>().eq("id", userFId));
            if (ObjectUtil.objectIsNotNull(entity)) ans.add(new Friend(entity));
        }
        return ans;
    }

}