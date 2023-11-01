package com.zsj.system.dao;

import com.zsj.system.entity.BlogHomeHpEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zsj.system.vo.BlogHomeImgVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 
 * 
 * @author zsj
 * @email zsjemail666@163.com
 * @date 2023-10-16 10:48:12
 */
@Mapper
public interface BlogHomeHpDao extends BaseMapper<BlogHomeHpEntity> {


    @Select("SELECT hp.id, hp.photo, hp.belong_user_id, hp.create_time, u.username from blog_home_hp as hp JOIN sys_user as u on u.id = hp.belong_user_id")
    List<BlogHomeImgVo> getAllImgList();
}
