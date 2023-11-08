package com.mm.csvparserservice.service;

import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletResponse;

public interface ReportService {
  void generateReport(@Nullable HttpServletResponse httpServletResponse);
}
