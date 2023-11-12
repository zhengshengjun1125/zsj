package com.zsj.Websocket.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

/**
 * @author https://gitee.com/zhengshengjun
 * @date 2023/10/7.
 */
@Data
public class UserVo {

    public UserVo() {
    }

    /**
     * id
     */
    @TableId
    private Long id;
    /**
     * 账号
     */
    private String username;
    /**
     * 角色名称
     */
    private String roleName;
    /**
     * 头像
     */
    private String avatar;

    /**
     * 邮箱
     */
    private String email;
    /**
     * 电话
     */
    private String mobile;

    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 创建者id
     */
    private Long creatUserId;
    /**
     * 登陆状态
     */
    private String loginStatus;
}
