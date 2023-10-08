package com.zsj.article.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zsj.article.vo.EntityVoForHomePage;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.zsj.article.entity.EntityEntity;
import com.zsj.article.service.EntityService;
import com.zsj.common.utils.PageUtils;
import com.zsj.common.utils.R;


/**
 * @author zsj
 * @email zsjemail666@163.com
 * @date 2023-10-07 20:47:18
 */
@RestController
@RequestMapping("article/entity")
public class EntityController {
    @Autowired
    private EntityService entityService;


    @GetMapping("/homeInfo")
    public R getHomeInfo() {
        List<EntityEntity> list = entityService.list();
        List<EntityVoForHomePage> result = new ArrayList<>();
        for (EntityEntity entityEntity : list) {
            result.add(new EntityVoForHomePage(entityEntity));
        }
        return R.ok("首页信息").put("data", result);
    }

    /**
     * 返回所有文章并且附带文章分类信息
     */
    @GetMapping("/getAllPage")
    public R getAllPage() {
        return R.ok().put("data",
                entityService.getAllArticle());
    }

    /**
     * 返回所有文章并且附带文章分类信息 并且分页
     */
    @GetMapping("/getAllPageByIndex")
    public R getAllPageLimit(@Param("cur") Integer cur,@Param("size")Integer size) {
        Page<EntityEntity> page = new Page<>(cur,size);
        return R.ok().put("data",entityService.getAllArticle(page));
    }



    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = entityService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
        EntityEntity entity = entityService.getById(id);

        return R.ok().put("entity", entity);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody EntityEntity entity) {
        entityService.save(entity);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody EntityEntity entity) {
        entityService.updateById(entity);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        entityService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
