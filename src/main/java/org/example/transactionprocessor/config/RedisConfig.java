package org.example.transactionprocessor.config;

import org.example.transactionprocessor.entity.Balance;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.math.BigDecimal;

/**
 * Configuration class for Redis setup.
 * Defines connection factory and templates for handling Redis operations.
 */
@Configuration
public class RedisConfig {

    /**
     * Creates a connection factory for Redis.
     *
     * @return a new instance of {@link LettuceConnectionFactory}
     */
    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory();
    }

    /**
     * Configures a Redis template for storing {@link Balance} objects.
     *
     * @param connectionFactory the Redis connection factory
     * @return a RedisTemplate instance for handling Balance objects
     */
    @Bean
    public RedisTemplate<String, Balance> redisTemplateBalance(LettuceConnectionFactory connectionFactory) {
        RedisTemplate<String, Balance> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new Jackson2JsonRedisSerializer<>(Balance.class));
        return template;
    }

    /**
     * Configures a Redis template for storing {@link BigDecimal} values.
     *
     * @param connectionFactory the Redis connection factory
     * @return a RedisTemplate instance for handling BigDecimal values
     */
    @Bean
    public RedisTemplate<String, BigDecimal> redisTemplateBigDecimal(LettuceConnectionFactory connectionFactory) {
        RedisTemplate<String, BigDecimal> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new Jackson2JsonRedisSerializer<>(BigDecimal.class));
        return template;
    }
}
