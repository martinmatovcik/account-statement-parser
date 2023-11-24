package com.mm.accountstatementparser.service;

import com.mm.accountstatementparser.dto.result.TransactionProcessResultDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileService {
  List<TransactionProcessResultDto> parseFile(MultipartFile file);
}
