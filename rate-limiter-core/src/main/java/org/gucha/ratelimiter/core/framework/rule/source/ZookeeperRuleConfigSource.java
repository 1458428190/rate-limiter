package org.gucha.ratelimiter.core.framework.rule.source;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.CloseableUtils;
import org.gucha.ratelimiter.core.framework.env.RateLimiterConfig;
import org.gucha.ratelimiter.core.framework.extension.Order;
import org.gucha.ratelimiter.core.framework.rule.parser.RuleConfigParser;

import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.gucha.ratelimiter.core.framework.context.RateLimiterBeansFactory.BEANS_CONTEXT;

/**
 * @Description:
 * @Author : laichengfeng
 * @Date : 2021/03/29 下午4:54
 */
@Order(Order.HIGHEST_PRECEDENCE + 20)
@Slf4j
public class ZookeeperRuleConfigSource implements RuleConfigSource {

    private static final Charset UTF8 = Charset.forName("UTF-8");
    private static final int MAX_RETRIES = 3;
    private static final int BASE_SLEEP_TIME_MS = (int) TimeUnit.SECONDS.toMillis(1);
    private static final int TIMEOUT = (int) TimeUnit.SECONDS.toMillis(3);

    private String address;
    private String path;
    private boolean needKeepAlive = false;
    private int maxRetries = MAX_RETRIES;
    private int baseSleepTimeMs = BASE_SLEEP_TIME_MS;
    private int connectionTimeout = TIMEOUT;
    private int sessionTimeout = TIMEOUT;
    private CuratorFramework client;
    private RuleConfigParser ruleConfigParser;

    private AtomicBoolean isInitialized = new AtomicBoolean(false);

    public ZookeeperRuleConfigSource() {
        this(null);
    }

    public ZookeeperRuleConfigSource(RuleConfigParser ruleConfigParser) {
        this(null, null, ruleConfigParser);
    }

    public ZookeeperRuleConfigSource(String address, String path, RuleConfigParser ruleConfigParser) {
        if (StringUtils.isEmpty(address)) {
            address = RateLimiterConfig.instance().getZookeeperConfig().getAddress();
        }
        if (StringUtils.isEmpty(path)) {
            path = RateLimiterConfig.instance().getZookeeperConfig().getPath();
        }
        this.address = address;
        this.path = path;
        this.ruleConfigParser = BEANS_CONTEXT.obtainRuleConfigParser(ruleConfigParser);
    }

    @Override
    public UniformRuleConfigMapping load() {
        initialClient();
        UniformRuleConfigMapping uniformRuleConfigMapping = loadRateLimitRule();
        if (!needKeepAlive) {
            CloseableUtils.closeQuietly(client);
            isInitialized.compareAndSet(true, false);
        }
        return uniformRuleConfigMapping;
    }

    /**
     * 初始化客户端
     */
    private void initialClient() {
        if (!isInitialized.compareAndSet(false, true)) {
            return;
        }

        if (client == null && StringUtils.isEmpty(address)) {
            throw new RuntimeException("zookeeper server address is not set.");
        }

        boolean connected;
        try {
            if (client == null) {
                RetryPolicy retryPolicy = new ExponentialBackoffRetry(baseSleepTimeMs, maxRetries);
                client = CuratorFrameworkFactory.builder().connectString(address).retryPolicy(retryPolicy)
                        .connectionTimeoutMs(connectionTimeout).sessionTimeoutMs(sessionTimeout).build();
            }
            client.start();
            connected = client.blockUntilConnected(connectionTimeout, TimeUnit.MILLISECONDS);
            if (!connected) {
                throw new RuntimeException("connection zookeeper failed.");
            }
        } catch (Exception e) {
            CloseableUtils.closeQuietly(client);
            isInitialized.compareAndSet(true, false);
            throw new RuntimeException("init zookeeper client, error.", e);
        }
    }

    private UniformRuleConfigMapping loadRateLimitRule() {
        byte[] result;
        try {
            result = client.getData().forPath(this.path);
        } catch (Exception e) {
            throw new RuntimeException("load rule from zookeeper failed.", e);
        }
        if (result != null && result.length > 0) {
            String configurationText = new String(result, UTF8);
            return ruleConfigParser.parse(configurationText);
        }
        log.warn("configuration of zookeeper node path: {} is empty.", path);
        return null;
    }
}
