package com.zsj.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zsj.common.utils.PageUtils;
import com.zsj.system.entity.MenuEntity;
import com.zsj.system.vo.SysMenuVo;

import java.util.List;
import java.util.Map;

/**
 * 菜单表
 *
 * @author zsj
 * @email zsjemail666@163.com
 * @date 2023-10-30 17:19:44
 */
public interface MenuService extends IService<MenuEntity> {

    PageUtils queryPage(Map<String, Object> params);

    public List<MenuEntity> findNodes();

    List<SysMenuVo> findUserMenuList(String username);
}

