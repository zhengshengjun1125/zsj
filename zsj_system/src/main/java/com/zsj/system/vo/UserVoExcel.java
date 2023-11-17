package com.zsj.system.vo;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.zsj.system.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author https://gitee.com/zhengshengjun
 * @date 2023/10/7.
 */
@Data
@AllArgsConstructor
public class UserVoExcel {

    public UserVoExcel() {
    }


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
     * 角色名称
     */
    private String roleName;

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

    @ExcelProperty(value = {"用户创建时间"})
    @ColumnWidth(20)
    private String createTime;
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
