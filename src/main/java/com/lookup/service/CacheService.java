package com.lookup.service;

import java.lang.reflect.Field;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.LongFunction;
import java.util.function.LongSupplier;
import java.util.function.Supplier;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lookup.entity.BaseEntity;

import lombok.RequiredArgsConstructor;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

@Component
@RequiredArgsConstructor
public class CacheService {
  private final RedisTemplate<String, Object> redisTemplate;
  private final ObjectMapper objectMapper;
  private String idFieldName = "id";

  public void deleteFromCache(String tableKey, String id) {
    redisTemplate.opsForHash().delete(tableKey, id);
  }

  public <R, E extends BaseEntity> R updateCahce(String tableKey,
                                                 E entity,
                                                 Class<R> rClass) {
    R response = objectMapper.convertValue(entity, rClass);
    cacheEntries(tableKey, List.of(response));

    return response;
  }

  public <R, E extends BaseEntity> R getById(String tableKey,
                                             Long id,
                                             LongFunction<E> getByIdFromService,
                                             Class<R> rClass) {
    Object o = redisTemplate.opsForHash().get(tableKey, id.toString());
    if (o != null) {
      return objectMapper.convertValue(o, rClass);
    }

    E entityById = getByIdFromService.apply(id);
    R response = objectMapper.convertValue(entityById, rClass);


    cacheEntries(tableKey, List.of(response));

    return response;
  }

  public <R, E extends BaseEntity> R getById(String tableKey,
                                             Long id,
                                             LongFunction<E> getByIdFromService,
                                             Class<R> rClass,
                                             String idFieldName) {
    this.idFieldName = idFieldName;
    return getById(tableKey, id, getByIdFromService, rClass);
  }

  public <R, E extends BaseEntity> List<R> getAll(String tableKey,
                                                  LongSupplier getCountFromService,
                                                  Supplier<List<E>> getAllFromService,
                                                  Class<R> rClass) {
    Map<Object, Object> entriesCache = redisTemplate.opsForHash().entries(tableKey);
    long dbCount = getCountFromService.getAsLong();

    if (!entriesCache.isEmpty() && (dbCount == entriesCache.size())) {
      return getCachedEntries(entriesCache, rClass);
    }

    redisTemplate.delete(tableKey);
    return fetchAndCacheEntries(tableKey, getAllFromService, rClass);
  }

  public <R, E extends BaseEntity> List<R> getAll(String tableKey,
                                                  LongSupplier getCountFromService,
                                                  Supplier<List<E>> getAllFromService,
                                                  Class<R> rClass,
                                                  String idFieldName) {
    this.idFieldName = idFieldName;
    return getAll(tableKey, getCountFromService, getAllFromService, rClass);
  }

  private <R> List<R> getCachedEntries(Map<Object, Object> entriesCache, Class<R> rClass) {
    return entriesCache.values().stream()
        .map(value -> objectMapper.convertValue(value, rClass))
        .sorted(Comparator.comparingLong(this::getIdField))
        .toList();
  }

  private <R, E extends BaseEntity> List<R> fetchAndCacheEntries(String tableKey,
                                                                 Supplier<List<E>> getAllFromService,
                                                                 Class<R> rClass) {
    List<E> allEntities = getAllFromService.get();
    List<R> sortedList = allEntities.stream()
        .map(entity -> objectMapper.convertValue(entity, rClass))
        .sorted(Comparator.comparingLong(this::getIdField))
        .toList();

    cacheEntries(tableKey, sortedList);

    return sortedList;
  }

  private <R> void cacheEntries(String tableKey, List<R> entries) {
    entries.forEach(entry -> {
      try {
        Field idField = ReflectionUtils.findField(entry.getClass(), idFieldName);
        if (idField != null) {
          idField.setAccessible(true);
          Object idValue = idField.get(entry);
          redisTemplate.opsForHash().put(tableKey, idValue.toString(), entry);
          redisTemplate.expire(tableKey, 30, TimeUnit.MINUTES);
        }
      } catch (IllegalAccessException e) {
        throw new RuntimeException("Failed to access 'id' field", e);
      }
    });
  }

  private <R> long getIdField(R object) {
    try {
      Field idField = ReflectionUtils.findField(object.getClass(), idFieldName);
      if (idField != null) {
        idField.setAccessible(true);
        return (Long) idField.get(object);
      }
      throw new RuntimeException("No 'id' field found");
    } catch (IllegalAccessException e) {
      throw new RuntimeException("Failed to access 'id' field", e);
    }
  }
}
