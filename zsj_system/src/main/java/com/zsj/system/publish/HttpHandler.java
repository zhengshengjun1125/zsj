package com.zsj.system.publish;

import com.alibaba.cloud.nacos.NacosConfigProperties;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.StringJoiner;

/**
 * @author https://gitee.com/zhengshengjun
 * @date 2023/12/1.
 */
@Slf4j
public class HttpHandler {


    public static void handler(Object rules, NacosConfigProperties nacosConfigProperties, String groupId, String dataId) throws IOException {
        StringJoiner param = new StringJoiner("&");
        param.add(String.format("tenant=%s", nacosConfigProperties.getNamespace())); // Nacos 的命名空间ID字段
        param.add(String.format("dataId=%s", dataId));
        param.add(String.format("group=%s", groupId));
        param.add(String.format("content=%s", URLEncoder.encode(JSON.toJSONString(rules), "UTF-8")));
        param.add(String.format("type=%s", "json"));
        String url = String.format("http://%s/nacos/v1/cs/configs?%s", nacosConfigProperties.getServerAddr(), param.toString());
        HttpPost httpPost = new HttpPost(url);
        CloseableHttpClient httpclient = HttpClients.createDefault();
        CloseableHttpResponse response = httpclient.execute(httpPost);
        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode == 200) {
            HttpEntity entity = response.getEntity();
            String result = EntityUtils.toString(entity);
            log.info("========> 发布成功：{}", result);
        } else {
            log.error("========> 发布失败：{}", response.toString());
        }
    }
}
