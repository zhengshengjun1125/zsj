package com.zsj.system.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import com.wf.captcha.ArithmeticCaptcha;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.*;

import com.zsj.system.entity.CaptchaEntity;
import com.zsj.system.service.CaptchaService;
import com.zsj.common.utils.PageUtils;
import com.zsj.common.utils.R;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;


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

    @Autowired
    @Lazy
    RedisTemplate<String,String> redisTemplate;


    /**
     * 获取验证码 算术验证码
     *
     * 思路
     * 后端接收到请求 将图片以及uuid返回给前端
     * 并且将uuid对应的结果值保存到redis中 设置过期时间为1分钟
     */
    @GetMapping("/get")
    public R getCaptcha() throws IOException {
        ArithmeticCaptcha captcha = new ArithmeticCaptcha(120,40);
        captcha.setLen(3);
        String result = captcha.text();
        UUID uuid = UUID.randomUUID();
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        ops.set(uuid.toString(),result,60, TimeUnit.SECONDS);
        String base64 = captcha.toBase64();
        captchaService.saveCaptcha(new CaptchaEntity(uuid.toString(),result,new Date(System.currentTimeMillis()+60000),base64));
        return R.ok("获取验证码成功").put("captcha",base64).put("key_id",uuid.toString());
    }

    /**
     * 检测验证码
     */
    @GetMapping("/check")
    public R checkCaptcha(@Param("key")String key,@Param("code")String code){
        ValueOperations<String, String> op = redisTemplate.opsForValue();
        if (op.get(key) !=null){
            if (code.equals(op.get(key))){
                redisTemplate.delete(key);//删除对应key
                return R.ok("验证成功");
            }
        }
        return R.error("验证失败！");
    }


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
