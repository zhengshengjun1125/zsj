package com.zsj.article.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
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

        return new ArticlePage(allArticleSimple, collect);
    }

    @Override
    public ArticlePage getAllArticleByUserName(Page<EntityEntity> page, EntityEntity entity, String name) {
        //封装基础查询条件
        QueryWrapper<EntityEntity> queryWrapper = new QueryWrapper<>();
        if (entity==null)entity = new EntityEntity();
        if (entity.getArtTitle() != null && !entity.getArtTitle().equals("")) {

            queryWrapper.like("art_title", entity.getArtTitle());//模糊查询标题
        }
        if (entity.getArtRequestMonth() != null) {
            queryWrapper.ge("art_request_month", entity.getArtRequestMonth());//月访问
        }
        if (entity.getArtRequestDay() != null) {
            queryWrapper.ge("art_request_day", entity.getArtRequestDay());
        }
        if (entity.getArtRequestTotal() != null) {
            queryWrapper.ge("art_request_total", entity.getArtRequestTotal());
        }
        if (entity.getArtClassId() != null) {
            queryWrapper.eq("art_class_id", entity.getArtClassId());
        }
        queryWrapper.eq("art_status",1);
        if (name.equals("zsj")) {
            //直接开始查询并且分页
            //zsj 可以查询 所有人的文章 但是其它的用户只能查询自己的
            //zsj 可以查某一个作者的
            if (entity.getArtAuther() != null&& !entity.getArtAuther().equals("")) {
                queryWrapper.eq("art_auther", entity.getArtAuther());
            }
            IPage<EntityEntity> no_do_list = baseMapper.selectPage(page, queryWrapper);//zsj查询出来的
            //将其改成我们需要的文章放回前台的信息
            return doChange(no_do_list);
        } else {
            //其它用户这里 不管最后怎么设置 都必须+一个条件就是作者是他自己的eq条件
            queryWrapper.eq("art_auther", name);
            IPage<EntityEntity> no_do_list = baseMapper.selectPage(page, queryWrapper);//其它人查询的自己的
            return doChange(no_do_list);
        }
    }

    @Override
    public boolean resetContentById(Long id, String artContent, String artTitle) {
        UpdateWrapper<EntityEntity> entityEntityUpdateWrapper = new UpdateWrapper<>();
        entityEntityUpdateWrapper.set("art_content",artContent);
        entityEntityUpdateWrapper.set("art_title",artTitle);
        entityEntityUpdateWrapper.eq("id",id);
        return update(entityEntityUpdateWrapper);
    }


    public ArticlePage doChange(IPage<EntityEntity> no_do_list){
        List<ClassEntity> allClass = classDao.selectList(new QueryWrapper<>());
        Map<Long, String> collect = allClass.stream()
                .collect(Collectors.toMap(ClassEntity::getId, ClassEntity::getClassName));
        return new ArticlePage(no_do_list, collect);
    }

    public IPage<EntityEntity> getAllArticleSimple(Page<EntityEntity> cur) {
        return baseMapper.selectPage(cur, new QueryWrapper<>());
    }
}