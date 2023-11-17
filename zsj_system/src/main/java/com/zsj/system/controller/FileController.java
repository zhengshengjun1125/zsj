package com.zsj.system.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.gson.reflect.TypeToken;
import com.zsj.common.utils.GsonUtil;
import com.zsj.common.utils.ObjectUtil;
import com.zsj.common.utils.R;
import com.zsj.system.entity.FileEntity;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.*;
import com.zsj.system.service.FileService;



/**
 * @author zsj
 * @email zsjemail666@163.com
 * @date 2023-11-12 15:55:51
 */
@RestController
@RequestMapping("system/file")
public class FileController {
    @Autowired
    private FileService fileService;


    @Autowired
    @Lazy
    private StringRedisTemplate stringRedisTemplate;

    @PostMapping("/list/{cur}/{size}")
    public R list(@RequestHeader("system_api_Authorize_name") String name,
                  @PathVariable("cur") Integer cur,
                  @PathVariable("size") Integer size,
                  @NotNull @RequestBody FileEntity entity) {
        //带参的不缓存 直接查库
        if (!fileEntityParamIsEmptyQ(entity)) {
            Page<FileEntity> list = fileService.pageConditionUser(name, cur, size, entity);
            return R.ok().put("data", list);
        }
        //不带参 缓存一下
        ValueOperations<String, String> ops =
                stringRedisTemplate.opsForValue();
        String json = ops.get("fileList/" + name + cur + size);
        if (ObjectUtil.isNullOrEmpty(json)) {
            //缓存中没有
            Page<FileEntity> entityPage;
            if (name.equals("zsj")) {
                entityPage = fileService.page(new Page<>(cur, size));
            } else {
                entityPage = fileService.page(new Page<>(cur, size),
                        new QueryWrapper<FileEntity>().eq("Affiliation", name));
            }
            ops.set("fileList/" + name + cur + size, GsonUtil.gson.toJson(entityPage));
            return R.ok().put("data", entityPage);
        }
        //缓存命中
        return R.ok().put("data", GsonUtil.gson.fromJson(json, new TypeToken<Page<FileEntity>>() {
        }.getType()));
    }


    /**
     * 传参鉴定 看所有查询条件是否为一个空串
     * 或整个实体为null
     * @param entity 条件实体
     * @return 是 否
     */
    private static boolean fileEntityParamIsEmptyQ(FileEntity entity) {
        return !ObjectUtil.objectIsNotNull(entity)
                || (ObjectUtil.isNullOrEmpty(entity.getFileName())
                && ObjectUtil.isNullOrEmpty(entity.getType())
                && ObjectUtil.isNullOrEmpty(entity.getAffiliation())
                && ObjectUtil.isNullOrEmpty(entity.getFileSuffix()));
    }
}
