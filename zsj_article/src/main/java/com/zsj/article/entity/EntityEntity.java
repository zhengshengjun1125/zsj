package com.zsj.article.entity;

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
 * @date 2023-10-07 20:47:18
 */
@Data
@TableName("art_entity")
public class EntityEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 文章id
	 */
	@TableId
	private Long id;
	/**
	 * 文章标题
	 */
	private String artTitle;
	/**
	 * 文章所属分类id
	 */
	private Long artClassId;
	/**
	 * 文章内容 需要是一个html格式或者md格式
	 */
	private String artContent;
	/**
	 * 文章月访问量
	 */
	private Long artRequestMonth;
	/**
	 * 文章日访问量
	 */
	private Long artRequestDay;
	/**
	 * 文章总访问量
	 */
	private Long artRequestTotal;
	/**
	 * 文章作者
	 */
	private String artAuther;
	/**
	 * 文章发布时间
	 */
	@JsonFormat(shape=JsonFormat.Shape.STRING,pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	private Date createTime;
	/**
	 * 文章修改时间
	 */
	@JsonFormat(shape=JsonFormat.Shape.STRING,pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	private Date updateTime;
	/**
	 * 0表示删除 1表示正常
	 */
	private Integer artStatus;

}
