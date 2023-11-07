package com.mm.csvparserservice.service;

import jakarta.servlet.http.HttpServletResponse;

public interface ReportService {
  void generateReport(HttpServletResponse response);
}
