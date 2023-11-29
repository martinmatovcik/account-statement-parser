package com.mm.accountstatementparser.controller;

import com.mm.accountstatementparser.dto.result.TransactionProcessResultDto;
import com.mm.accountstatementparser.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/file")
@RequiredArgsConstructor
public class FileController {
  private final FileService fileService;

  @PostMapping
  public ResponseEntity<List<TransactionProcessResultDto>> uploadFileAndCreateTransaction(@RequestParam MultipartFile file) {
    return new ResponseEntity<>(fileService.parseFile(file), HttpStatus.OK);
  }
}
