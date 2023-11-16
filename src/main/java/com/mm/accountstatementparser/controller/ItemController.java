package com.mm.accountstatementparser.controller;

import com.mm.accountstatementparser.dto.ItemDto;
import com.mm.accountstatementparser.entity.Item;
import com.mm.accountstatementparser.service.ItemService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/report-items/")
@RequiredArgsConstructor
public class ItemController {
  private final ItemService itemService;

  @GetMapping
  public ResponseEntity<List<ItemDto>> getAllItems() {
    return new ResponseEntity<>(
        itemService.getAllItems().stream().map(Item::toDto).toList(),
        HttpStatus.OK);
  }

  @GetMapping("{id}")
  public ResponseEntity<ItemDto> getItemById(@PathVariable UUID id) {
    return new ResponseEntity<>(itemService.getItemById(id).toDto(), HttpStatus.OK);
  }

  @PostMapping
  public ResponseEntity<ItemDto> createItem(@RequestBody ItemDto itemDto) {
    return new ResponseEntity<>(
        itemService.persistItem(itemDto.toEntity()).toDto(), HttpStatus.CREATED);
  }

  @PutMapping("{id}")
  public ResponseEntity<ItemDto> updateItemById(
      @PathVariable UUID id, @RequestBody ItemDto itemDto) {
    return new ResponseEntity<>(
        itemService.updateItemById(id, itemDto.toEntity()).toDto(),
        HttpStatus.OK);
  }

  @DeleteMapping("{id}")
  public ResponseEntity<HttpStatus> deleteItemById(@PathVariable UUID id) {
    itemService.deleteItemById(id);
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
