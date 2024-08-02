package com.lookup.mapper;

import java.util.Comparator;
import java.util.List;

import com.lookup.dto.request.CategoryCreateRequest;
import com.lookup.dto.response.CategoryResponse;
import com.lookup.entity.Category;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CategoryMapper {
  CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);

  CategoryResponse toDto(Category entity);

  default List<CategoryResponse> toDtoList(List<Category> entities) {
    return entities.stream()
        .map(this::toDto)
        .sorted(Comparator.comparing(CategoryResponse::id))
        .toList();
  }

  Category toEntity(CategoryCreateRequest request);
}
