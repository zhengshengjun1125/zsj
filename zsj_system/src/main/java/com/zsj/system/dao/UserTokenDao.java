package com.zsj.system.dao;

import com.zsj.system.entity.UserEntity;
import com.zsj.system.entity.UserTokenEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 系统用户Token
 * 
 * @author zsj
 * @email zsjemail666@163.com
 * @date 2023-10-05 20:07:09
 */
@Mapper
public interface UserTokenDao extends BaseMapper<UserTokenEntity> {

    int saveToken(@Param("entity")UserTokenEntity entity);

    int updateToken(@Param("entity") UserTokenEntity entity);


    UserEntity getUserByToken(@Param("token")String token);
}
