package com.zsj.system.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * @author zsj
 * @email zsjemail666@163.com
 * @date 2023-10-05 20:07:09
 */
@Data
@TableName("sys_user")
@Getter
@Setter
public class UserEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * id
     */
    @ExcelProperty(value = {"用户id"})
    @ColumnWidth(15)
    private Long id;
    /**
     * 账号
     */
    @ExcelProperty(value = {"用户账号"})
    @ColumnWidth(20)
    private String username;

    /**
     * 密码
     */
    @ExcelIgnore
    private String password;
    /**
     * 头像
     */
    @ExcelProperty(value = {"用户头像地址"})
    @ColumnWidth(30)
    private String avatar;
    /**
     * 邮箱
     */
    @ExcelProperty(value = {"用户邮箱"})
    @ColumnWidth(20)
    private String email;
    /**
     * 角色
     */
    @ExcelProperty(value = {"用户角色id"})
    @ColumnWidth(20)
    private Long roleId;
    /**
     * 电话
     */
    @ExcelProperty(value = {"用户手机号"})
    @ColumnWidth(20)
    private String mobile;
    /**
     * 状态：0禁用 1正常
     */
    @ExcelIgnore
    private Integer status;
    /**
     * 创建时间
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ExcelProperty(value = {"用户创建时间"})
    @ColumnWidth(20)
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    /**
     * 创建者id
     */
    @ExcelIgnore
    private Long creatUserId;
    /**
     * 登录状态
     */
    @ExcelIgnore
    private String loginStatus;
}
