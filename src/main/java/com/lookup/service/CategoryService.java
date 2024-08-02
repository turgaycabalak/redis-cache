package com.lookup.service;

import java.util.List;

import com.lookup.dto.request.CategoryCreateRequest;
import com.lookup.dto.request.CategoryUpdateRequest;
import com.lookup.entity.Category;
import com.lookup.entity.Category;
import com.lookup.mapper.CategoryMapper;
import com.lookup.repository.CategoryRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryService {
  private final CategoryRepository categoryRepository;

  public List<Category> getAllCategories() {
    return categoryRepository.findAll();
  }

  public Category getCategoryById(Long id) {
    return categoryRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Category not found by id: " + id));
  }

  public Category saveCategory(CategoryCreateRequest request) {
    Category entity = CategoryMapper.INSTANCE.toEntity(request);
    return categoryRepository.save(entity);
  }

  public Category updateCategoryById(CategoryUpdateRequest request) {
    Category categoryById = getCategoryById(request.id());
    categoryById.setName(request.name());
    return categoryRepository.save(categoryById);
  }

  public void deleteCategoryById(Long id) {
    Category categoryById = getCategoryById(id);
    categoryById.setActive(false);
    categoryRepository.save(categoryById);
  }
}
