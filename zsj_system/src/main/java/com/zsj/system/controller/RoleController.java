package com.zsj.system.controller;

import java.util.*;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.zsj.system.vo.RoleByIndex;
import com.zsj.system.vo.RoleParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.zsj.system.entity.RoleEntity;
import com.zsj.system.service.RoleService;
import com.zsj.common.utils.PageUtils;
import com.zsj.common.utils.R;

import javax.management.relation.Role;


/**
 * @author zsj
 * @email zsjemail666@163.com
 * @date 2023-10-09 13:02:12
 */
@RestController
@RequestMapping("system/role")
public class RoleController {
    @Autowired
    private RoleService roleService;


    /**
     * 角色信息分页
     *
     * @param cur    当前页数
     * @param size   每页展示几条数据
     * @param params 请求条件 也就是角色名称
     * @return 换回对应的角色信息表
     */
    @PostMapping("/page/{cur}/{size}")
    public R findByPage(@PathVariable("cur") Integer cur,
                        @PathVariable("size") Integer size,
                        @RequestBody RoleParams params) {
        return R.ok().put("data", roleService.findByPage(cur, size, params));
    }


    /**
     * 添加角色
     */
    @PostMapping("/add")
    public R addRole(@RequestBody RoleEntity entity) {
        if (entity.getRoleName().equals("")) return R.error("你不能添加空字符");
        entity.setCreateTime(new Date(System.currentTimeMillis()));
        entity.setUpdateTime(new Date(System.currentTimeMillis()));
        entity.setStatus(1);
        long count = roleService.count(new QueryWrapper<RoleEntity>().eq("role_name", entity.getRoleName()));
        if (count != 0) {
            //说明数据存在 将status改回来就行 并且设置默认level
            UpdateWrapper<RoleEntity> roleEntityUpdateWrapper = new UpdateWrapper<>();
            roleEntityUpdateWrapper.set("status",1);
            roleEntityUpdateWrapper.eq("role_name",entity.getRoleName());
            boolean update = roleService.update(roleEntityUpdateWrapper);
            return update?R.ok("添加成功"):R.error("添加失败");
        };
        return roleService.save(entity) ? R.ok("添加成功") : R.error("添加失败");
    }

    @PostMapping("/updateById")
    public R updateRole(@RequestBody RoleEntity role) {
        if (Objects.isNull(role)) return R.error("查无此人");
        Long id = role.getId();
        return roleService
                .update(new UpdateWrapper<RoleEntity>()
                        .eq("id", id)
                        .set("role_name", role.getRoleName())) ? R.ok("修改成功") : R.error("修改失败");
    }

    @GetMapping("/getAll")
    public R getAllRolesByIndex(){
        List<RoleEntity> list = roleService.list(new QueryWrapper<RoleEntity>().eq("status",1));
        return R.ok().put("data",list.stream().map(RoleByIndex::new).collect(Collectors.toList()));
    }

    /**
     * 删除角色
     */
    @PostMapping("/remove")
    public R removeRole(@RequestBody RoleEntity entity) {
        Long id = entity.getId();
        if (Objects.isNull(id)) return R.error("删除失败");
        //逻辑删除
        return roleService.update(new UpdateWrapper<RoleEntity>().eq("id", id)
                .set("status", 0))
                ?
                R.ok("删除成功")
                :
                R.error("删除失败");
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = roleService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
        RoleEntity role = roleService.getById(id);

        return R.ok().put("role", role);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody RoleEntity role) {
        roleService.save(role);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody RoleEntity role) {
        roleService.updateById(role);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        roleService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
