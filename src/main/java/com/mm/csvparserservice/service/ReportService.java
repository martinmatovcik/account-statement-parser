package com.mm.csvparserservice.service;

import java.time.Month;

public interface ReportService {
  void generateReport(Month month);
  void generateTemplate(Month month);
}
