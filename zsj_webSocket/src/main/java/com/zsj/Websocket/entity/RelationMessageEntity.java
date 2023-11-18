package com.zsj.Websocket.entity;

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
 * @date 2023-11-08 19:03:54
 */
@Data
@TableName("relation_message")
public class RelationMessageEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 信息id
	 */
	@TableId(type = IdType.ASSIGN_ID)
	private String id;
	/**
	 * 发送人
	 */
	private String fromuser;
	/**
	 * 接收人
	 */
	private String touser;
	/**
	 * 消息内容
	 */
	private String message;
	/**
	 * 接收状态
	 */
	private String alreadRead; // yes or no
	/**
	 * 发起时间
	 */
	private Date createTime;

	public RelationMessageEntity() {
	}

	public RelationMessageEntity(String fromuser, String touser, String message, String alreadRead, Date createTime) {
		this.fromuser = fromuser;
		this.touser = touser;
		this.message = message;
		this.alreadRead = alreadRead;
		this.createTime = createTime;
	}
}
