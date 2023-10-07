package com.zsj.article.entity;

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
 * @date 2023-10-07 20:47:17
 */
@Data
@TableName("art_class")
public class ClassEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 分类id
	 */
	@TableId
	private Long id;
	/**
	 * 分类名称
	 */
	private String className;
	/**
	 * 分类创建时间
	 */
	private Date createTime;
	/**
	 * 分类修改时间
	 */
	private Date updateTime;
	/**
	 * 分类状态
	 */
	private Integer classStatus;
	/**
	 * 分类父id 为0为根节点
	 */
	private Long classFatherId;

}
