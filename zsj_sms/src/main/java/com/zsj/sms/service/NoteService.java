package com.zsj.sms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zsj.common.utils.PageUtils;
import com.zsj.sms.entity.NoteEntity;

import java.util.Map;

/**
 * 
 *
 * @author zsj
 * @email zsjemail666@163.com
 * @date 2023-11-17 15:37:42
 */
public interface NoteService extends IService<NoteEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

