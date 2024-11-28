package com.ambev.order.config;

import org.junit.jupiter.api.Test;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RedisConfigTest {

    @Test
    void testCacheManagerConfiguration() {
        // Mock da conexão Redis
        RedisConnectionFactory redisConnectionFactory = mock(RedisConnectionFactory.class);

        // Instância da classe a ser testada
        RedisConfig redisConfig = new RedisConfig();

        // Executa o método
        CacheManager cacheManager = redisConfig.cacheManager(redisConnectionFactory);

        // Validações
        assertNotNull(cacheManager, "CacheManager não deve ser nulo.");
        assertTrue(cacheManager instanceof RedisCacheManager, "CacheManager deve ser do tipo RedisCacheManager.");

    }

    @Test
    void testRedisTemplateConfiguration() {
        // Mock da conexão Redis
        RedisConnectionFactory redisConnectionFactory = mock(RedisConnectionFactory.class);

        // Instância da classe a ser testada
        RedisConfig redisConfig = new RedisConfig();

        // Executa o método
        RedisTemplate<String, Object> redisTemplate = redisConfig.redisTemplate(redisConnectionFactory);

        // Validações
        assertNotNull(redisTemplate, "RedisTemplate não deve ser nulo.");
        assertTrue(redisTemplate.getKeySerializer() instanceof StringRedisSerializer,
                "A chave deve ser serializada como String.");
        assertTrue(redisTemplate.getValueSerializer() instanceof GenericJackson2JsonRedisSerializer,
                "O valor deve ser serializado como JSON genérico.");
    }

    @Test
    void testCacheManagerHandlesEmptyConfiguration() {
        // Mock da conexão Redis
        RedisConnectionFactory redisConnectionFactory = mock(RedisConnectionFactory.class);

        // Instância da classe a ser testada
        RedisConfig redisConfig = new RedisConfig();

        // Remove a configuração de caches nomeados
        RedisCacheConfiguration defaultCacheConfig = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(10))
                .disableCachingNullValues();

        CacheManager cacheManager = RedisCacheManager.builder(redisConnectionFactory)
                .cacheDefaults(defaultCacheConfig)
                .build();

        assertNotNull(cacheManager, "CacheManager não deve ser nulo.");
        assertTrue(cacheManager instanceof RedisCacheManager, "CacheManager deve ser do tipo RedisCacheManager.");
    }
}
