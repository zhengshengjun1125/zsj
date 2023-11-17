package com.zsj.sms.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zsj.common.utils.PageUtils;
import com.zsj.common.utils.Query;

import com.zsj.sms.dao.NoteDao;
import com.zsj.sms.entity.NoteEntity;
import com.zsj.sms.service.NoteService;


@Service("noteService")
public class NoteServiceImpl extends ServiceImpl<NoteDao, NoteEntity> implements NoteService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<NoteEntity> page = this.page(
                new Query<NoteEntity>().getPage(params),
                new QueryWrapper<NoteEntity>()
        );

        return new PageUtils(page);
    }

}