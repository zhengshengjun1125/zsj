package com.zsj.system.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.zsj.system.entity.UserEntity;
import lombok.Data;

import java.util.Date;

/**
 * @author https://gitee.com/zhengshengjun
 * @date 2023/10/7.
 */
@Data
public class UserVo {

    public UserVo(UserEntity user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.avatar = user.getAvatar();
        this.mobile = user.getMobile();
        this.createTime = user.getCreateTime();
        this.creatUserId = user.getCreatUserId();
        this.loginStatus = user.getLoginStatus();
    }


    public UserVo(UserEntity user,String roleName) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.roleName = roleName;
        this.avatar = user.getAvatar();
        this.mobile = user.getMobile();
        this.createTime = user.getCreateTime();
        this.creatUserId = user.getCreatUserId();
        this.loginStatus = user.getLoginStatus();
    }

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
