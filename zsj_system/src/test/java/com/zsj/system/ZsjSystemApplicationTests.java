package com.zsj.system;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zsj.common.utils.Encrypt;
import com.zsj.system.dao.UserDao;
import com.zsj.system.dao.UserTokenDao;
import com.zsj.system.entity.UserEntity;
import com.zsj.system.entity.UserTokenEntity;
import com.zsj.system.service.UserTokenService;
import com.zsj.system.vo.UserVo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Lazy;

import java.awt.*;
import java.util.Date;
import java.util.List;

@SpringBootTest
class ZsjSystemApplicationTests {


    @Autowired
    @Lazy
    private UserTokenDao userTokenDao;

    @Autowired
    @Lazy
    private UserDao userDao;

    @Test
    void contextLoads() {
        UserEntity entity = new UserEntity();
//        entity.setUsername("zsj");

//        entity.setRoleId(1L);
        List<UserEntity> all = userDao.getAllUserByCondition(entity);
        all.forEach(System.out::println);
    }

}
