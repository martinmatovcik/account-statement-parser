package com.mm.csvparserservice.controller;

import com.mm.csvparserservice.dto.TransactionDto;
import com.mm.csvparserservice.service.CSVService;
import com.mm.csvparserservice.service.ReportService;
import com.mm.csvparserservice.service.TransactionService;
import java.time.Month;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
public class TransactionController {
  private final TransactionService transactionService;
  private final ReportService reportService;
  private final CSVService csvService;

  @GetMapping
  public ResponseEntity<List<TransactionDto>> getAllTransactions() {
    return new ResponseEntity<>(transactionService.getAllTransactionDtos(), HttpStatus.OK);
  }

  @GetMapping("/generate-report")
  public ResponseEntity<String> generateGoogleSheetsReport(@RequestParam int month) {
    reportService.generateReport(Month.of(month));
    return new ResponseEntity<>("Successfully generated", HttpStatus.OK);
  }

  @PostMapping("/upload-statement")
  public ResponseEntity<String> uploadCSVStatement() {
    // Todo: replace "filepath" with request params or body
    csvService.parseCSV("file");
    return new ResponseEntity<>("Successfully uploaded", HttpStatus.OK);
  }
}
