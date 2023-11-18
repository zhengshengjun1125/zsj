    package com.zsj.system.dao;

import com.zsj.system.entity.RoleEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zsj.system.vo.RoleParams;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

    /**
 * 
 * 
 * @author zsj
 * @email zsjemail666@163.com
 * @date 2023-10-09 13:02:12
 */
@Mapper
public interface RoleDao extends BaseMapper<RoleEntity> {
	List<RoleEntity> findByPage(RoleParams roleParams);
}
