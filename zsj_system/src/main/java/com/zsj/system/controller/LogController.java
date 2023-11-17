package com.zsj.system.controller;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;


import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mysql.cj.log.Log;
import com.zsj.common.utils.ObjectUtil;
import com.zsj.system.entity.MenuEntity;
import com.zsj.system.vo.UserVoExcel;
import com.zsj.system.vo.UserVoList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import com.zsj.system.entity.LogEntity;
import com.zsj.system.service.LogService;
import com.zsj.common.utils.R;

import javax.servlet.http.HttpServletResponse;


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
     *
     * @return 日志信息
     */
    @GetMapping("/listTen")
    public R listTen() {
        List<LogEntity> list = logService.getTen();
        return R.ok().put("data", list);
    }


    /**
     * 日志分页
     *
     * @param cur  当前页
     * @param size 每页数量
     * @return 分页信息
     */
    @PostMapping("/list/{cur}/{size}")
    public R listByPage(@PathVariable("cur") int cur,
                        @PathVariable("size") int size,
                        @Nullable @RequestBody LogEntity entity) {
        if (checkEntityIsEmptyParam(entity)) {
            //空参查所有
            //cur 当前页  size每页数量
            Page<LogEntity> logEntityPage = new Page<>(cur, size);
            Page<LogEntity> page = logService.page(logEntityPage);
            return R.ok().put("data", page);
        }
        //条件查询
        Page<LogEntity> entityPage = logService.pageByCondition(cur, size, entity);
        return R.ok().put("data", entityPage);
    }


    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public R save(@RequestBody LogEntity e) {
        if (logService.save(e)) return R.ok();
        return R.error();
    }

    private static boolean checkEntityIsEmptyParam(LogEntity entity) {
        return ObjectUtil.objectIsNull(entity) ||
                (ObjectUtil.isNullOrEmpty(entity.getIp()) &&
                        ObjectUtil.isNullOrEmpty(entity.getUsername()) &&
                        ObjectUtil.isNullOrEmpty(entity.getOperation()) &&
                        ObjectUtil.isNullOrEmpty(entity.getMethod()));

    }

    @GetMapping("/excel/export")
    public void exportUserListExcel(HttpServletResponse response) {
        try {
            this.setExcelResponseProp(response, "日志列表");
            EasyExcel
                    .write(response.getOutputStream())
                    .head(LogEntity.class)
                    .sheet("日志信息表")
                    .doWrite(logService.list());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 设置响应结果
     *
     * @param response    响应结果对象
     * @param rawFileName 文件名
     * @throws UnsupportedEncodingException 不支持编码异常
     */
    private void setExcelResponseProp(HttpServletResponse response, String rawFileName) throws UnsupportedEncodingException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode(rawFileName, "UTF-8").replaceAll(" ", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
    }
}
