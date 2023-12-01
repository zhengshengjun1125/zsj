package com.zsj.system.publish;

import com.alibaba.cloud.nacos.NacosConfigProperties;
import com.alibaba.csp.sentinel.util.StringUtil;
import com.zsj.system.publish.vo.FlowRuleEntity;
import com.zsj.system.rule.DynamicRulePublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author https://gitee.com/zhengshengjun
 * @date 2023/12/1.
 */
@Component
public class FlowRulePublisher implements DynamicRulePublisher<List<FlowRuleEntity>> {

    @Value("${sentinel.nacos.flow.group-id}")
    private String nacosFlowGroupId;

    @Autowired
    private NacosConfigProperties nacosConfigProperties;

    /**
     * 重要属性：
     * resource	        资源名，资源名是限流规则的作用对象
     * count	        限流阈值
     * grade	        限流阈值类型，QPS 或线程数模式	                                 QPS 模式
     * limitApp	        流控针对的调用来源	                                             default，代表不区分调用来源
     * strategy	        调用关系限流策略：直接、链路、关联	                             根据资源本身（直接）
     * controlBehavior	流控效果（直接拒绝 / 排队等待 / 慢启动模式），不支持按调用关系限流	 直接拒绝
     * @param app app name
     * @param rules list of rules to push
     * @throws Exception
     */
    @Override
    public void publish(String app, List<FlowRuleEntity> rules) throws Exception {
        if (StringUtil.isBlank(app)) {
            return;
        }
        if (rules == null) {
            return;
        }
        List<FlowRuleEntity> flowRules = rules.stream().map(data -> {
            FlowRuleEntity entity = new FlowRuleEntity();
            entity.setResource(data.getResource());
            entity.setCount(data.getCount());
            entity.setGrade(data.getGrade());
            entity.setLimitApp(data.getLimitApp());
            entity.setStrategy(data.getStrategy());
            entity.setControlBehavior(data.getControlBehavior());
            return entity;
        }).collect(Collectors.toList());
        String dataId = String.format("%s-flow-rule", app);
        HttpHandler.handler(flowRules, nacosConfigProperties, nacosFlowGroupId, dataId);
    }

}
