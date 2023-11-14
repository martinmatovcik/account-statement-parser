package com.mm.accountstatementparser.controller;

import com.mm.accountstatementparser.dto.TransactionDto;
import com.mm.accountstatementparser.entity.Transaction;
import com.mm.accountstatementparser.service.TransactionService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
public class TransactionController {
  private final TransactionService transactionService;

  @GetMapping
  public ResponseEntity<List<TransactionDto>> getAllTransactions() {
    return new ResponseEntity<>(
        transactionService.getAllTransactions().stream().map(Transaction::toDto).toList(),
        HttpStatus.OK);
  }

  @GetMapping("/{id}")
  public ResponseEntity<TransactionDto> getTransactionById(@PathVariable UUID id) {
    return new ResponseEntity<>(transactionService.getTransactionById(id).toDto(), HttpStatus.OK);
  }

  @PostMapping
  public ResponseEntity<TransactionDto> createTransaction(
      @RequestBody TransactionDto transactionDto) {
    return new ResponseEntity<>(
        transactionService.persistTransaction(transactionDto.toEntity()).toDto(),
        HttpStatus.CREATED);
  }

  @PutMapping("/{id}")
  public ResponseEntity<TransactionDto> updateTransactionById(
      @PathVariable UUID id, @RequestBody TransactionDto transactionDto) {
    return new ResponseEntity<>(
        transactionService.updateTransaction(id, transactionDto.toEntity()).toDto(), HttpStatus.OK);
  }

//  todo: PATCH mapping

  @DeleteMapping("/{id}")
  public ResponseEntity<HttpStatus> deleteTransactionById(@PathVariable UUID id) {
    transactionService.deleteTransactionById(id);
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
