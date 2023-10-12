package com.zsj.system.vo;

import com.zsj.system.entity.RoleEntity;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * @author https://gitee.com/zhengshengjun
 * @date 2023/10/12.
 */
@Data
@Getter
@Setter
public class RoleByIndex {
    private Long id;
    private String roleName;

    public RoleByIndex(RoleEntity entity) {
        this.id = entity.getId();
        this.roleName = entity.getRoleName();
    }
}
