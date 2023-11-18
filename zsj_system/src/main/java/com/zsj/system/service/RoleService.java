package com.zsj.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zsj.common.utils.PageUtils;
import com.zsj.system.entity.RoleEntity;
import com.zsj.system.vo.RoleParams;

import java.util.Map;

/**
 * 
 *
 * @author zsj
 * @email zsjemail666@163.com
 * @date 2023-10-09 13:02:12
 */
public interface RoleService extends IService<RoleEntity> {

    IPage<RoleEntity> findByPage(Integer cur, Integer size, RoleParams roleParams);

    PageUtils queryPage(Map<String, Object> params);
}

