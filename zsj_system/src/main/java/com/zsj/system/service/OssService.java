package com.zsj.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zsj.common.utils.PageUtils;
import com.zsj.system.entity.OssEntity;

import java.util.Map;

/**
 * 文件上传
 *
 * @author zsj
 * @email zsjemail666@163.com
 * @date 2023-10-05 20:07:09
 */
public interface OssService extends IService<OssEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

