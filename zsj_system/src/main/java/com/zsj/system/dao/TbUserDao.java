package com.zsj.system.dao;

import com.zsj.system.entity.TbUserEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户
 * 
 * @author zsj
 * @email zsjemail666@163.com
 * @date 2023-10-05 20:07:09
 */
@Mapper
public interface TbUserDao extends BaseMapper<TbUserEntity> {
	
}
