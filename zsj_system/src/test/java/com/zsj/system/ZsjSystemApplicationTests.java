package com.zsj.system;

import cn.hutool.extra.qrcode.QrCodeUtil;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zsj.common.utils.Encrypt;
import com.zsj.common.xss.HTMLFilter;
import com.zsj.system.dao.UserDao;
import com.zsj.system.dao.UserTokenDao;
import com.zsj.system.entity.UserEntity;
import com.zsj.system.entity.UserTokenEntity;
import com.zsj.system.service.UserTokenService;
import com.zsj.system.vo.UserVo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Lazy;

import java.awt.*;
import java.io.File;
import java.util.Date;
import java.util.List;

@SpringBootTest
@Slf4j
class ZsjSystemApplicationTests {



    @Autowired
    @Lazy
    private UserTokenDao userTokenDao;

    @Autowired
    @Lazy
    private UserDao userDao;


    @Autowired
    @Lazy
    RedissonClient redissonClient;


    @Test
    void contextLoads() {
        String md = Encrypt.encrypt_md5("123456");
        log.info("md5加密后的密码是{}", md);
        String s = Encrypt.encrypt_hash512("123456");
        String s2 = Encrypt.encrypt_hash512("123456");
        log.info("password {}", s);
        System.out.println(s.equals(s2));
    }


    @Test
    void contextLoads2() {
        String md = "<script type=\"module\" src=\"/src/main.js\"></script>";
        String s = HTMLFilter.htmlSpecialChars(md);
        HTMLFilter htmlFilter = new HTMLFilter();
        String filter = htmlFilter.filter(md);
        log.info("filter s {}", s);
        log.info("filter s2 {}", filter);
    }

    @Test
    void redissonTest() {
        RLock lock = redissonClient.getLock("test");
        lock.lock();
        try {
            log.info("加锁之后执行业务逻辑,业务Id" + Thread.currentThread().getId());
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            log.info("解锁操作");
            lock.unlock();
        }
    }

    @Test
    void token(){
        UserEntity userByToken = userTokenDao
                .getUserByToken("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6ImVmZDQ2Njk1MmNhMjRlYzk4N2NjMjRmZDc4NmU2YjU2IiwiYWNjb3VudCI6InpzaiJ9.T7-eC6-is-PZGdatePTIHL-JNCp-_PsdE7uZbYhu9nA");;
        log.info("user{}",userByToken);
    }
}
