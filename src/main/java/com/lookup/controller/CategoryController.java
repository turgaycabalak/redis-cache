package com.lookup.controller;

import java.util.List;

import com.lookup.dto.request.CategoryCreateRequest;
import com.lookup.dto.request.CategoryUpdateRequest;
import com.lookup.dto.response.CategoryResponse;
import com.lookup.entity.Category;
import com.lookup.mapper.CategoryMapper;
import com.lookup.service.CategoryService;

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
@RequestMapping("api/v1/category")
@RequiredArgsConstructor
public class CategoryController {
  private final CategoryService categoryService;

  @GetMapping
  public List<CategoryResponse> getAllCategories() {
    List<Category> allCountries = categoryService.getAllCategories();
    return CategoryMapper.INSTANCE.toDtoList(allCountries);
  }

  @GetMapping("/{id}")
  public CategoryResponse getCategoryById(@PathVariable Long id) {
    Category categoryById = categoryService.getCategoryById(id);
    return CategoryMapper.INSTANCE.toDto(categoryById);
  }

  @PostMapping
  public CategoryResponse saveCategory(@RequestBody CategoryCreateRequest request) {
    Category category = categoryService.saveCategory(request);
    return CategoryMapper.INSTANCE.toDto(category);
  }

  @PutMapping
  public CategoryResponse updateCategoryById(@RequestBody CategoryUpdateRequest request) {
    Category category = categoryService.updateCategoryById(request);
    return CategoryMapper.INSTANCE.toDto(category);
  }

  @DeleteMapping("/{id}")
  public void deleteCategoryById(@PathVariable Long id) {
    categoryService.deleteCategoryById(id);
  }
}
