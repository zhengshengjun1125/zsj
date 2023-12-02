package com.zsj.sms.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zsj.common.utils.PageUtils;
import com.zsj.sms.entity.EmailEntity;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author zsj
 * @email zsjemail666@163.com
 * @date 2023-11-17 15:37:42
 */
public interface EmailService extends IService<EmailEntity> {

    PageUtils queryPage(Map<String, Object> params);



    IPage<EmailEntity> pageByCondition(int cur, int size, EmailEntity entity);
}

