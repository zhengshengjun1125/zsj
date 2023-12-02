package com.zsj.sms.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 * 
 * 
 * @author zsj
 * @email zsjemail666@163.com
 * @date 2023-12-02 14:52:48
 */
@Data
@TableName("sms_props")
public class PropsEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * uuid
	 */
	@TableId
	private String id;
	/**
	 * 邮件服务器
	 */
	private String host;
	/**
	 * 邮箱账号
	 */
	private String username;
	/**
	 * 邮箱认证码
	 */
	private String smtp;
	/**
	 * 所属用户
	 */
	private String belongUser;
	/**
	 * 创建时间
	 */
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;
	/**
	 * 修改时间
	 */
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date updateTime;
	/**
	 * 乐观锁
	 */
	private Long version;

}
