package com.mm.accountstatementparser.controller;

import com.mm.accountstatementparser.dto.command.AssignCategoryItemCommandDto;
import com.mm.accountstatementparser.dto.entityDto.TransactionDto;
import com.mm.accountstatementparser.dto.result.TransactionProcessResultDto;
import com.mm.accountstatementparser.entity.Transaction;
import com.mm.accountstatementparser.service.TransactionService;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/transaction")
@RequiredArgsConstructor
public class TransactionController extends CrudEntityController<TransactionDto> {
  private final TransactionService transactionService;

  @Override
  @GetMapping
  public ResponseEntity<List<TransactionDto>> getAll() {
    return new ResponseEntity<>(
        transactionService.getAll().stream().map(Transaction::toDto).toList(), HttpStatus.OK);
  }

  @Override
  @GetMapping("/{id}")
  public ResponseEntity<TransactionDto> getEntityById(@PathVariable UUID id) {
    return new ResponseEntity<>(transactionService.getEntityById(id).toDto(), HttpStatus.OK);
  }

  @Override
  @PostMapping
  public ResponseEntity<TransactionDto> createEntity(@RequestBody TransactionDto requestDto) {
    TransactionProcessResultDto result =
        transactionService.processTransaction(requestDto.toEntity());

    HttpStatus httpStatus = HttpStatus.CREATED;
    if (result.getId() == null) httpStatus = HttpStatus.ACCEPTED;

    return new ResponseEntity<>(result, httpStatus);
  }

  @Override
  @PutMapping("/{id}")
  public ResponseEntity<TransactionDto> updateEntityById(
      @PathVariable UUID id, @RequestBody TransactionDto requestDto) {
    return new ResponseEntity<>(
        transactionService.updateEntityById(id, requestDto.toEntity()).toDto(), HttpStatus.OK);
  }

  @Override
  @PatchMapping("/{id}")
  public ResponseEntity<TransactionDto> updateEntityFieldsById(
      @PathVariable UUID id, @RequestBody Map<Object, Object> fields) {
    return new ResponseEntity<>(
        transactionService.updateEntityFieldsById(id, fields).toDto(), HttpStatus.OK);
  }

  @Override
  @DeleteMapping("/{id}")
  public ResponseEntity<HttpStatus> deleteEntityById(@PathVariable UUID id) {
    transactionService.deleteEntityById(id);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @PutMapping("/assign")
  public ResponseEntity<List<TransactionDto>> assignTransactionsToCategoryItemsById(
      @RequestBody List<AssignCategoryItemCommandDto> assignCategoryItemCommandDtos) {
    return new ResponseEntity<>(
        transactionService.assignTransactionsToCategoryItems(assignCategoryItemCommandDtos).stream()
            .map(Transaction::toDto)
            .toList(),
        HttpStatus.OK);
  }

  @PutMapping("/re-assign")
  public ResponseEntity<List<TransactionDto>> reassignUnassignedTransactions() {
    return new ResponseEntity<>(
        transactionService.reassignAllUnassignedTransactions().stream()
            .map(Transaction::toDto)
            .toList(),
        HttpStatus.OK);
  }

//  Internal-Only
  @DeleteMapping
  public ResponseEntity<HttpStatus> deleteAll() {
    transactionService.deleteAll();
    return new ResponseEntity<>(HttpStatus.OK);
  }

}
