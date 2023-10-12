package com.zsj.system.entity;

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
 * @date 2023-10-09 13:02:12
 */
@Data
@TableName("sys_role")
public class RoleEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 *  
	 */
	@TableId
	private Long id;
	/**
	 * 角色名称
	 */
	private String roleName;
	/**
	 * 角色状态
	 */
	private Integer status;
	/**
	 * 创建时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;
	/**
	 * 修改时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date updateTime;

}
