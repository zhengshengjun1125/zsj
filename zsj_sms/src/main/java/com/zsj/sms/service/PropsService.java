package com.zsj.sms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zsj.common.utils.PageUtils;
import com.zsj.sms.entity.PropsEntity;

import java.util.Map;

/**
 * 
 *
 * @author zsj
 * @email zsjemail666@163.com
 * @date 2023-12-02 14:52:48
 */
public interface PropsService extends IService<PropsEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

