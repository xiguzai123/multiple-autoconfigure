package com.xiguzai123.multipleautoconfigure.demo;

import com.xiguzai123.multipleautoconfigure.data.redis.JedisConnectionConfigurator;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.data.redis.LettuceClientConfigurationBuilderCustomizer;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;

/**
 * @author hong
 * @date 2023/7/21 22:37
 */
// @Configuration
public class MultipleAutoConfigureDemo {

    @Bean("multipleRedisProperties")
    @ConfigurationProperties(prefix = "multiple.redis")
    public RedisProperties redisProperties() {
        return new RedisProperties();
    }

    @Bean("multipleJedisConnectionFactory")
    public JedisConnectionFactory jedisConnectionFactory(@Qualifier("multipleRedisProperties") RedisProperties redisProperties,
                                                         ObjectProvider<RedisStandaloneConfiguration> standaloneConfigurationProvider,
                                                         ObjectProvider<RedisSentinelConfiguration> sentinelConfigurationProvider,
                                                         ObjectProvider<RedisClusterConfiguration> clusterConfigurationProvider,
                                                         ObjectProvider<LettuceClientConfigurationBuilderCustomizer> builderCustomizers) {
        return JedisConnectionConfigurator.redisConnectionFactory(redisProperties, standaloneConfigurationProvider, sentinelConfigurationProvider, clusterConfigurationProvider, builderCustomizers);
    }
}
