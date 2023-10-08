package com.zsj.article.controller;

import java.util.Arrays;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.zsj.article.entity.ClassEntity;
import com.zsj.article.service.ClassService;
import com.zsj.common.utils.PageUtils;
import com.zsj.common.utils.R;


/**
 * @author zsj
 * @email zsjemail666@163.com
 * @date 2023-10-07 20:47:17
 */
@RestController
@RequestMapping("article/class")
public class ClassController {
    @Autowired
    private ClassService classService;


    @GetMapping("/getAllClass")
    public R getAllClass() {
        //获取所有分类并且以树形结果展示
        return R.ok().put("data", classService.listTree());
    }


    @GetMapping("/getAllClassEasy")
    public R getAllClassEasy() {
        //获取所有分类并且以树形结果展示
        return R.ok().put("data", classService.list());
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = classService.queryPage(params);
        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
        ClassEntity classEntity = classService.getById(id);

        return R.ok().put("class", classEntity);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody ClassEntity classEntity) {
        classService.save(classEntity);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody ClassEntity classEntity) {
        classService.updateById(classEntity);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        classService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
