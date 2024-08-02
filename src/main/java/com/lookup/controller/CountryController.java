package com.lookup.controller;

import java.util.List;

import com.lookup.dto.request.CountryCreateRequest;
import com.lookup.dto.request.CountryUpdateRequest;
import com.lookup.dto.response.CountryResponse;
import com.lookup.entity.Country;
import com.lookup.mapper.CountryMapper;
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
  private final CountryService countryService;

  @GetMapping
  public List<CountryResponse> getAllCountries() {
    List<Country> allCountries = countryService.getAllCountries();
    return CountryMapper.INSTANCE.toDtoList(allCountries);
  }

  @GetMapping("/{id}")
  public CountryResponse getCountryById(@PathVariable Long id) {
    Country countryById = countryService.getCountryById(id);
    return CountryMapper.INSTANCE.toDto(countryById);
  }

  @PostMapping
  public CountryResponse saveCountry(@RequestBody CountryCreateRequest request) {
    Country country = countryService.saveCountry(request);
    return CountryMapper.INSTANCE.toDto(country);
  }

  @PutMapping
  public CountryResponse updateCountryById(@RequestBody CountryUpdateRequest request) {
    Country country = countryService.updateCountryById(request);
    return CountryMapper.INSTANCE.toDto(country);
  }

  @DeleteMapping("/{id}")
  public void deleteCountryById(@PathVariable Long id) {
    countryService.deleteCountryById(id);
  }
}
