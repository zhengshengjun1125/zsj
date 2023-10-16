package com.zsj.system.controller;

import java.util.Arrays;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zsj.system.entity.BlogHomeHpEntity;
import com.zsj.system.service.BlogHomeHpService;
import com.zsj.common.utils.PageUtils;
import com.zsj.common.utils.R;



/**
 * 
 *
 * @author zsj
 * @email zsjemail666@163.com
 * @date 2023-10-16 10:48:12
 * @description 作为每个博客的封面图 根据不同用户展示不同的主页面背景图
 */
@RestController
@RequestMapping("system/bloghomehp")
public class BlogHomeHpController {
    @Autowired
    private BlogHomeHpService blogHomeHpService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = blogHomeHpService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
		BlogHomeHpEntity blogHomeHp = blogHomeHpService.getById(id);

        return R.ok().put("blogHomeHp", blogHomeHp);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody BlogHomeHpEntity blogHomeHp){
		blogHomeHpService.save(blogHomeHp);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody BlogHomeHpEntity blogHomeHp){
		blogHomeHpService.updateById(blogHomeHp);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
		blogHomeHpService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
