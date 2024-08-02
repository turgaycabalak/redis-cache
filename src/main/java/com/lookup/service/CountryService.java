package com.lookup.service;

import java.util.List;

import com.lookup.dto.request.CountryCreateRequest;
import com.lookup.dto.request.CountryUpdateRequest;
import com.lookup.entity.Country;
import com.lookup.mapper.CountryMapper;
import com.lookup.repository.CountryRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CountryService {
  private final CountryRepository countryRepository;

  public List<Country> getAllCountries() {
    return countryRepository.findAll();
  }

  public Country getCountryById(Long id) {
    return countryRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Country not found by id: " + id));
  }

  public Country saveCountry(CountryCreateRequest request) {
    Country entity = CountryMapper.INSTANCE.toEntity(request);
    return countryRepository.save(entity);
  }

  public Country updateCountryById(CountryUpdateRequest request) {
    Country countryById = getCountryById(request.id());
    countryById.setName(request.name());
    countryById.setCapital(request.capital());
    return countryRepository.save(countryById);
  }

  public void deleteCountryById(Long id) {
    Country countryById = getCountryById(id);
    countryById.setActive(false);
    countryRepository.save(countryById);
  }

}
