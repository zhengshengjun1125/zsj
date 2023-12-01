package com.zsj.system.blockHandler;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.zsj.common.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author https://gitee.com/zhengshengjun
 * @date 2023/12/1.
 */
@Component
@Slf4j
public class CaptchaHandler {

    //触发noblockexception的方法
    //返回类型与原方法一致，参数类型需要和原方法相匹配，并在最后加上BlockException类型的参数
    public static R getCaptchaNoBlockHandler(BlockException e) {
        log.warn("获取验证码请求量大,触发拒绝");
        return R.reject();
    }

    //熔断机制
    public static R getCaptchaErrorFallback() {
        log.warn("获取验证码请求量大,触发熔断");
        return R.reject("请求频繁,稍后再试");
    }
}
