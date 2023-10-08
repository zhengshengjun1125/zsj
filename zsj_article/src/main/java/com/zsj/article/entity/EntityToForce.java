package com.zsj.article.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * 真正返回给前端的实体
 * 
 * @author zsj
 * @email zsjemail666@163.com
 * @date 2023-10-07 20:47:18
 */
@Data
@ToString
public class EntityToForce implements Serializable {
	private static final long serialVersionUID = 1L;

	public EntityToForce(EntityEntity entity,String artClassName) {
		this.id = entity.getId();
		this.artTitle = entity.getArtTitle();;
		this.artClassId = entity.getArtClassId();;
		this.artClassName = artClassName;
		this.artContent = entity.getArtContent();;
		this.artRequestMonth = entity.getArtRequestMonth();;
		this.artRequestDay = entity.getArtRequestDay();;
		this.artRequestTotal = entity.getArtRequestTotal();;
		this.artAuther = entity.getArtAuther();;
		this.createTime = entity.getCreateTime();;
		this.updateTime = entity.getUpdateTime();;
	}

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
	 * 文章所属分类id
	 */
	private String artClassName;

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
	private Date createTime;
	/**
	 * 文章修改时间
	 */
	private Date updateTime;

}
