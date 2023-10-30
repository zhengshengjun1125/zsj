package com.zsj.system.dao;

import com.zsj.system.entity.RoleMenuEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zsj.system.vo.AssginMenuDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 角色菜单
 *
 * @author zsj
 * @email zsjemail666@163.com
 * @date 2023-10-30 18:43:10
 */
@Mapper
public interface RoleMenuDao extends BaseMapper<RoleMenuEntity> {
    List<Long> findSysRoleMenuByRoleId(Long roleId);

    void deleteByRoleId(Long roleId);

    void doAssign(AssginMenuDto assginMenuDto);
}
