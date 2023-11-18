package com.zsj.article.vo;

import com.zsj.article.entity.EntityEntity;
import lombok.Data;

/**
 * 
 * 
 * @author zsj
 * @email zsjemail666@163.com
 * @date 2023-10-07 20:47:18
 */
@Data
public class EntityVoForHomePage {

	public EntityVoForHomePage(EntityEntity entity) {
		this.artTitle = entity.getArtTitle();
		this.artRequestMonth = entity.getArtRequestMonth();
		this.artRequestDay = entity.getArtRequestDay();
		this.artRequestTotal = entity.getArtRequestTotal();
	}

	public EntityVoForHomePage(String artTitle, Long artRequestMonth, Long artRequestDay, Long artRequestTotal) {
		this.artTitle = artTitle;
		this.artRequestMonth = artRequestMonth;
		this.artRequestDay = artRequestDay;
		this.artRequestTotal = artRequestTotal;
	}

	public EntityVoForHomePage() {
	}

	/**
	 * 文章标题
	 */
	private String artTitle;
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


}
