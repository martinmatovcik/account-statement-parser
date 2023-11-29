package com.mm.accountstatementparser.controller;

import com.mm.accountstatementparser.dto.entityDto.CategoryItemDto;
import com.mm.accountstatementparser.entity.CategoryItem;
import com.mm.accountstatementparser.service.CategoryItemService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/category-item")
@RequiredArgsConstructor
public class CategoryItemController {
  private final CategoryItemService categoryItemService;

  @GetMapping
  public ResponseEntity<List<CategoryItemDto>> getAllCategoryItems() {
    return new ResponseEntity<>(
        categoryItemService.getAllCategoryItems().stream().map(CategoryItem::toDto).toList(),
        HttpStatus.OK);
  }

  @GetMapping("/{id}")
  public ResponseEntity<CategoryItemDto> getCategoryItemById(@PathVariable UUID id) {
    return new ResponseEntity<>(categoryItemService.getCategoryItemById(id).toDto(), HttpStatus.OK);
  }

  @PostMapping
  public ResponseEntity<CategoryItemDto> createCategoryItem(@RequestBody CategoryItemDto categoryItemDto) {
    return new ResponseEntity<>(
        categoryItemService.persistCategoryItem(categoryItemDto.toEntity()).toDto(), HttpStatus.CREATED);
  }

  @PutMapping("/{id}")
  public ResponseEntity<CategoryItemDto> updateCategoryItemById(
      @PathVariable UUID id, @RequestBody CategoryItemDto categoryItemDto) {
    return new ResponseEntity<>(
        categoryItemService.updateCategoryItemById(id, categoryItemDto.toEntity()).toDto(),
        HttpStatus.OK);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<HttpStatus> deleteCategoryItemById(@PathVariable UUID id) {
    categoryItemService.deleteCategoryItemById(id);
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
