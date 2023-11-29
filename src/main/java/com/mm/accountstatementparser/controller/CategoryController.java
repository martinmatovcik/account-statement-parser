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
public class CategoryController extends CrudEntityController<CategoryDto> {
  private final CategoryService categoryService;

  @Override
  @GetMapping
  public ResponseEntity<List<CategoryDto>> getAll() {
    return new ResponseEntity<>(
        categoryService.getAll().stream().map(Category::toDto).toList(), HttpStatus.OK);
  }

  @Override
  @GetMapping("/{id}")
  public ResponseEntity<CategoryDto> getEntityById(@PathVariable UUID id) {
    return new ResponseEntity<>(categoryService.getEntityById(id).toDto(), HttpStatus.OK);
  }

  @Override
  @PostMapping
  public ResponseEntity<CategoryDto> createEntity(@RequestBody CategoryDto requestDto) {
    return new ResponseEntity<>(
        categoryService.persistEntity(requestDto.toEntity()).toDto(), HttpStatus.CREATED);
  }

  @Override
  @PutMapping("/{id}")
  public ResponseEntity<CategoryDto> updateEntityById(
      @PathVariable UUID id, @RequestBody CategoryDto requestDto) {
    return new ResponseEntity<>(
        categoryService.updateEntityById(id, requestDto.toEntity()).toDto(), HttpStatus.OK);
  }

  @Override
  @PatchMapping("/{id}")
  public ResponseEntity<CategoryDto> updateEntityFieldsById(
      @PathVariable UUID id, @RequestBody Map<Object, Object> fields) {
    return new ResponseEntity<>(
        categoryService.updateEntityFieldsById(id, fields).toDto(), HttpStatus.OK);
  }

  @Override
  @DeleteMapping("/{id}")
  public ResponseEntity<HttpStatus> deleteEntityById(@PathVariable UUID id) {
    categoryService.deleteEntityById(id);
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
