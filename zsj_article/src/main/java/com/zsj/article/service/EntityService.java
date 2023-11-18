package com.zsj.article.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zsj.article.Page.ArticlePage;
import com.zsj.article.entity.EntityToForce;
import com.zsj.common.utils.PageUtils;
import com.zsj.article.entity.EntityEntity;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author zsj
 * @email zsjemail666@163.com
 * @date 2023-10-07 20:47:18
 */
public interface EntityService extends IService<EntityEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<EntityToForce> getAllArticle();

    ArticlePage getAllArticle(Page<EntityEntity> cur);

    ArticlePage getAllArticleByUserName(Page<EntityEntity> page, EntityEntity entity, String name);

    boolean resetContentById(Long id, String artContent, String artTitle);
}

