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
        log.info("filter s {}",s);
        log.info("filter s2 {}",filter);

    }
}
