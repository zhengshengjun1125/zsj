package com.zsj.system.controller;

import java.util.Arrays;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zsj.system.entity.OssEntity;
import com.zsj.system.service.OssService;
import com.zsj.common.utils.PageUtils;
import com.zsj.common.utils.R;



/**
 * 文件上传
 *
 * @author zsj
 * @email zsjemail666@163.com
 * @date 2023-10-05 20:07:09
 */
@RestController
@RequestMapping("system/oss")
public class OssController {
    @Autowired
    private OssService ossService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = ossService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
		OssEntity oss = ossService.getById(id);

        return R.ok().put("oss", oss);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody OssEntity oss){
		ossService.save(oss);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody OssEntity oss){
		ossService.updateById(oss);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
		ossService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
