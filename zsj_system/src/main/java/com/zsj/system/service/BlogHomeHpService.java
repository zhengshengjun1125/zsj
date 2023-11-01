package com.zsj.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zsj.common.utils.PageUtils;
import com.zsj.system.entity.BlogHomeHpEntity;
import com.zsj.system.vo.BlogHomeImgVo;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author zsj
 * @email zsjemail666@163.com
 * @date 2023-10-16 10:48:12
 */
public interface BlogHomeHpService extends IService<BlogHomeHpEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<BlogHomeImgVo> getByUserID(String name);

    List<BlogHomeImgVo> listALL();
}

