package com.zsj.system.entity;

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
 * @date 2023-10-16 10:48:12
 */
@Data
@TableName("blog_home_hp")
public class BlogHomeHpEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Long id;
	/**
	 * 博客首页图片
	 */
	private String photo;
	/**
	 * 归属用户id
	 */
	private Long belongUserId;
	/**
	 * 创建时间
	 */
	private Date createTime;

}
