package com.chenxin.miniapp.config.jedisConfig;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import redis.clients.jedis.*;

import java.util.HashSet;
import java.util.Set;

@Component
public class JedisConfig {
    @Value("${jedisCluster.pool}")
    private String jedisCluster;

    private final Jedis jedis = new Jedis("127.0.0.1", 6379);

    @Bean("jedisTemplate")
    public Jedis getJedis() {
        return jedis;
    }

    @Bean
    public JedisCluster getJedisCluster() {
        Set<HostAndPort> hostAndport = new HashSet<>();
        hostAndport.add(new HostAndPort("127.0.0.1", 6379));
        hostAndport.add(new HostAndPort("127.0.0.1", 6380));
        hostAndport.add(new HostAndPort("127.0.0.1", 6381));
        return new JedisCluster(hostAndport);
    }

    @Bean
    public JedisCluster jedisCluster() {
        Set<HostAndPort> hostAndPorts = new HashSet<>();
        String[] hostAndportsArr = this.jedisCluster.split("\\|\\|");
        for (String hostAndPort : hostAndportsArr) {
            String[] oneHostAndPort = hostAndPort.split(":");
            HostAndPort hostAndPort1 = new HostAndPort(oneHostAndPort[0], Integer.valueOf(oneHostAndPort[1]));
            hostAndPorts.add(hostAndPort1);
        }
        int timeout = 1000 * 10;
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(20);
        poolConfig.setMaxIdle(10);
        poolConfig.setMaxWaitMillis(1000 * 10);
        poolConfig.setTestOnBorrow(false);
        return new JedisCluster(hostAndPorts, timeout, poolConfig);
    }


}
