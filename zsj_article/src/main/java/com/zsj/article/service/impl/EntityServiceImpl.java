package com.zsj.article.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zsj.article.Page.ArticlePage;
import com.zsj.article.dao.ClassDao;
import com.zsj.article.entity.ClassEntity;
import com.zsj.article.entity.EntityToForce;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zsj.common.utils.PageUtils;
import com.zsj.common.utils.Query;

import com.zsj.article.dao.EntityDao;
import com.zsj.article.entity.EntityEntity;
import com.zsj.article.service.EntityService;


@Service("entityService")
public class EntityServiceImpl extends ServiceImpl<EntityDao, EntityEntity> implements EntityService {

    @Autowired
    ClassDao classDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<EntityEntity> page = this.page(
                new Query<EntityEntity>().getPage(params),
                new QueryWrapper<EntityEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<EntityToForce> getAllArticle() {
        List<EntityEntity> allArt = baseMapper.getAllArt();
        List<ClassEntity> allClass = classDao.selectList(new QueryWrapper<>());
        Map<Long, String> collect = allClass.stream()
                .collect(Collectors.toMap(ClassEntity::getId, ClassEntity::getClassName));
        return allArt.stream().map(entity -> new EntityToForce(entity, collect.get(entity.getArtClassId())))
                .collect(Collectors.toList());
    }

    @Override
    public ArticlePage getAllArticle(Page<EntityEntity> cur) {
        List<ClassEntity> allClass = classDao.selectList(new QueryWrapper<>());
        Map<Long, String> collect = allClass.stream()
                .collect(Collectors.toMap(ClassEntity::getId, ClassEntity::getClassName));
        IPage<EntityEntity> allArticleSimple = getAllArticleSimple(cur);

        return new ArticlePage(allArticleSimple,collect);
    }


    public IPage<EntityEntity> getAllArticleSimple(Page<EntityEntity> cur) {
        return baseMapper.selectPage(cur, new QueryWrapper<>());
    }
}