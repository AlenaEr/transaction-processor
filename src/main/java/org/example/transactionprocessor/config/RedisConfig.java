package org.example.transactionprocessor.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.math.BigDecimal;

@Configuration
public class RedisConfig {
    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(); // Подключается по умолчанию к localhost:6379
    }

    @Bean
    public RedisTemplate<String, BigDecimal> redisTemplate(LettuceConnectionFactory connectionFactory) {
        RedisTemplate<String, BigDecimal> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new Jackson2JsonRedisSerializer<>(BigDecimal.class));
        return template;
    }
}
