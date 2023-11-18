package com.zsj.system.vo;

import lombok.Data;

import java.util.List;

/**
 * @author https://gitee.com/zhengshengjun
 * @date 2023/10/30.
 */
@Data
public class SysMenuVo {

    private String title;
    private String name;
    private List<SysMenuVo> children;

}
