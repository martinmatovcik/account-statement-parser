package com.mm.csvparserservice.controller;

import com.mm.csvparserservice.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Month;

@RestController
@RequestMapping("/api/v1/report")
@RequiredArgsConstructor
public class ReportController {
    private final ReportService reportService;
    @GetMapping
    public ResponseEntity<String> generateGoogleSheetsReport(@RequestParam int month) {
        reportService.generateReport(Month.of(month));
        return new ResponseEntity<>("Successfully generated", HttpStatus.OK);
    }
}
