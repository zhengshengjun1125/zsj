package com.zsj.system.service.impl;

import com.zsj.system.entity.UserEntity;
import com.zsj.system.service.UserService;
import com.zsj.system.vo.BlogHomeImgVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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


    @Autowired
    @Lazy
    private UserService userService;


    @Autowired
    private BlogHomeHpDao blogHomeHpDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<BlogHomeHpEntity> page = this.page(
                new Query<BlogHomeHpEntity>().getPage(params),
                new QueryWrapper<BlogHomeHpEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<BlogHomeImgVo> getByUserID(String name) {
        QueryWrapper<UserEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", name);
        UserEntity one = userService.getOne(queryWrapper);
        QueryWrapper<BlogHomeHpEntity> blogHomeHpEntityQueryWrapper = new QueryWrapper<>();
        blogHomeHpEntityQueryWrapper.eq("belong_user_id", one.getId());
        List<BlogHomeHpEntity> blogHomeHpEntities = baseMapper.selectList(blogHomeHpEntityQueryWrapper);
        return blogHomeHpEntities.stream().map(entity -> {
            return new BlogHomeImgVo(entity, name);
        }).collect(Collectors.toList());
    }

    @Override
    public List<BlogHomeImgVo> listALL() {
        return blogHomeHpDao.getAllImgList();
    }

}