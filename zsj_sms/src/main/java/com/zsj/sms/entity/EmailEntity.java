package com.zsj.sms.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * 
 * @author zsj
 * @email zsjemail666@163.com
 * @date 2023-11-17 15:37:42
 */
@Data
@TableName("sms_email")
public class EmailEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 邮件id
	 */
	@TableId(type = IdType.ASSIGN_ID)
	private String id;
	/**
	 * 发送人
	 */
	private String sender;
	/**
	 * 接收人
	 */
	private String recipient;
	/**
	 * 是否为系统消息
	 */
	private Integer isSystem;
	/**
	 * 邮件内容
	 */
	private String content;
	/**
	 * 邮件标题
	 */
	private String title;
	/**
	 * 邮件创建时间
	 */
	private Date createTime;

}
