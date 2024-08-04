package com.lookup.service;

import java.util.List;

import com.lookup.dto.request.CategoryCreateRequest;
import com.lookup.dto.request.CategoryUpdateRequest;
import com.lookup.entity.Category;
import com.lookup.mapper.CategoryMapper;
import com.lookup.repository.CategoryRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryService {
  private final CategoryRepository categoryRepository;

  private Category getCategoryEntity(Long id) {
    return categoryRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Category not found by id: " + id));
  }

  @Cacheable(cacheNames = "categories", key = "'AllCategories'")
  public List<Category> getAllCategories() {
    return categoryRepository.findAll();
  }

  @Cacheable(value = "category", key = "#id")
  public Category getCategoryById(Long id) {
    return getCategoryEntity(id);
  }

  @CacheEvict(cacheNames = "categories", allEntries = true)
  public Category saveCategory(CategoryCreateRequest request) {
    Category entity = CategoryMapper.INSTANCE.toEntity(request);
    return categoryRepository.save(entity);
  }

  @CacheEvict(cacheNames = "categories", allEntries = true)
  public Category updateCategoryById(CategoryUpdateRequest request) {
    Category categoryById = getCategoryEntity(request.id());
    categoryById.setName(request.name());
    return categoryRepository.save(categoryById);
  }

  @CacheEvict(cacheNames = "categories", allEntries = true)
  public void deleteCategoryById(Long id) {
    Category categoryById = getCategoryEntity(id);
    categoryById.setActive(false);
    categoryRepository.save(categoryById);
  }
}
