package com.zsj.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zsj.common.utils.PageUtils;
import com.zsj.system.entity.UserFQEntity;

import java.util.Map;

/**
 * 
 *
 * @author zsj
 * @email zsjemail666@163.com
 * @date 2023-11-08 20:18:48
 */
public interface UserFQService extends IService<UserFQEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

