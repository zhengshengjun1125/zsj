package com.zsj.common.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author <a href="https://gitee.com/zhengshengjun">zsj</a>
 * @date 2023/12/2.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private String username;
    private String email;

    public User(String username) {
        this.username = username;
    }
}
