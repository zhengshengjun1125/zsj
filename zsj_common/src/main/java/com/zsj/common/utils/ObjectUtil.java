package com.zsj.common.utils;


import org.apache.http.entity.FileEntity;

/**
 * @author https://gitee.com/zhengshengjun
 * @date 2023/10/31.
 */
public class ObjectUtil {

    public static boolean isNullOrEmpty(String s) {
        if (s == null) return true;
        else return s.equals("");
    }

    public static boolean objectIsNotNull(Object o) {
        return o != null;
    }

    public static boolean objectIsNull(Object o) {
        return o == null;
    }

    ;


}
