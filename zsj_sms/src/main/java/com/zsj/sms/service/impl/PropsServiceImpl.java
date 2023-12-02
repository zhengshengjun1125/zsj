package com.zsj.sms.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zsj.common.utils.PageUtils;
import com.zsj.common.utils.Query;

import com.zsj.sms.dao.PropsDao;
import com.zsj.sms.entity.PropsEntity;
import com.zsj.sms.service.PropsService;


@Service("propsService")
public class PropsServiceImpl extends ServiceImpl<PropsDao, PropsEntity> implements PropsService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<PropsEntity> page = this.page(
                new Query<PropsEntity>().getPage(params),
                new QueryWrapper<PropsEntity>()
        );

        return new PageUtils(page);
    }

}