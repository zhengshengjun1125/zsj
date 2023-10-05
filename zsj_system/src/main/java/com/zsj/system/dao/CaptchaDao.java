package com.zsj.system.dao;

import com.zsj.system.entity.CaptchaEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 系统验证码
 * 
 * @author zsj
 * @email zsjemail666@163.com
 * @date 2023-10-05 20:07:09
 */
@Mapper
public interface CaptchaDao extends BaseMapper<CaptchaEntity> {
	
}
