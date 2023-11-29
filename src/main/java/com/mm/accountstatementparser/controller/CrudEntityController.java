package com.mm.accountstatementparser.controller;

import com.mm.accountstatementparser.dto.entityDto.DtoParent;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

public abstract class CrudEntityController<T extends DtoParent<?>> {
  @GetMapping
  public abstract ResponseEntity<List<T>> getAll();

  @GetMapping("/{id}")
  public abstract ResponseEntity<T> getEntityById(@PathVariable UUID id);

  @PostMapping
  public abstract ResponseEntity<T> createEntity(@RequestBody T requestDto);

  @PutMapping("/{id}")
  public abstract ResponseEntity<T> updateEntityById(
      @PathVariable UUID id, @RequestBody T requestDto);

  @PatchMapping("/{id}")
  public abstract ResponseEntity<T> updateEntityFieldsById(
      @PathVariable UUID id, @RequestBody Map<Object, Object> fields);

  @DeleteMapping("/{id}")
  public abstract ResponseEntity<HttpStatus> deleteEntityById(@PathVariable UUID id);
}
