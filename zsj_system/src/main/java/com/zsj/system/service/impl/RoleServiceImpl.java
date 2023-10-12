package com.zsj.system.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zsj.system.vo.RoleParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zsj.common.utils.PageUtils;
import com.zsj.common.utils.Query;

import com.zsj.system.dao.RoleDao;
import com.zsj.system.entity.RoleEntity;
import com.zsj.system.service.RoleService;


@Service("roleService")
public class RoleServiceImpl extends ServiceImpl<RoleDao, RoleEntity> implements RoleService {

    @Autowired
    private RoleDao roleDao;


    @Override
    public IPage<RoleEntity> findByPage(Integer cur, Integer size, RoleParams roleParams) {
        Page<RoleEntity> page = new Page<>(cur,size);
        return roleDao.selectPage(page,
                new QueryWrapper<RoleEntity>()
                        .like("role_name", roleParams.getRoleName())
                        .eq("status",1));
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<RoleEntity> page = this.page(
                new Query<RoleEntity>().getPage(params),
                new QueryWrapper<RoleEntity>()
        );

        return new PageUtils(page);
    }

}