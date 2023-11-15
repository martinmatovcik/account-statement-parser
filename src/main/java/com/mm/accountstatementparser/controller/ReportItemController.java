package com.mm.accountstatementparser.controller;

import com.mm.accountstatementparser.dto.ReportItemDto;
import com.mm.accountstatementparser.entity.ReportItem;
import com.mm.accountstatementparser.service.ReportItemService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/report-items/")
@RequiredArgsConstructor
public class ReportItemController {
  private final ReportItemService reportItemService;

  @GetMapping
  public ResponseEntity<List<ReportItemDto>> getAllReportItems() {
    return new ResponseEntity<>(
        reportItemService.getAllReportItems().stream().map(ReportItem::toDto).toList(),
        HttpStatus.OK);
  }

  @GetMapping("{id}")
  public ResponseEntity<ReportItemDto> getReportItemById(@PathVariable UUID id) {
    return new ResponseEntity<>(reportItemService.getReportItemById(id).toDto(), HttpStatus.OK);
  }

  @PostMapping
  public ResponseEntity<ReportItemDto> createReportItem(@RequestBody ReportItemDto reportItemDto) {
    return new ResponseEntity<>(
        reportItemService.persistReportItem(reportItemDto.toEntity()).toDto(), HttpStatus.CREATED);
  }

  @PutMapping("{id}")
  public ResponseEntity<ReportItemDto> updateReportItemById(
      @PathVariable UUID id, @RequestBody ReportItemDto reportItemDto) {
    return new ResponseEntity<>(
        reportItemService.updateReportItemById(id, reportItemDto.toEntity()).toDto(),
        HttpStatus.OK);
  }

  @DeleteMapping("{id}")
  public ResponseEntity<HttpStatus> deleteReportItemById(@PathVariable UUID id) {
    reportItemService.deleteReportItemById(id);
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
