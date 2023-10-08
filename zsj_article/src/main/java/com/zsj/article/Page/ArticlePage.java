package com.zsj.article.Page;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.zsj.article.entity.EntityEntity;
import com.zsj.article.entity.EntityToForce;
import lombok.Data;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author https://gitee.com/zhengshengjun
 * @date 2023/10/8.
 */

@Data
public final class ArticlePage implements Serializable {

    private static final long serialVersionUID = 1213123444L;

    private List<EntityToForce> records;
    private long total;
    private long size;
    private long current;
    private List<OrderItem> orders;
    private boolean optimizeCountSql;
    private boolean isSearchCount;
    private boolean hitCount;

    public ArticlePage(IPage<EntityEntity> page, Map<Long, String> collect) {
        List<EntityEntity> list = page.getRecords();
        this.records = list.stream().map(entity -> new EntityToForce(entity, collect.get(entity.getArtClassId())))
                .collect(Collectors.toList());
        this.total = page.getTotal();
        this.size = page.getSize();
        this.current = page.getCurrent();
        this.optimizeCountSql = page.optimizeCountSql();
        this.isSearchCount = page.searchCount();
        this.hitCount = page.searchCount();
    }


    public void setRecords(List<EntityToForce> records) {
        this.records = records;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public void setCurrent(long current) {
        this.current = current;
    }

    public void setOrders(List<OrderItem> orders) {
        this.orders = orders;
    }

    public void setOptimizeCountSql(boolean optimizeCountSql) {
        this.optimizeCountSql = optimizeCountSql;
    }

    public void setSearchCount(boolean searchCount) {
        isSearchCount = searchCount;
    }

    public void setHitCount(boolean hitCount) {
        this.hitCount = hitCount;
    }

    public List<EntityToForce> getRecords() {
        return records;
    }

    public long getTotal() {
        return total;
    }

    public long getSize() {
        return size;
    }

    public long getCurrent() {
        return current;
    }

    public List<OrderItem> getOrders() {
        return orders;
    }

    public boolean isOptimizeCountSql() {
        return optimizeCountSql;
    }

    public boolean isSearchCount() {
        return isSearchCount;
    }

    public boolean isHitCount() {
        return hitCount;
    }
}
