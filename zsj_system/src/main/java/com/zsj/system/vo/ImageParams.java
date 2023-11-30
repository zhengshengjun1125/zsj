package com.zsj.system.vo;

import lombok.Data;

import java.awt.*;
import java.io.Serializable;

/**
 * @author https://gitee.com/zhengshengjun
 * @date 2023/11/30.
 */
@Data
public class ImageParams implements Serializable {

    //水印位置
    private String location;
    //水印文字
    private String text;
    //水印颜色
    private SelfColor color;
}
