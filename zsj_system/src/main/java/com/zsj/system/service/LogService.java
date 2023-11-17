package com.zsj.system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zsj.common.utils.PageUtils;
import com.zsj.system.entity.LogEntity;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author zsj
 * @email zsjemail666@163.com
 * @date 2023-10-05 20:07:09
 */
public interface LogService extends IService<LogEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<LogEntity> getTen();

    Page<LogEntity> pageByCondition(int cur, int size, LogEntity entity);
}

