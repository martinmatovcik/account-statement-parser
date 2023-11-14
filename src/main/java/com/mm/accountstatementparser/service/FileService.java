package com.mm.accountstatementparser.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {
  void parseFile(MultipartFile file);
}
