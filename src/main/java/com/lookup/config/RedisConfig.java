package com.lookup.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

  @Bean
  public ObjectMapper getObjectMapper() {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    objectMapper.registerModule(new JavaTimeModule()); // Register the JavaTimeModule to handle LocalDateTime
    objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); // Optional: Write dates as ISO strings
    return objectMapper;
  }

  @Bean
  public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
    RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
    redisTemplate.setConnectionFactory(connectionFactory);

    // Key and Hash Key serializers
    RedisSerializer<String> stringSerializer = new StringRedisSerializer();
    redisTemplate.setKeySerializer(stringSerializer);
    redisTemplate.setHashKeySerializer(stringSerializer);

    // Value and Hash Value serializers
    Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer =
        new Jackson2JsonRedisSerializer<>(getObjectMapper(), Object.class);

    redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
    redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);

    return redisTemplate;
  }

  @Bean("localeKeyGenerator")
  public KeyGenerator customKeyGenerator() {
    return (target, method, params) -> {
      var key = new StringBuilder();
      // key.append(cacheProperties.getRedis().getKeyPrefix());
      key.append("/");
      key.append(LocaleContextHolder.getLocale().getLanguage());
      for (Object param : params) {
        key.append("/");
        key.append(param.toString());
      }
      return key.toString();
    };
  }
}

