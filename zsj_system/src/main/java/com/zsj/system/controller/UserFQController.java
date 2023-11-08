package com.zsj.system.controller;


import com.zsj.common.utils.R;
import com.zsj.system.entity.UserFQEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.zsj.system.service.UserFQService;


/**
 * @author zsj
 * @email zsjemail666@163.com
 * @date 2023-11-08 20:18:48
 */
@RestController
@RequestMapping("system/homieQ")
public class UserFQController {
    @Autowired
    private UserFQService userFQService;


    @PostMapping("/demand")
    public R demandQueue(@RequestBody UserFQEntity entity) {
        //加入好友申请列表
        return userFQService.save(entity) ? R.ok() : R.error();
    }


}
