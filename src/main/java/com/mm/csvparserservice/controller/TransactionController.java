package com.mm.csvparserservice.controller;

import com.mm.csvparserservice.dto.TransactionDto;
import com.mm.csvparserservice.service.TransactionService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
public class TransactionController {
  private final TransactionService transactionService;

  @GetMapping
  public ResponseEntity<List<TransactionDto>> getAllTransactions() {
    return new ResponseEntity<>(transactionService.getAllTransactions(), HttpStatus.OK);
  }
}
