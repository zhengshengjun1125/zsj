package com.zsj.article.service.impl;

import com.zsj.article.vo.ClassVoForTree;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zsj.common.utils.PageUtils;
import com.zsj.common.utils.Query;

import com.zsj.article.dao.ClassDao;
import com.zsj.article.entity.ClassEntity;
import com.zsj.article.service.ClassService;


@Service("classService")
public class ClassServiceImpl extends ServiceImpl<ClassDao, ClassEntity> implements ClassService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ClassEntity> page = this.page(
                new Query<ClassEntity>().getPage(params),
                new QueryWrapper<ClassEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<ClassVoForTree> listTree() {

        //获取所有数据
        List<ClassEntity> all = baseMapper.selectList(new QueryWrapper<>());

        //找出根节点后 把所有符合条件的分类加入到当前分类的子分类中
        //这个方法不行 最多找到二级分类
        /*return god.stream()
                .map(classEntity -> {
                    List<ClassVoForTree> children = exclude.stream()
                            .filter(excludeEntity -> excludeEntity.getClassFatherId().equals(classEntity.getId()))
                            .map(excludeEntity -> new ClassVoForTree(excludeEntity, Collections.emptyList()))
                            .collect(Collectors.toList());
                    return new ClassVoForTree(classEntity, children);
                })
                .collect(Collectors.toList());*/

        return buildClassTree(all);
    }

    //构建分类树
    public List<ClassVoForTree> buildClassTree(List<ClassEntity> all) {
        List<ClassEntity> roots = all.stream()
                .filter(classEntity -> classEntity.getClassFatherId().equals(0L))
                .collect(Collectors.toList());

        return roots.stream()
                .map(root -> buildTree(root, all))
                .collect(Collectors.toList());

    }

    //递归寻找最深子节点
    private ClassVoForTree buildTree(ClassEntity parent, List<ClassEntity> all) {
        List<ClassEntity> children = all.stream()
                .filter(classEntity -> classEntity.getClassFatherId().equals(parent.getId()))
                .collect(Collectors.toList());

        List<ClassVoForTree> childTree = children.stream()
                .map(child -> buildTree(child, all))
                .collect(Collectors.toList());

        return new ClassVoForTree(parent, childTree);
    }

}