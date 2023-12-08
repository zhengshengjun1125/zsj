package com.zsj.system.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zsj
 * @email zsjemail666@163.com
 * @date 2023-10-05 20:07:09
 */
@Data
@TableName("sys_log")
@NoArgsConstructor
public class LogEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    @ExcelProperty(value = {"日志id"})
    @ColumnWidth(15)
    private Long id;
    /**
     * 用户名
     */
    @ExcelProperty(value = {"操作用户"})
    @ColumnWidth(15)
    private String username;
    /**
     * 操作名称
     */
    @ExcelProperty(value = {"操作接口路径"})
    @ColumnWidth(15)
    private String operation;
    /**
     * 请求方法
     */
    @ExcelProperty(value = {"请求方法"})
    @ColumnWidth(15)
    private String method;
    /**
     * 请求参数
     */
    @ExcelProperty(value = {"请求的参数"})
    @ColumnWidth(15)
    private String params;
    /**
     * IP地址
     */
    @ExcelProperty(value = {"请求的IP地址"})
    @ColumnWidth(15)
    private String ip;

    /**
     * 创建时间
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ExcelProperty(value = {"创建时间"})
    @ColumnWidth(20)
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    private Date createDate;


    public LogEntity(com.zsj.common.vo.LogEntity entity) {
        this.id = entity.getId();
        this.username = entity.getUsername();
        this.operation = entity.getOperation();
        this.method = entity.getMethod();
        this.params = entity.getParams();
        this.ip = entity.getIp();
        this.createDate = entity.getCreateDate();
    }


}
