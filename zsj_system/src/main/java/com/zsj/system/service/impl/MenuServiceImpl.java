package com.zsj.system.service.impl;

import com.alibaba.nacos.client.naming.utils.CollectionUtils;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.zsj.common.utils.ObjectUtil;
import com.zsj.system.entity.UserEntity;
import com.zsj.system.service.UserService;
import com.zsj.system.util.MenuUtil;
import com.zsj.system.vo.SysMenuVo;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zsj.common.utils.PageUtils;
import com.zsj.common.utils.Query;

import com.zsj.system.dao.MenuDao;
import com.zsj.system.entity.MenuEntity;
import com.zsj.system.service.MenuService;


@Service("menuService")
public class MenuServiceImpl extends ServiceImpl<MenuDao, MenuEntity> implements MenuService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<MenuEntity> page = this.page(
                new Query<MenuEntity>().getPage(params),
                new QueryWrapper<MenuEntity>()
        );

        return new PageUtils(page);
    }

    @Autowired
    private MenuDao sysMenuMapper;

    @Autowired
    @Lazy
    private UserService userService;

    @Override
    public List<MenuEntity> findNodes() {
        List<MenuEntity> sysMenuList = sysMenuMapper.selectAll();
        if (CollectionUtils.isEmpty(sysMenuList)) return null;
        return MenuUtil.buildTree(sysMenuList);
    }

    @Override
    public List<SysMenuVo> findUserMenuList(String username) {

        QueryWrapper<UserEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        UserEntity one = userService.getOne(queryWrapper);
        // 获取当前登录用户的角色id
        Long userId = one.getRoleId();
        List<MenuEntity> sysMenuList = sysMenuMapper.selectListByUserId(userId);
        //构建树形数据
        List<MenuEntity> sysMenuTreeList = MenuUtil.buildTree(sysMenuList);
        return this.buildMenus(sysMenuTreeList);
    }

    @Override
    public boolean updateMenuByTableId(MenuEntity sysMenu) {
        Long id = sysMenu.getId();
        UpdateWrapper<MenuEntity> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", id);
        Integer status = sysMenu.getStatus();
        String title = sysMenu.getTitle();
        String component = sysMenu.getComponent();
        Integer sortValue = sysMenu.getSortValue();
        if (ObjectUtil.objectIsNotNull(status)) {
            updateWrapper.set("status", status);
        }
        if (ObjectUtil.objectIsNotNull(title)) {
            updateWrapper.set("title", title);
        }
        if (ObjectUtil.objectIsNotNull(component)) {
            updateWrapper.set("component", component);
        }
        if (ObjectUtil.objectIsNotNull(sortValue)) {
            updateWrapper.set("sort_value", sortValue);
        }
        return this.update(updateWrapper);
    }

    // 将List<SysMenu>对象转换成List<SysMenuVo>对象
    private List<SysMenuVo> buildMenus(List<MenuEntity> menus) {

        List<SysMenuVo> sysMenuVoList = new LinkedList<SysMenuVo>();
        for (MenuEntity sysMenu : menus) {
            SysMenuVo sysMenuVo = new SysMenuVo();
            sysMenuVo.setTitle(sysMenu.getTitle());
            sysMenuVo.setName(sysMenu.getComponent());
            List<MenuEntity> children = sysMenu.getChildren();
            if (!CollectionUtils.isEmpty(children)) {
                sysMenuVo.setChildren(buildMenus(children));
            }
            sysMenuVoList.add(sysMenuVo);
        }
        return sysMenuVoList;
    }

}