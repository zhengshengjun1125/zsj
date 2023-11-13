package com.zsj.system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zsj.common.utils.PageUtils;
import com.zsj.system.entity.FileEntity;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author zsj
 * @email zsjemail666@163.com
 * @date 2023-11-12 15:55:51
 */
public interface FileService extends IService<FileEntity> {

    PageUtils queryPage(Map<String, Object> params);

    Page<FileEntity> pageConditionUser(String name, Integer cur, Integer size, FileEntity entity);
}

