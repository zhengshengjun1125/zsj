package com.zsj.system.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.zsj.system.vo.SysMenuVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.zsj.system.entity.MenuEntity;
import com.zsj.system.service.MenuService;
import com.zsj.common.utils.PageUtils;
import com.zsj.common.utils.R;


/**
 * 菜单表
 *
 * @author zsj
 * @email zsjemail666@163.com
 * @date 2023-10-30 17:19:44
 */
@RestController
@RequestMapping("system/menu")
public class MenuController {
    @Autowired
    private MenuService menuService;


    @GetMapping("/menus")
    public R menus(@RequestHeader("system_api_Authorize_name") String name) {
        List<SysMenuVo> sysMenuVoList =  menuService.findUserMenuList(name) ;
        return R.ok().put("data",sysMenuVoList);
    }

    @GetMapping("/findNodes")
    public R findNodes() {
        List<MenuEntity> list = menuService.findNodes();
        return R.ok().put("data", list);
    }


    /**
     * 保存
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public R save(@RequestBody MenuEntity menu) {
        if (menuService.save(menu)) return R.ok();
        return R.error();
    }


    @PutMapping("/updateById")
    public R updateById(@RequestBody MenuEntity sysMenu) {
        if (menuService.updateById(sysMenu)) return R.ok();
        return R.error();
    }

    @DeleteMapping("/removeById/{id}")
    public R removeById(@PathVariable Long id) {
        UpdateWrapper<MenuEntity> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id",id);
        updateWrapper.set("is_deleted",0);
        if (menuService.update(updateWrapper)) return R.ok("删除成功");
        else return R.error("删除失败");
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = menuService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
        MenuEntity menu = menuService.getById(id);
        return R.ok().put("menu", menu);
    }


    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody MenuEntity menu) {
        menuService.updateById(menu);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        menuService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
