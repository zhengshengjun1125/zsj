package com.zsj.system.dao;

import com.zsj.system.entity.LogEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 
 * 
 * @author zsj
 * @email zsjemail666@163.com
 * @date 2023-10-05 20:07:09
 */
@Mapper
public interface LogDao extends BaseMapper<LogEntity> {

    List<LogEntity> getTen();
}
