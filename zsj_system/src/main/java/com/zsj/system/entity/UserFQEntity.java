package com.zsj.system.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * 好友申请队列数据表
 * @author zsj
 * @email zsjemail666@163.com
 * @date 2023-11-08 20:18:48
 */
@Data
@TableName("sys_user_f_q")
public class UserFQEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@TableId
	private Long id;
	/**
	 * 请求用户id
	 */
	private Long demandUserId;
	/**
	 * 被请求用户id
	 */
	private Long toUserId;
	/**
	 * 发起时间
	 */
	private Date demandTime;
	/**
	 * 申请状态
	 */
	private Enum demandStatus;

}
