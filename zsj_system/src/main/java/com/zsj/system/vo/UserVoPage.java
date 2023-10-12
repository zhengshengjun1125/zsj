package com.zsj.system.vo;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;

import java.util.List;

/**
 * @author https://gitee.com/zhengshengjun
 * @date 2023/10/12.
 */
public class UserVoPage implements IPage<UserVo> {
    private List<UserVo> records;
    // 其他分页属性的定义，如 current、size、total等

    @Override
    public List<OrderItem> orders() {
        return null;
    }

    @Override
    public List<UserVo> getRecords() {
        return records;
    }

    @Override
    public UserVoPage setRecords(List<UserVo> records) {
        this.records = records;
        return this;
    }

    @Override
    public long getTotal() {
        return 0;
    }

    @Override
    public IPage<UserVo> setTotal(long total) {
        return null;
    }

    @Override
    public long getSize() {
        return 0;
    }

    @Override
    public IPage<UserVo> setSize(long size) {
        return null;
    }

    @Override
    public long getCurrent() {
        return 0;
    }

    @Override
    public IPage<UserVo> setCurrent(long current) {
        return null;
    }

    // 其他 getter 和 setter 方法
}
