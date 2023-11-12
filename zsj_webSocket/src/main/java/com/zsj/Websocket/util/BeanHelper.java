package com.zsj.Websocket.util;

import org.springframework.context.ApplicationContext;

/**
 * @author https://gitee.com/zhengshengjun
 * @date 2023/11/12.
 */
public class BeanHelper {
    public static ApplicationContext ac;

    public static <T> T getBean(Class<T> clazz) {
        return (T) ac.getBean(clazz);
    }

    public static void setApplicationContext(ApplicationContext applicationContext) {
        ac = applicationContext;
    }

}
