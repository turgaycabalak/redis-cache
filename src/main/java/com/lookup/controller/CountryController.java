package com.lookup.controller;

import java.util.List;

import com.lookup.dto.request.CountryCreateRequest;
import com.lookup.dto.request.CountryUpdateRequest;
import com.lookup.dto.response.CountryResponse;
import com.lookup.entity.Country;
import com.lookup.service.CacheService;
import com.lookup.service.CountryService;

import lombok.RequiredArgsConstructor;

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
  private final CacheService cacheService;


  @GetMapping
  public List<CountryResponse> getAllCountries() {
    return cacheService.getAll(TABLE_KEY, countryService::getCount, countryService::getAllCountries,
        CountryResponse.class);
  }

  @GetMapping("/{id}")
  public CountryResponse getCountryById(@PathVariable Long id) {
    return cacheService.getById(TABLE_KEY, id, countryService::getCountryById, CountryResponse.class);
  }

  @PostMapping
  public CountryResponse saveCountry(@RequestBody CountryCreateRequest request) {
    Country country = countryService.saveCountry(request);
    return cacheService.updateCahce(TABLE_KEY, country, CountryResponse.class);
  }

  @PutMapping
  public CountryResponse updateCountryById(@RequestBody CountryUpdateRequest request) {
    Country country = countryService.updateCountryById(request);
    return cacheService.updateCahce(TABLE_KEY, country, CountryResponse.class);
  }

  @DeleteMapping("/{id}")
  public void deleteCountryById(@PathVariable Long id) {
    countryService.deleteCountryById(id);
    cacheService.deleteFromCache(TABLE_KEY, id.toString());
  }
}
