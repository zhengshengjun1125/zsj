package com.zsj.system.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zsj.system.vo.BlogHomeImgVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.zsj.system.entity.BlogHomeHpEntity;
import com.zsj.system.service.BlogHomeHpService;
import com.zsj.common.utils.PageUtils;
import com.zsj.common.utils.R;


/**
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
     * 初始化缓存背景图
     */
    public void initImgList() {

    }

    /**
     * 根据权限获取背景图
     *
     * @param name 账号
     * @return 背景图列表
     */
    @GetMapping("/getImgByName")
    public R getImgByName(@RequestHeader("System_api_authorize_name") String name) {
        if (name.equals("zsj")) {
            return R.ok().put("data", blogHomeHpService.listALL());
        }
        List<BlogHomeImgVo> list = blogHomeHpService.getByUserID(name);
        return R.ok().put("data", list);
    }


    @PostMapping("/insert")
    public R insertImgSelf(@RequestBody BlogHomeHpEntity entity) {
        entity.setCreateTime(new Date(System.currentTimeMillis()));
        boolean save = blogHomeHpService.
                saveOrUpdate(entity, new QueryWrapper<BlogHomeHpEntity>().
                        eq("belong_user_id", entity.getBelongUserId()));
        return save ? R.ok("添加成功") : R.error("添加失败");
    }

    /**
     * 删除
     */
    @GetMapping("/delete")
    public R delete(@RequestParam("id") Long id) {
        return blogHomeHpService.removeById(id) ? R.ok("删除成功") : R.error("未知异常，请联系管理员");
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = blogHomeHpService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
        BlogHomeHpEntity blogHomeHp = blogHomeHpService.getById(id);

        return R.ok().put("blogHomeHp", blogHomeHp);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody BlogHomeHpEntity blogHomeHp) {
        blogHomeHpService.save(blogHomeHp);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody BlogHomeHpEntity blogHomeHp) {
        blogHomeHpService.updateById(blogHomeHp);

        return R.ok();
    }



}
