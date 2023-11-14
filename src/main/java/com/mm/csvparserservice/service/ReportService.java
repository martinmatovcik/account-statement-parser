package com.mm.csvparserservice.service;

import com.mm.csvparserservice.entity.ReportItem;
import java.time.Month;
import java.util.List;

public interface ReportService {
  void generateReport(Month month);
}
