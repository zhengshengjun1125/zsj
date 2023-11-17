package com.zsj.sms.controller;

import com.zsj.sms.util.EmailContentUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author https://gitee.com/zhengshengjun
 * @date 2023/11/17.
 */
@RestController
public class TestController {

    @GetMapping("/html")
    public String html() {
        return EmailContentUtil.createRegisterUserEmailContent("biggen");
    }
}
