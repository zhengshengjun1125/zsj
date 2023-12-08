package com.zsj.gateway.feign;

import com.zsj.common.utils.R;
import com.zsj.common.vo.LogEntity;
import com.zsj.gateway.vo.UserTokenEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;


/**
 * @Deprecated 此接口已经废弃
 * @author https://gitee.com/zhengshengjun
 * @date 2023/10/8.
 * 从这里调取日志接口
 */

@FeignClient("zsjSystem")
@Deprecated
public interface SystemFeign {
    @Deprecated
    @PostMapping("/system/token/check")
    public R checkTokenIsLegal(UserTokenEntity entity);


    @Deprecated
    @PostMapping("/system/log/save")
    public R saveLog(LogEntity entity);

}
