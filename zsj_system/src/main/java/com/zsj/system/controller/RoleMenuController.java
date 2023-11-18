package com.zsj.system.controller;

import java.util.Arrays;
import java.util.Map;

import com.zsj.system.vo.AssginMenuDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.zsj.system.entity.RoleMenuEntity;
import com.zsj.system.service.RoleMenuService;
import com.zsj.common.utils.PageUtils;
import com.zsj.common.utils.R;


/**
 * 角色菜单
 *
 * @author zsj
 * @email zsjemail666@163.com
 * @date 2023-10-30 18:43:10
 */
@RestController
@RequestMapping("system/rolemenu")
public class RoleMenuController {
    @Autowired
    private RoleMenuService roleMenuService;


    @PostMapping("/doAssign")
    public R doAssign(@RequestBody AssginMenuDto assginMenuDto) {
        roleMenuService.doAssign(assginMenuDto);
        return R.ok();
    }


    @GetMapping(value = "/findSysRoleMenuByRoleId/{roleId}")
    public R findSysRoleMenuByRoleId(@PathVariable(value = "roleId") Long roleId) {
        Map<String, Object> sysRoleMenuList = roleMenuService.findSysRoleMenuByRoleId(roleId);
        return R.ok().put("list", sysRoleMenuList);
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = roleMenuService.queryPage(params);
        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
        RoleMenuEntity roleMenu = roleMenuService.getById(id);

        return R.ok().put("roleMenu", roleMenu);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody RoleMenuEntity roleMenu) {
        roleMenuService.save(roleMenu);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody RoleMenuEntity roleMenu) {
        roleMenuService.updateById(roleMenu);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        roleMenuService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
