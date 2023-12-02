package com.zsj.sms.feign;


import com.zsj.common.utils.R;
import com.zsj.common.vo.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient("zsjSystem")
public interface SystemFeign {

    //获取当前用户的余额
    @PostMapping("/system/user/getBalance")
    R getCurUserBalance(User entity);

}
