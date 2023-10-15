package com.zsj.article.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.zsj.article.vo.ClassVoForTree;
import com.zsj.article.vo.ClassVoForTreeForElement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
@Slf4j
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

    @Override
    public List<ClassVoForTreeForElement> listTree4ELE() {
        // 获取所有可用分类的数据
        List<ClassEntity> all = baseMapper.selectList(new QueryWrapper<ClassEntity>().eq("class_status", 1));
        return buildClassTree4ELE(all);
    }

    //斩草除根
    @Override
    public boolean zccg(Long id) {
        log.warn("警告！您正在进行危险操作,正在删除id为{}的分类数据,改节点数据以及该节点的所有根节点将会被逻辑删除", id);
        List<ClassEntity> all = baseMapper.selectList(new QueryWrapper<ClassEntity>().eq("class_status", 1));
        UpdateWrapper<ClassEntity> wrapper = new UpdateWrapper<ClassEntity>().eq("id", id).set("class_status", 0);
        update(wrapper);//先干掉目标对象
        /*
            先查出所有数据 然后找出父节点为这个节点的的所有根节点 而且需要递归 继续往下寻找直到没有根节点
         */
        //再干掉他的九族
        return clean_tree(all, id);
    }

    /**
     * 此方法用于查询当前id的子节点有哪些 将他们返回
     */
    @Override
    public List<ClassEntity> getChildById(Long id) {
        // 获取所有可用分类的数据
        List<ClassEntity> all
                = baseMapper.selectList(new QueryWrapper<ClassEntity>().eq("class_status", 1));
        List<ClassEntity> childList = new ArrayList<>();
        for (ClassEntity classEntity : all) {
            if (classEntity.getClassFatherId().equals(id)) {
                // 当前节点是id的子节点
                childList.add(classEntity);
                // 递归寻找当前节点的子节点
                List<ClassEntity> grandchildren = getChildById(classEntity.getId());
                childList.addAll(grandchildren);
            }
        }
        return childList;
    }



    /**
     * @param all 所有数据
     * @param id  当前父节点
     */
    private boolean clean_tree(List<ClassEntity> all, Long id) {
        List<Boolean> result = new ArrayList<>();
        //需要先查到父节点为这个id的所有节点
        //先找到所有根节点
        List<ClassEntity> family = all.stream()
                .filter(classEntity -> classEntity.getClassFatherId().equals(id))
                .collect(Collectors.toList());
        for (ClassEntity classEntity : family) {
            result.add(clean(classEntity, all));
        }
        for (Boolean f : result) {
            if (!f) return false;
        }
        return true;
    }

    private boolean clean(ClassEntity parent, List<ClassEntity> all) {
        List<Boolean> result = new ArrayList<>();
        //先删除当前的parent
        UpdateWrapper<ClassEntity> first_do = new UpdateWrapper<ClassEntity>().
                eq("id", parent.getId()).set("class_status", 0);
        //再递归往下寻找继续删除
        result.add(update(first_do));
        List<ClassEntity> children = all.stream()
                .filter(classEntity -> classEntity.getClassFatherId().equals(parent.getId()))
                .collect(Collectors.toList());
        //这些对象都需要删掉
        for (ClassEntity child : children) {
            //删掉所有儿子
            UpdateWrapper<ClassEntity> wrapper = new UpdateWrapper<ClassEntity>().
                    eq("id", child.getId()).set("class_status", 0);
            //再递归往下寻找继续删除
            clean(child, all);
            result.add(update(wrapper));
        }
        for (Boolean f : result) {
            if (!f) return false;
        }
        return true;
    }


    public List<ClassVoForTreeForElement> buildClassTree4ELE(List<ClassEntity> all) {
        //先找到所有根节点
        List<ClassEntity> roots = all.stream()
                .filter(classEntity -> classEntity.getClassFatherId().equals(0L))
                .collect(Collectors.toList());

        return roots.stream()
                .map(root -> buildTree4ELE(root, all))
                .collect(Collectors.toList());
    }


    //递归寻找最深子节点
    private ClassVoForTreeForElement buildTree4ELE(ClassEntity parent, List<ClassEntity> all) {
        //从所有数据中晒出当前父id为当前parent的id的数据
        List<ClassEntity> children = all.stream()
                .filter(classEntity -> classEntity.getClassFatherId().equals(parent.getId()))
                .collect(Collectors.toList());

        List<ClassVoForTreeForElement> childTree = children.stream()
                .map(child -> buildTree4ELE(child, all))
                .collect(Collectors.toList());
        return new ClassVoForTreeForElement(parent.getId().toString(), parent.getClassName(), parent.getClassCreater(), childTree);
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