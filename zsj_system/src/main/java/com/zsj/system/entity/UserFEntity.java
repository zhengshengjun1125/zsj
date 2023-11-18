package com.zsj.system.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * 好友表
 * @author zsj
 * @email zsjemail666@163.com
 * @date 2023-11-08 20:18:47
 */
@Data
@TableName("sys_user_f")
public class UserFEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Long id;
	/**
	 * 用户id
	 */
	private Long userId;
	/**
	 * 用户好友id
	 */
	private Long userFId;
	/**
	 * 用户好友状态
	 */
	private Integer userFStatus;
	/**
	 * 加好友时间
	 */
	private Date relationTime;

}
