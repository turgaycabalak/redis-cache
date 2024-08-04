package com.lookup.controller;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lookup.dto.request.CountryCreateRequest;
import com.lookup.dto.request.CountryUpdateRequest;
import com.lookup.dto.response.CountryResponse;
import com.lookup.entity.Country;
import com.lookup.mapper.CountryMapper;
import com.lookup.service.CountryService;

import lombok.RequiredArgsConstructor;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <a href="http://localhost:4070/swagger-ui/index.html#/">...</a>
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/country")
public class CountryController {
  private static final String TABLE_KEY = "Country";
  private final CountryService countryService;
  private final RedisTemplate<String, Object> redisTemplate;
  private final ObjectMapper objectMapper;


  @GetMapping
  public List<CountryResponse> getAllCountries() {
    Map<Object, Object> entriesCache = redisTemplate.opsForHash().entries(TABLE_KEY);
    long dbCount = countryService.getCount();

    if (!entriesCache.isEmpty() && (dbCount == entriesCache.size())) {
      List<Object> list = entriesCache.values().stream().toList();
      return list.stream()
          .map(o -> objectMapper.convertValue(o, CountryResponse.class))
          .sorted(Comparator.comparingLong(CountryResponse::id))
          .toList();
    }

    redisTemplate.delete(TABLE_KEY);

    List<Country> allCountries = countryService.getAllCountries();
    List<CountryResponse> dtoList = CountryMapper.INSTANCE.toDtoList(allCountries);

    dtoList.forEach(c -> redisTemplate.opsForHash().put(TABLE_KEY, c.id().toString(), c));
    redisTemplate.expire(TABLE_KEY, 30, TimeUnit.MINUTES);

    return dtoList;
  }

  @GetMapping("/{id}")
  public CountryResponse getCountryById(@PathVariable Long id) {
    Object o = redisTemplate.opsForHash().get(TABLE_KEY, id.toString());
    if (o != null) {
      return objectMapper.convertValue(o, CountryResponse.class);
    }

    Country countryById = countryService.getCountryById(id);
    CountryResponse dto = CountryMapper.INSTANCE.toDto(countryById);
    redisTemplate.opsForHash().put(TABLE_KEY, dto.id().toString(), dto);
    return dto;
  }

  @PostMapping
  public CountryResponse saveCountry(@RequestBody CountryCreateRequest request) {
    Country country = countryService.saveCountry(request);
    CountryResponse dto = CountryMapper.INSTANCE.toDto(country);

    redisTemplate.opsForHash().put(TABLE_KEY, dto.id().toString(), dto);
    return dto;
  }

  @PutMapping
  public CountryResponse updateCountryById(@RequestBody CountryUpdateRequest request) {
    Country country = countryService.updateCountryById(request);
    CountryResponse dto = CountryMapper.INSTANCE.toDto(country);

    redisTemplate.opsForHash().put(TABLE_KEY, dto.id().toString(), dto);
    return dto;
  }

  @DeleteMapping("/{id}")
  public void deleteCountryById(@PathVariable Long id) {
    countryService.deleteCountryById(id);
    redisTemplate.opsForHash().delete(TABLE_KEY, id.toString());
  }
}
