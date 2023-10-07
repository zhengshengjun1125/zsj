package com.zsj.article.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zsj.common.utils.PageUtils;
import com.zsj.article.entity.EntityEntity;

import java.util.Map;

/**
 * 
 *
 * @author zsj
 * @email zsjemail666@163.com
 * @date 2023-10-07 20:47:18
 */
public interface EntityService extends IService<EntityEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

