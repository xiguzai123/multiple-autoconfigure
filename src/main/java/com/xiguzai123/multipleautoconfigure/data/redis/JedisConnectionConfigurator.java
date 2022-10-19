package com.xiguzai123.multipleautoconfigure.data.redis;

import lombok.SneakyThrows;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.data.redis.LettuceClientConfigurationBuilderCustomizer;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * @author chenyanhong
 * @since 2022-10-19 16:46
 */
public class JedisConnectionConfigurator {

    @SneakyThrows
    public static JedisConnectionFactory redisConnectionFactory(RedisProperties redisProperties,
                                                                  ObjectProvider<RedisStandaloneConfiguration> standaloneConfigurationProvider,
                                                                  ObjectProvider<RedisSentinelConfiguration> sentinelConfigurationProvider,
                                                                  ObjectProvider<RedisClusterConfiguration> clusterConfigurationProvider,
                                                                  ObjectProvider<LettuceClientConfigurationBuilderCustomizer> builderCustomizers) {
        final Class<?> clazz = Class.forName("org.springframework.boot.autoconfigure.data.redis.JedisConnectionConfiguration");
        final Constructor<?> constructor = ReflectionUtils
                .accessibleConstructor(clazz, RedisProperties.class, ObjectProvider.class, ObjectProvider.class, ObjectProvider.class);
        constructor.setAccessible(true);
        final Object instance = constructor.newInstance(redisProperties, standaloneConfigurationProvider, sentinelConfigurationProvider, clusterConfigurationProvider);
        final Method[] methods = ReflectionUtils.getDeclaredMethods(clazz);
        final Method redisConnectionFactory = Arrays.stream(methods).filter(method -> "redisConnectionFactory".equals(method.getName())).findFirst().orElse(null);
        redisConnectionFactory.setAccessible(true);
        return (JedisConnectionFactory) redisConnectionFactory.invoke(instance, builderCustomizers);
    }
}
