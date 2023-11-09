package com.mm.csvparserservice.service;

import com.mm.csvparserservice.dto.TransactionDto;

import java.util.List;

public interface CSVService {
    void parseCSV(String filePath);
}
