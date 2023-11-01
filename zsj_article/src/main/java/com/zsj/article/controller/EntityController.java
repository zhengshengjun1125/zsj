package com.zsj.article.controller;

import java.util.*;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.gson.reflect.TypeToken;
import com.zsj.article.Page.ArticlePage;
import com.zsj.article.vo.EntityVoForHomePage;
import com.zsj.common.utils.GsonUtil;
import com.zsj.common.utils.ObjectUtil;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.lang.Nullable;
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

    @Autowired
    @Lazy
    private StringRedisTemplate stringRedisTemplate;


    /**
     * 根据周点赞数量来排序文章列表 todo
     *
     * @return
     */
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
     * 首页展示的火热文章
     */
    @GetMapping("/getAllPageToHome")
    public R getAllPageLimit(@Param("cur") Integer cur, @Param("size") Integer size) {
        Page<EntityEntity> page = new Page<>(cur, size);
        return R.ok().put("data", entityService.getAllArticle(page));
    }

    /**
     * 返回所有文章并且附带文章分类信息并且分页 并且按条件查询
     */
    @PostMapping("/getAllPageByIndex/{cur}/{size}")
    public R getAllPageLimit(@RequestHeader("system_api_Authorize_name") String name,
                             @PathVariable("cur") Integer cur,
                             @PathVariable("size") Integer size,
                             @Nullable @RequestBody EntityEntity entity) {
        ValueOperations<String, String> ops =
                stringRedisTemplate.opsForValue();
        //进行权限控制 每个用户只能查到自己的
        String json = ops.get("articlePage/" + cur + '/' + size+'/'+name);
        if (ObjectUtil.isNullOrEmpty(json)) {
            //根据条件进行文章信息的查询
            ArticlePage articlePage = getArticlePage(name, cur, size, entity, ops);
            return R.ok().put("data", articlePage);
        }
        return R.ok().put("data", GsonUtil.gson.fromJson(json, ArticlePage.class));
    }

    private ArticlePage getArticlePage(String name, Integer cur, Integer size, EntityEntity entity, ValueOperations<String, String> ops) {
        ArticlePage articlePage = entityService.getAllArticleByUserName(new Page<>(cur, size), entity, name);
        String gsonJson = GsonUtil.gson.toJson(articlePage);
        ops.set("articlePage/" + cur + '/' + size+'/'+name, gsonJson);
        return articlePage;
    }

    /**
     * 根据id修改内容content
     */
    @PostMapping("/rebuildArt")
    public R rebuildArtContent(@RequestBody EntityEntity entity) {
        if (entity != null) {
            Long id = entity.getId();
            String artContent = entity.getArtContent();
            String artTitle = entity.getArtTitle();
            if (id != null && artContent != null && artTitle != null && !artTitle.equals("")) {
                //更新缓存
                boolean b = entityService.resetContentById(id, artContent, artTitle);
                extracted();
                //传入的参数不能为空
                return b ?
                        R.ok("修改成功")
                        : R.error("未知错误,请联系管理员");
            }
        }
        return R.error("非法操作");
    }

    private void extracted() {
        Set<String> keys = stringRedisTemplate.keys("articlePage/*");
        stringRedisTemplate.delete(keys);//删除缓存中的分页文章信息 下次拿的时候更新缓存即可
    }

    /**
     * 修改文章分类
     */
    @PostMapping("/rebuildArtType")
    public R rebuildArtTypeById(@RequestBody EntityEntity entity) {
        if (entity != null) {
            Long id = entity.getId();
            Long artClassId = entity.getArtClassId();
            if (id != null && artClassId != null) {
                UpdateWrapper<EntityEntity> wrapper = new UpdateWrapper<>();
                wrapper.set("art_class_id", artClassId);
                wrapper.eq("id", id);
                extracted();
                return entityService.update(wrapper) ? R.ok("修改成功") : R.error("修改失败");
            }
        }
        return R.error("非法操作");
    }

    /**
     * 新增文章
     * 当前token用户为作者
     * 并且设置各种访问量为0
     */
    @PostMapping("/joinArtFamily")
    public R joinArtFamily(@RequestHeader("system_api_Authorize_name") String auther,
                           @RequestBody EntityEntity entity) {
        //初始化文章实体
        entity.setArtRequestDay(0L);
        entity.setArtRequestMonth(0L);
        entity.setArtRequestTotal(0L);
        entity.setArtStatus(1);
        entity.setArtAuther(auther);
        entity.setCreateTime(new Date(System.currentTimeMillis()));
        entity.setUpdateTime(new Date(System.currentTimeMillis()));
        boolean save = entityService.save(entity);
        extracted();
        return save ? R.ok("添加成功") : R.error("添加失败");
    }

    /**
     * 逻辑删除文章
     * 和之前一样自己可以删除自己的 zsj可以删除所有人的
     */
    @PostMapping("/cancelArtFamilyById")
    public R cancelArtFamilyById(@RequestHeader("system_api_Authorize_name") String cur,
                                 @RequestBody EntityEntity entity) {
        if (entity != null) {
            Long id = entity.getId();
            if (id != null) {
                UpdateWrapper<EntityEntity> wrapper = new UpdateWrapper<>();
                if (cur.equals("zsj")) {
                    //是root直接删
                    wrapper.eq("id", id);
                    wrapper.set("art_status", 0);
                    return entityService.update(wrapper) ? R.ok("删除成功") : R.error("删除失败");
                } else {
                    //只能删除自己的文章
                    QueryWrapper<EntityEntity> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("id", entity.getId());
                    EntityEntity one = entityService.getOne(entityService.query());
                    if (one.getArtAuther().equals(cur)) {
                        //可以删
                        wrapper.eq("id", id);
                        wrapper.set("art_status", 0);
                        boolean update = entityService.update(wrapper);
                        extracted();
                        return update ? R.ok("删除成功") : R.error("删除失败");
                    }
                }
            }
        }
        return R.error("删除失败");
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
