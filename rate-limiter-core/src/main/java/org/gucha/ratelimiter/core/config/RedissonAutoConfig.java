package org.gucha.ratelimiter.core.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * TODO 替换成redis
 * @author laichengfeng
 * @since 2019/7/17 8:21
 */
@Configuration
public class RedissonAutoConfig {

    private static String configPrefix = "redis://";

    @Value("${spring.redis.clusters}")
    private String cluster;

    @Bean(destroyMethod = "shutdown")
    public RedissonClient redisson() {
        Config config = new Config();
        String[] nodes = cluster.split(",");
        // 单机
        if(nodes.length <= 1) {
            config.useSingleServer().setAddress(configPrefix + nodes[0]);
        } else {
            //redisson版本是3.5，集群的ip前面要加上“redis://”，不然会报错，3.2版本可不加
            for (int i = 0; i < nodes.length; i++) {
                nodes[i] = configPrefix + nodes[i];
            }
            //这是用的集群server
            //设置集群状态扫描时间
            config.useClusterServers()
                    .setScanInterval(2000)
                    .addNodeAddress(nodes);
        }
        return Redisson.create(config);
    }
}