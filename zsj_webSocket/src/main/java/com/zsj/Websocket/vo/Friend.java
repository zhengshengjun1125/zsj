package com.zsj.Websocket.vo;

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

}
