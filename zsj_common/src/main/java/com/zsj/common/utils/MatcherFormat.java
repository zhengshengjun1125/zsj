package com.zsj.common.utils;

/**
 * @author https://gitee.com/zhengshengjun
 * @date 2023/10/13.
 */
public class MatcherFormat {

    //手机正则
    public static final String mobile_matcher_dif = "/^1(3[0-9]|4[01456879]|5[0-35-9]|6[2567]|7[0-8]|8[0-9]|9[0-35-9])\\d{8}$/";

    public static final String mobile_matcher = "^1[3-9]\\d{9}$";

    //邮箱正则
    public static final String email_matcher = "^[a-zA-Z0-9_.-]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*\\.[a-zA-Z0-9]{2,6}$";
}
