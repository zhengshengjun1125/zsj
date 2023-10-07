package com.zsj.system;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zsj.common.utils.Encrypt;
import com.zsj.system.dao.UserTokenDao;
import com.zsj.system.entity.UserTokenEntity;
import com.zsj.system.service.UserTokenService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Lazy;

import java.awt.*;
import java.util.Date;

@SpringBootTest
class ZsjSystemApplicationTests {


    @Autowired
    @Lazy
    private UserTokenDao userTokenDao;


    @Test
    void contextLoads() {
        int i = userTokenDao.saveToken(new UserTokenEntity(1L, "222", new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis())));

        System.out.println(i);
    }

}
