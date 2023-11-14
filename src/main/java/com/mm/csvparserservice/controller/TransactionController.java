package com.mm.csvparserservice.controller;

import com.mm.csvparserservice.dto.TransactionDto;
import com.mm.csvparserservice.entity.Transaction;
import com.mm.csvparserservice.service.CSVService;
import com.mm.csvparserservice.service.TransactionService;
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
  private final CSVService csvService;

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
  public ResponseEntity<TransactionDto> updateTransaction(@PathVariable UUID id,
      @RequestBody TransactionDto transactionDto) {
    return new ResponseEntity<>(
        transactionService.updateTransaction(id, transactionDto.toEntity()).toDto(), HttpStatus.OK);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<HttpStatus> deleteTransactionById(@PathVariable UUID id) {
    transactionService.deleteTransactionById(id);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @PostMapping("/upload-statement")
  public ResponseEntity<HttpStatus> uploadCSVStatement() {
    // Todo: replace "filepath" with request params or body
    csvService.parseCSV("file");
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
