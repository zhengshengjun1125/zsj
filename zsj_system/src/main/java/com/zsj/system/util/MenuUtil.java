package com.zsj.system.util;

import com.zsj.system.entity.MenuEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author https://gitee.com/zhengshengjun
 * @date 2023/10/30.
 */
public class MenuUtil {

    /**
     * 使用递归方法建菜单
     * @param MenuEntityList
     * @return
     */
    public static List<MenuEntity> buildTree(List<MenuEntity> MenuEntityList) {
        List<MenuEntity> trees = new ArrayList<>();
        for (MenuEntity MenuEntity : MenuEntityList) {
            if (MenuEntity.getParentId().longValue() == 0) {
                trees.add(findChildren(MenuEntity,MenuEntityList));
            }
        }
        return trees;
    }

    /**
     * 递归查找子节点
     * @param treeNodes
     * @return
     */
    public static MenuEntity findChildren(MenuEntity MenuEntity, List<MenuEntity> treeNodes) {
        MenuEntity.setChildren(new ArrayList<MenuEntity>());
        for (MenuEntity it : treeNodes) {
            if(MenuEntity.getId().longValue() == it.getParentId().longValue()) {
                //if (MenuEntity.getChildren() == null) {
                //    MenuEntity.setChildren(new ArrayList<>());
                //}
                MenuEntity.getChildren().add(findChildren(it,treeNodes));
            }
        }
        return MenuEntity;
    }
}
