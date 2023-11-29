package com.mm.accountstatementparser.controller;

import com.mm.accountstatementparser.dto.command.AssignCategoryCommandDto;
import com.mm.accountstatementparser.dto.command.AssignCategoryItemCommandDto;
import com.mm.accountstatementparser.dto.entityDto.CategoryItemDto;
import com.mm.accountstatementparser.dto.entityDto.TransactionDto;
import com.mm.accountstatementparser.entity.CategoryItem;
import com.mm.accountstatementparser.entity.Transaction;
import com.mm.accountstatementparser.service.CategoryItemService;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/category-item")
@RequiredArgsConstructor
public class CategoryItemController extends CrudEntityController<CategoryItemDto> {
  private final CategoryItemService categoryItemService;

  @Override
  @GetMapping
  public ResponseEntity<List<CategoryItemDto>> getAll() {
    return new ResponseEntity<>(
        categoryItemService.getAll().stream().map(CategoryItem::toDto).toList(), HttpStatus.OK);
  }

  @Override
  @GetMapping("/{id}")
  public ResponseEntity<CategoryItemDto> getEntityById(@PathVariable UUID id) {
    return new ResponseEntity<>(categoryItemService.getEntityById(id).toDto(), HttpStatus.OK);
  }

  @Override
  @PostMapping
  public ResponseEntity<CategoryItemDto> createEntity(@RequestBody CategoryItemDto requestDto) {
    return new ResponseEntity<>(
        categoryItemService.persistEntity(requestDto.toEntity()).toDto(), HttpStatus.CREATED);
  }

  @Override
  @PutMapping("/{id}")
  public ResponseEntity<CategoryItemDto> updateEntityById(
      @PathVariable UUID id, @RequestBody CategoryItemDto requestDto) {
    return new ResponseEntity<>(
        categoryItemService.updateEntityById(id, requestDto.toEntity()).toDto(), HttpStatus.OK);
  }

  @Override
  @PatchMapping("/{id}")
  public ResponseEntity<CategoryItemDto> updateEntityFieldsById(
      @PathVariable UUID id, @RequestBody Map<Object, Object> fields) {
    return new ResponseEntity<>(
        categoryItemService.updateEntityFieldsById(id, fields).toDto(), HttpStatus.OK);
  }

  @Override
  @DeleteMapping("/{id}")
  public ResponseEntity<HttpStatus> deleteEntityById(@PathVariable UUID id) {
    categoryItemService.deleteEntityById(id);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @PutMapping("/assign")
  public ResponseEntity<List<CategoryItemDto>> assignCategoryItemToCategoryById(
          @RequestBody List<AssignCategoryCommandDto> assignCategoryCommandDtos) {
    return new ResponseEntity<>(
            categoryItemService.assignCategoryItemToCategoryById(assignCategoryCommandDtos).stream()
                    .map(CategoryItem::toDto)
                    .toList(),
            HttpStatus.OK);
  }
}
