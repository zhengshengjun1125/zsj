package com.zsj.system.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zsj.common.utils.PageUtils;
import com.zsj.common.utils.Query;

import com.zsj.system.dao.CaptchaDao;
import com.zsj.system.entity.CaptchaEntity;
import com.zsj.system.service.CaptchaService;


@Service("captchaService")
public class CaptchaServiceImpl extends ServiceImpl<CaptchaDao, CaptchaEntity> implements CaptchaService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CaptchaEntity> page = this.page(
                new Query<CaptchaEntity>().getPage(params),
                new QueryWrapper<CaptchaEntity>()
        );

        return new PageUtils(page);
    }

}