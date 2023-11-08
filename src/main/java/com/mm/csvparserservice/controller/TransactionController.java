package com.mm.csvparserservice.controller;

import com.mm.csvparserservice.dto.TransactionDto;
import com.mm.csvparserservice.service.ReportService;
import com.mm.csvparserservice.service.TransactionService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
public class TransactionController {
  private final TransactionService transactionService;
  private final ReportService reportService;

  @GetMapping
  public ResponseEntity<List<TransactionDto>> getAllTransactions() {
    return new ResponseEntity<>(transactionService.getAllTransactionDtos(), HttpStatus.OK);
  }

  @GetMapping("/generate-report")
  public ResponseEntity<String> generateGoogleSheetsReport() {
    reportService.generateReport();
    return new ResponseEntity<>("Successfully generated", HttpStatus.OK);
  }

  @PostMapping("/upload-statement")
  public ResponseEntity<String> uploadCSVStatement() {
    transactionService.parseCSV("filepath"); // Todo: replace with request params or body
    return new ResponseEntity<>("Successfully uploaded", HttpStatus.OK);
  }
}
