package com.zsj.sms.entity;

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
@TableName("sms_note")
public class NoteEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 短信id
	 */
	@TableId
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
	 * 短信内容
	 */
	private String content;
	/**
	 * 短信标题
	 */
	private String title;
	/**
	 * 创建时间
	 */
	private Date createTime;

}
