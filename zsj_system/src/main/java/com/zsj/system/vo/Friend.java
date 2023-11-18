package com.zsj.system.vo;

import com.zsj.system.entity.UserEntity;
import lombok.*;

/**
 * @author https://gitee.com/zhengshengjun
 * @date 2023/11/8.
 */
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Friend {
    private Long id;
    /**
     * 账号
     */
    private String username;
    /**
     * 头像
     */
    private String avatar;

    public Friend(UserEntity entity) {
        this.username = entity.getUsername();
        this.avatar = entity.getAvatar();
        this.id = entity.getId();
    }
}
