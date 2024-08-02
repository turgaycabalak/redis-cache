package com.lookup.mapper;

import java.util.Comparator;
import java.util.List;

import com.lookup.dto.request.CountryCreateRequest;
import com.lookup.dto.response.CountryResponse;
import com.lookup.entity.Country;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CountryMapper {
  CountryMapper INSTANCE = Mappers.getMapper(CountryMapper.class);

  CountryResponse toDto(Country entity);

  default List<CountryResponse> toDtoList(List<Country> entities) {
    return entities.stream()
        .map(this::toDto)
        .sorted(Comparator.comparing(CountryResponse::id))
        .toList();
  }

  Country toEntity(CountryCreateRequest request);
}
