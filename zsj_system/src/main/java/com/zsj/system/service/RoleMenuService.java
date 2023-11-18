package com.zsj.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zsj.common.utils.PageUtils;
import com.zsj.system.entity.RoleMenuEntity;
import com.zsj.system.vo.AssginMenuDto;

import java.util.Map;

/**
 * 角色菜单
 *
 * @author zsj
 * @email zsjemail666@163.com
 * @date 2023-10-30 18:43:10
 */
public interface RoleMenuService extends IService<RoleMenuEntity> {

    PageUtils queryPage(Map<String, Object> params);

    Map<String, Object> findSysRoleMenuByRoleId(Long roleId);

    void doAssign(AssginMenuDto assginMenuDto);
}

