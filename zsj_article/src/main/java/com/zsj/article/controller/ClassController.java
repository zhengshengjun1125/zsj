package com.zsj.article.controller;

import java.util.*;
import java.util.concurrent.TimeUnit;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zsj.article.vo.ClassVoForTree;
import com.zsj.article.vo.ClassVoForTreeForElement;
import com.zsj.common.annotation.SysLog;
import com.zsj.common.utils.GsonUtil;
import io.netty.util.internal.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;
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


    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @SysLog("新增根节点")
    @PostMapping("/setRootClass")
    public R setRootClass(@RequestHeader("system_api_Authorize_name") String name,
                          @Nullable @RequestBody ClassEntity entity) {
        //设置新的根节点 如果根节点已经存在就不让设置 如果节点是存在的 但是status为0  那就恢复status为1
        if (entity != null) {
            //初始化实体
            entity.setClassCreater(name);
            entity.setClassFatherId(0L);
            entity.setCreateTime(new Date(System.currentTimeMillis()));
            entity.setUpdateTime(new Date(System.currentTimeMillis()));
            entity.setClassStatus(1);

            QueryWrapper<ClassEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("class_name", entity.getClassName());
            //分情况判断
            ClassEntity one = classService.getOne(queryWrapper);
            if (!Objects.isNull(one)) {
                Integer status = one.getClassStatus();
                extracted();
                if (status == 1) return R.error("分类已经存在啦");
                else {
                    UpdateWrapper<ClassEntity> wrapper = new UpdateWrapper<>();
                    wrapper.eq("id", one.getId());
                    wrapper.set("class_status", 1);
                    return classService.update(wrapper) ? R.ok("添加成功") : R.error("添加失败");
                }
            } else {
                //说明查不到 直接加进去就可以了
                return classService.save(entity) ? R.ok("添加成功") : R.error("添加失败");
            }

        }
        return R.error("非法操作:请求体丢失");
    }


    @PostMapping("/cancelClassById")
    public R cancelClassById(@RequestHeader("system_api_Authorize_name") String name,
                             @RequestBody ClassEntity entity) {
        if (entity != null) {
            Long id = entity.getId();
            QueryWrapper<ClassEntity> wrapper = new QueryWrapper<>();
            wrapper.eq("id", id);
            ClassEntity one = classService.getOne(wrapper);
            assert one != null;
            if (name.equals("zsj")) {
                //超级管理 想干什么就干什么
                //逻辑删除这个id的分类和他id下的所有子分类
                extracted();
                return classService.zccg(id) ? R.ok() : R.error();
            } else if (one.getClassCreater().equals(name)) {
                //说明作者是他 进行删除
                extracted();
                return classService.zccg(id) ? R.ok() : R.error();
            }
        }
        return R.error("非法操作");
    }

    @PostMapping("/appendClass")
    public R appendClass(@RequestHeader("system_api_Authorize_name") String name,
                         @RequestBody ClassEntity entity) {
        //任何人都在任何子节点中追加任意名称的子节点 只要我们得到当前需要添加的根节点下没有同名的子节点 就可以添加
        if (entity!=null){
            Long fatherId = entity.getClassFatherId();//被追加的根节点id
            List<ClassEntity> child = classService.getChildById(fatherId);
            //还有一种情况是其它大分类中已经存在此分类
            List<ClassEntity> list = classService.list();
            for (ClassEntity classEntity : child) {
                if (classEntity.getClassName().equals(entity.getClassName())){
                    return R.error("当前节点已经存在必须唯一");
                }
            }
            for (ClassEntity classEntity : list) {
                if (classEntity.getClassName().equals(entity.getClassName())){
                    return R.error("其它节点已经存在必须唯一");
                }
            }
            //说明一切正常 初始化数据然后加到数据库中就行
            entity.setClassCreater(name);
            entity.setCreateTime(new Date(System.currentTimeMillis()));
            entity.setUpdateTime(new Date(System.currentTimeMillis()));
            entity.setClassStatus(1);
            boolean save = classService.save(entity);
            extracted();
            return save?R.ok("添加成功"):R.error("添加失败");
        }
        return R.error("非法操作");
    }

    @GetMapping("/getAllClass")
    public R getAllClass() {
        //获取所有分类并且以树形结果展示
        return R.ok().put("data", classService.listTree());
    }

    @GetMapping("/getAllClassForELE")
    public R getAllClassForELE() {
        //获取所有分类并且以树形结果展示
        ValueOperations<String, String> ops =
                stringRedisTemplate.opsForValue();
        String data = ops.get("getAllClassForELE");
        if (StringUtil.isNullOrEmpty(data)){
            List<ClassVoForTreeForElement> classVoForTreeForElements = getClassVoForTreeForElements(ops);
            return R.ok().put("data",classVoForTreeForElements);
        }
        return R.ok().put("data",GsonUtil.gson.fromJson(data, new TypeToken<List<ClassVoForTreeForElement>>(){}.getType()));
    }

    private List<ClassVoForTreeForElement> getClassVoForTreeForElements(ValueOperations<String, String> ops) {
        List<ClassVoForTreeForElement> classVoForTreeForElements = classService.listTree4ELE();
        ops.set("getAllClassForELE", GsonUtil.gson.toJson(classVoForTreeForElements),1, TimeUnit.HOURS);
        return classVoForTreeForElements;
    }
    private void extracted() {
        Set<String> keys = stringRedisTemplate.keys("getAllClassForELE*");
        stringRedisTemplate.delete(keys);//删除缓存中的分页文章信息 下次拿的时候更新缓存即可
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
