package com.zsj.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zsj.common.utils.PageUtils;
import com.zsj.system.entity.UserEntity;
import com.zsj.system.vo.UserVo;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author zsj
 * @email zsjemail666@163.com
 * @date 2023-10-05 20:07:09
 */
public interface UserService extends IService<UserEntity> {

    PageUtils queryPage(Map<String, Object> params);

    PageUtils getAllUserByCondition(Integer cur, Integer size, UserEntity user);
}

