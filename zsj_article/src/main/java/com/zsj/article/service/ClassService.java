package com.zsj.article.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zsj.common.utils.PageUtils;
import com.zsj.article.entity.ClassEntity;

import java.util.Map;

/**
 * 
 *
 * @author zsj
 * @email zsjemail666@163.com
 * @date 2023-10-07 20:47:17
 */
public interface ClassService extends IService<ClassEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

