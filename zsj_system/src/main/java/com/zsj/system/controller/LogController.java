package com.zsj.system.controller;


import java.util.List;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zsj.system.entity.MenuEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.zsj.system.entity.LogEntity;
import com.zsj.system.service.LogService;
import com.zsj.common.utils.R;


/**
 * @author zsj
 * @email zsjemail666@163.com
 * @date 2023-10-05 20:07:09
 */
@RestController
@RequestMapping("system/log")
public class LogController {
    @Autowired
    private LogService logService;


    /**
     * 获取最近十条日志数据
     * @return 日志信息
     */
    @GetMapping("/listTen")
    public R listTen() {
        List<LogEntity> list = logService.getTen();
        return R.ok().put("data", list);
    }


    /**
     * 日志分页
     * @param cur 当前页
     * @param size 每页数量
     * @return 分页信息
     */
    @GetMapping("/list/{cur}/{size}")
    public R listByPage(@PathVariable("cur") int cur, @PathVariable("size")
    int size) {
        //cur 当前页  size每页数量
        Page<LogEntity> logEntityPage = new Page<>(cur, size);
        Page<LogEntity> page = logService.page(logEntityPage);
        return R.ok().put("data", page);
    }


    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public R save(@RequestBody LogEntity e) {
        if (logService.save(e)) return R.ok();
        return R.error();
    }

}
