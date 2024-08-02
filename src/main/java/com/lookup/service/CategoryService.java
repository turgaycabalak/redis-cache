package com.lookup.service;

import java.util.List;

import com.lookup.entity.Category;
import com.lookup.repository.CategoryRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryService {
  private final CategoryRepository categoryRepository;

  public List<Category> getAllCountries() {
    return categoryRepository.findAll();
  }

  public Category getCategoryById(Long id) {
    return categoryRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Category not found by id: " + id));
  }
}
