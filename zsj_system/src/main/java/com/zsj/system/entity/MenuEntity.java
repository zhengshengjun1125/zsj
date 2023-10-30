package com.zsj.system.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import lombok.Data;

/**
 * 菜单表
 * 
 * @author zsj
 * @email zsjemail666@163.com
 * @date 2023-10-30 17:19:44
 */
@Data
@TableName("sys_menu")
public class MenuEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 编号
	 */
	@TableId
	private Long id;
	/**
	 * 所属上级
	 */
	private Long parentId;
	/**
	 * 菜单标题
	 */
	private String title;
	/**
	 * 组件名称
	 */
	private String component;
	/**
	 * 排序
	 */
	private Integer sortValue;
	/**
	 * 状态(0:禁止,1:正常)
	 */
	private Integer status;
	/**
	 * 是否存在在子节点
	 */
	private Integer hasChildren;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 更新时间
	 */
	private Date updateTime;
	/**
	 * 删除标记（0:不可用 1:可用）
	 */
	private Integer isDeleted;

	// 下级列表
	private List<MenuEntity> children;

}
