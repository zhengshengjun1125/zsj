package com.zsj.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zsj.common.utils.PageUtils;
import com.zsj.system.entity.UserFEntity;
import com.zsj.system.vo.Friend;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author zsj
 * @email zsjemail666@163.com
 * @date 2023-11-08 20:18:47
 */
public interface UserFService extends IService<UserFEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<Friend> getCurUserFVo(List<UserFEntity> list);
}

