package com.zsj.system.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 系统验证码
 * 
 * @author zsj
 * @email zsjemail666@163.com
 * @date 2023-10-05 20:07:09
 */
@Data
@TableName("sys_captcha")
public class CaptchaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	public CaptchaEntity(String uuid, String code) {
		this.uuid = uuid;
		this.code = code;
	}

	public CaptchaEntity(String uuid, String code, Date expireTime) {
		this.uuid = uuid;
		this.code = code;
		this.expireTime = expireTime;
	}


	public CaptchaEntity(String uuid, String code, Date expireTime, String base64) {
		this.uuid = uuid;
		this.code = code;
		this.expireTime = expireTime;
		this.base64 = base64;
	}

	/**
	 * uuid
	 */
	@TableId
	private String uuid;
	/**
	 * 验证码
	 */
	private String code;
	/**
	 * 过期时间
	 */
	private Date expireTime;


	private String base64;
}
