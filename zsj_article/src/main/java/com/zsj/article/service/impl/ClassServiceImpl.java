package com.zsj.article.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
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

}