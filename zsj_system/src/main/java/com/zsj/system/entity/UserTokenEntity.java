package com.zsj.system.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 系统用户Token
 * 
 * @author zsj
 * @email zsjemail666@163.com
 * @date 2023-10-05 20:07:09
 */
@Data
@TableName("sys_user_token")
public class UserTokenEntity implements Serializable {
	private static final long serialVersionUID = 1L;


	public UserTokenEntity() {
	}


	public UserTokenEntity(Long userId, String token, Date expireTime, Date updateTime) {
		this.userId = userId;
		this.token = token;
		this.expireTime = expireTime;
		this.updateTime = updateTime;
	}

	/**
	 * 
	 */
	@TableId
	private Long userId;
	/**
	 * token
	 */
	private String token;
	/**
	 * 过期时间
	 */
	private Date expireTime;
	/**
	 * 更新时间
	 */
	private Date updateTime;

}
