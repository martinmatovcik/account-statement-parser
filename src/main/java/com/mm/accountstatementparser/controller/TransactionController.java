package com.mm.accountstatementparser.controller;

import com.mm.accountstatementparser.dto.command.AssignItemCommandDto;
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
@RequestMapping("/api/v1/transactions/")
@RequiredArgsConstructor
public class TransactionController {
  private final TransactionService transactionService;

  @GetMapping
  public ResponseEntity<List<TransactionDto>> getAllTransactions() {
    return new ResponseEntity<>(
        transactionService.getAllTransactions().stream().map(Transaction::toDto).toList(),
        HttpStatus.OK);
  }

  @GetMapping("{id}")
  public ResponseEntity<TransactionDto> getTransactionById(@PathVariable UUID id) {
    return new ResponseEntity<>(transactionService.getTransactionById(id).toDto(), HttpStatus.OK);
  }

  @PostMapping
  public ResponseEntity<TransactionProcessResultDto> createTransaction(
      @RequestBody TransactionDto transactionDto) {
    TransactionProcessResultDto result =
        transactionService.processTransaction(transactionDto.toEntity());

    HttpStatus httpStatus = HttpStatus.CREATED;
    if (result.getSavedTransactionDto() == null) httpStatus = HttpStatus.ACCEPTED;

    return new ResponseEntity<>(result, httpStatus);
  }

  @PutMapping("{id}")
  public ResponseEntity<TransactionDto> updateTransactionById(
      @PathVariable UUID id, @RequestBody TransactionDto transactionDto) {
    return new ResponseEntity<>(
        transactionService.updateTransactionById(id, transactionDto.toEntity()).toDto(),
        HttpStatus.OK);
  }

  @PatchMapping("{id}")
  public ResponseEntity<TransactionDto> updateFieldsInTransactionById(
      @PathVariable UUID id, @RequestBody Map<Object, Object> fields) {
    return new ResponseEntity<>(
        transactionService.updateFieldsInTransactionById(id, fields).toDto(), HttpStatus.OK);
  }

  @DeleteMapping("{id}")
  public ResponseEntity<HttpStatus> deleteTransactionById(@PathVariable UUID id) {
    transactionService.deleteTransactionById(id);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @PutMapping("assign-item/") // todo: better mappings e.g. /assign-categoryItem
  public ResponseEntity<List<TransactionDto>> assignItemToTransactionById(
      @RequestBody List<AssignItemCommandDto> assignItemCommandDtos) {
    return new ResponseEntity<>(
        transactionService.assignTransactionToItemById(assignItemCommandDtos).stream()
            .map(Transaction::toDto)
            .toList(),
        HttpStatus.OK);
  }

  @PutMapping("reassign/") // todo: better mappings e.g. /assign-categoryItem
  public ResponseEntity<List<TransactionDto>> reassignUnassignedTransaction() {
    return new ResponseEntity<>(
        transactionService.reassignAllUnassignedTransactionsToItems().stream()
            .map(Transaction::toDto)
            .toList(),
        HttpStatus.OK);
  }
}
