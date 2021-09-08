package com.github.chengzhy.basiccode.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * elasticsearch配置
 * @author chengzhy
 * @date 2021/8/6 11:05
 **/
@Configuration
public class ElasticsearchConfig {

    @Value("#{'${elasticsearch.hostlist:127.0.0.1:9200}'.split(',')}")
    private List<String> hostlist;

    @Bean
    public RestHighLevelClient restHighLevelClient() {
        return new RestHighLevelClient(RestClient.builder(httpHosts()));
    }

    private HttpHost[] httpHosts() {
        HttpHost[] httpHosts = new HttpHost[hostlist.size()];
        try {
            for (int i=0; i<httpHosts.length; i++) {
                String[] host = hostlist.get(i).split(":");
                httpHosts[i] = new HttpHost(host[0], Integer.parseInt(host[1]));
            }
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            throw new IllegalArgumentException("elasticsearch配置错误！");
        }
        return httpHosts;
    }

}
