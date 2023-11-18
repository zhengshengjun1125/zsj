package com.zsj.system.vo;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author https://gitee.com/zhengshengjun
 * @date 2023/10/30.
 */
@Data
public class AssginMenuDto {
    private Long roleId;							// 角色id
    private List<Map<String , Number>> menuIdList;	// 选中的菜单id的集合 , Map中包含了2部分的数据：菜单id，isHalf
}