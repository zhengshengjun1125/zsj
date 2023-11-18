package com.zsj.system.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zsj.common.utils.ObjectUtil;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zsj.common.utils.PageUtils;
import com.zsj.common.utils.Query;

import com.zsj.system.dao.FileDao;
import com.zsj.system.entity.FileEntity;
import com.zsj.system.service.FileService;


@Service("fileService")
public class FileServiceImpl extends ServiceImpl<FileDao, FileEntity> implements FileService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<FileEntity> page = this.page(
                new Query<FileEntity>().getPage(params),
                new QueryWrapper<FileEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public Page<FileEntity> pageConditionUser(String name, Integer cur, Integer size, FileEntity entity) {
        Page<FileEntity> page = new Page<>(cur, size);
        QueryWrapper<FileEntity> baseQuery = new QueryWrapper<>();
        if (name.equals("zsj")) {
            //如果是超管 可以加上所有者的参数 如果此参数是个空串或者null 那就不需要进行设定 直接查所有人的
            if (!ObjectUtil.isNullOrEmpty(entity.getAffiliation())) {
                baseQuery.eq("Affiliation", entity.getAffiliation());
            }
        } else {
            //否则他只能查询他自己的数据
            baseQuery.eq("Affiliation", name);
        }
        //文件名模糊查询
        if (!ObjectUtil.isNullOrEmpty(entity.getFileName())) baseQuery.like("file_name", entity.getFileName());
        //指定类型查询
        if (!ObjectUtil.isNullOrEmpty(entity.getType())) baseQuery.eq("type", entity.getType());
        //后缀模拟查询
        if (!ObjectUtil.isNullOrEmpty(entity.getFileSuffix())) baseQuery.like("file_suffix", entity.getFileSuffix());
        return baseMapper.selectPage(page, baseQuery);
    }

}