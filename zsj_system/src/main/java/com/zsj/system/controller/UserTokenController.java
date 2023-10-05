package com.zsj.system.controller;

import java.util.Arrays;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zsj.system.entity.UserTokenEntity;
import com.zsj.system.service.UserTokenService;
import com.zsj.common.utils.PageUtils;
import com.zsj.common.utils.R;



/**
 * 系统用户Token
 *
 * @author zsj
 * @email zsjemail666@163.com
 * @date 2023-10-05 20:07:09
 */
@RestController
@RequestMapping("system/usertoken")
public class UserTokenController {
    @Autowired
    private UserTokenService userTokenService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = userTokenService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{userId}")
    public R info(@PathVariable("userId") Long userId){
		UserTokenEntity userToken = userTokenService.getById(userId);

        return R.ok().put("userToken", userToken);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody UserTokenEntity userToken){
		userTokenService.save(userToken);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody UserTokenEntity userToken){
		userTokenService.updateById(userToken);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] userIds){
		userTokenService.removeByIds(Arrays.asList(userIds));

        return R.ok();
    }

}
