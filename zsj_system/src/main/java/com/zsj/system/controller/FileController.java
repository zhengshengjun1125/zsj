package com.zsj.system.controller;


import com.zsj.common.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.zsj.system.service.FileService;


/**
 * @author zsj
 * @email zsjemail666@163.com
 * @date 2023-11-12 15:55:51
 */
@RestController
@RequestMapping("system/file")
public class FileController {
    @Autowired
    private FileService fileService;


    @GetMapping("/list")
    public R list() {
        return R.ok().put("data", fileService.list());
    }
}
