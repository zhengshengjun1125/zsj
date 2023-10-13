package com.zsj.system.dao;

import com.zsj.system.entity.UserEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zsj.system.vo.UserVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 
 * 
 * @author zsj
 * @email zsjemail666@163.com
 * @date 2023-10-05 20:07:09
 */
@Mapper
public interface UserDao extends BaseMapper<UserEntity> {

    List<UserEntity> getAllUserByCondition(@Param("user") UserEntity user);

    List<UserEntity> getUpgradeExistInfo(@Param("user")UserVo user);

}
