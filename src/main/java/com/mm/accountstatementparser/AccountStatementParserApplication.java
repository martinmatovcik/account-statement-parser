package com.mm.accountstatementparser;

import com.mm.accountstatementparser.service.ReportItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@RequiredArgsConstructor
public class AccountStatementParserApplication {
  private final ReportItemService reportItemService;

  public static void main(String[] args) {
    SpringApplication.run(AccountStatementParserApplication.class, args);

    //    try {
    //      List<String> fileLines = Files.readAllLines(Path.of("file/fio-example.csv");
    ////      for (int i = 0; i < 10; i++) {
    ////        System.out.println(fileLines.get(i);
    ////      }
    //
    //      System.out.println("==============SKIPPED=============";
    //
    //      fileLines.stream().skip(10).forEach(System.out::println);
    //
    //    } catch (IOException e) {
    //      throw new RuntimeException("Something went wrong with reading the file", e);
    //    }
  }
}
