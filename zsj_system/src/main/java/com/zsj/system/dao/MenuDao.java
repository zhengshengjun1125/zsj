package com.zsj.system.dao;

import com.zsj.system.entity.MenuEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 菜单表
 * 
 * @author zsj
 * @email zsjemail666@163.com
 * @date 2023-10-30 17:19:44
 */
@Mapper
public interface MenuDao extends BaseMapper<MenuEntity> {
    public abstract List<MenuEntity> selectAll();

    List<MenuEntity> selectListByUserId(Long userId);

//    public abstract  void insert(MenuEntity sysMenu);

//
//    public abstract void updateById(MenuEntity sysMenu);

}
