package com.mm.accountstatementparser.controller;

import com.mm.accountstatementparser.dto.entityDto.CategoryDto;
import com.mm.accountstatementparser.entity.Category;
import com.mm.accountstatementparser.service.CategoryService;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/category")
@RequiredArgsConstructor
public class CategoryController {
  private final CategoryService categoryService;

  @GetMapping
  public ResponseEntity<List<CategoryDto>> getAllCategories() {
    return new ResponseEntity<>(
        categoryService.getAll().stream().map(Category::toDto).toList(), HttpStatus.OK);
  }

  @GetMapping("/{id}")
  public ResponseEntity<CategoryDto> getCategoryById(@PathVariable UUID id) {
    return new ResponseEntity<>(categoryService.getEntityById(id).toDto(), HttpStatus.OK);
  }

  @PostMapping
  public ResponseEntity<CategoryDto> createCategory(@RequestBody CategoryDto categoryDto) {
    return new ResponseEntity<>(
        categoryService.persistEntity(categoryDto.toEntity()).toDto(), HttpStatus.CREATED);
  }

  @PutMapping("/{id}")
  public ResponseEntity<CategoryDto> updateCategoryById(
      @PathVariable UUID id, @RequestBody CategoryDto categoryDto) {
    return new ResponseEntity<>(
        categoryService.updateEntityById(id, categoryDto.toEntity()).toDto(), HttpStatus.OK);
  }

  @PatchMapping("/{id}")
  public ResponseEntity<CategoryDto> updateFieldsInCategoryById(
      @PathVariable UUID id, @RequestBody Map<Object, Object> fields) {
    return new ResponseEntity<>(
        categoryService.updateFieldsInEntityById(id, fields).toDto(), HttpStatus.OK);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<HttpStatus> deleteCategoryById(@PathVariable UUID id) {
    categoryService.deleteEntityById(id);
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
