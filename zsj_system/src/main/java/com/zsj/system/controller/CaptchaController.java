package com.zsj.system.controller;

import java.util.Arrays;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zsj.system.entity.CaptchaEntity;
import com.zsj.system.service.CaptchaService;
import com.zsj.common.utils.PageUtils;
import com.zsj.common.utils.R;



/**
 * 系统验证码
 *
 * @author zsj
 * @email zsjemail666@163.com
 * @date 2023-10-05 20:07:09
 */
@RestController
@RequestMapping("system/captcha")
public class CaptchaController {
    @Autowired
    private CaptchaService captchaService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = captchaService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{uuid}")
    public R info(@PathVariable("uuid") String uuid){
		CaptchaEntity captcha = captchaService.getById(uuid);

        return R.ok().put("captcha", captcha);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody CaptchaEntity captcha){
		captchaService.save(captcha);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody CaptchaEntity captcha){
		captchaService.updateById(captcha);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody String[] uuids){
		captchaService.removeByIds(Arrays.asList(uuids));

        return R.ok();
    }

}
