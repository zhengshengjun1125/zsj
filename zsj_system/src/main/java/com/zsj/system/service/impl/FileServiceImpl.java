package com.zsj.system.service.impl;

import org.springframework.stereotype.Service;
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

}