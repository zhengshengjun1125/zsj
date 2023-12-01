package com.zsj.system.rule;

import java.util.Date;

import com.alibaba.csp.sentinel.slots.block.Rule;


public interface RuleEntity {

    Long getId();

    void setId(Long id);

    String getApp();

    String getIp();

    Integer getPort();

    Date getGmtCreate();

    Rule toRule();
}
