package com.zsj.system.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zsj.system.entity.RoleEntity;
import com.zsj.system.service.RoleService;
import com.zsj.system.vo.UserVo;
import com.zsj.system.vo.UserVoPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Role;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zsj.common.utils.PageUtils;
import com.zsj.common.utils.Query;

import com.zsj.system.dao.UserDao;
import com.zsj.system.entity.UserEntity;
import com.zsj.system.service.UserService;


@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserDao, UserEntity> implements UserService {

    @Autowired
    @Lazy
    private UserDao userDao;


    @Autowired
    @Lazy
    private RoleService roleService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<UserEntity> page = this.page(
                new Query<UserEntity>().getPage(params),
                new QueryWrapper<UserEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils getAllUserByCondition(Integer cur, Integer size, UserEntity user) {
        Page<UserEntity> page = new Page<>(cur, size);
        QueryWrapper<UserEntity> queryWrapper = new QueryWrapper<>();
        //模拟sql的写法
        if (user.getUsername() != null) {
            queryWrapper.like("username", user.getUsername());
        }
        if (user.getRoleId() != null) {
            queryWrapper.eq("role_id", user.getRoleId());
        }
        if (user.getEmail() != null) {
            queryWrapper.like("email", user.getEmail());
        }
        if (user.getMobile() != null) {
            queryWrapper.like("mobile", user.getMobile());
        }
        //根据条件查询出来的分页信息对象
        Page<UserEntity> selectPage = userDao.selectPage(page, queryWrapper);
        List<UserEntity> records = selectPage.getRecords();
        PageUtils pageUtils = new PageUtils();
        pageUtils.setList(records.stream().map(e -> {
            RoleEntity role = roleService.getOne(new QueryWrapper<RoleEntity>().eq("id", e.getRoleId()));
            return new UserVo(e, role.getRoleName());
        }).collect(Collectors.toList()));
        pageUtils.setCurrPage((int) selectPage.getCurrent());

        pageUtils.setPageSize((int) selectPage.getSize());

        pageUtils.setTotalCount((int) selectPage.getTotal());

        pageUtils.setTotalPage((int) selectPage.getPages());
        return pageUtils;
    }

}