package com.zsj.Websocket.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zsj.common.utils.PageUtils;
import com.zsj.Websocket.entity.RelationMessageEntity;

import java.util.Map;

/**
 * 
 *
 * @author zsj
 * @email zsjemail666@163.com
 * @date 2023-11-08 19:03:54
 */
public interface RelationMessageService extends IService<RelationMessageEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

