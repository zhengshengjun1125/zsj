package com.zsj.common.utils;

/**
 * @author https://gitee.com/zhengshengjun
 * @date 2023/11/17.
 */
public class GlobalValueToExchange {
    //日志队列和交换机
    public static final String LOG_EXCHANGE = "ex.system.log";
    public static final String LOG_QUEUE = "system.logs";

    //邮件队列和交换机
    public static final String EMAIL_EXCHANGE = "ex.sms.email";
    public static final String EMAIL_QUEUE = "sms.email";

    //扣款队列和交换机
    public static final String DEDUCT_EXCHANGE = "ex.system.deduct";
    public static final String DEDUCT_QUEUE = "system.deduct";


}
