package com.mm.accountstatementparser;

import com.mm.accountstatementparser.entity.ReportItem;
import com.mm.accountstatementparser.entity.TransactionMainCategory;
import com.mm.accountstatementparser.service.ReportItemService;
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@RequiredArgsConstructor
public class AccountStatementParserApplication implements CommandLineRunner {
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

  @Override
  public void run(String... args) throws Exception {
    if (true) {
      List<ReportItem> reportItems =
          List.of(
              new ReportItem("Nájom", BigDecimal.valueOf(17300.00), TransactionMainCategory.NEEDS),
              new ReportItem(
                  "Elektrina", BigDecimal.valueOf(1000.00), TransactionMainCategory.NEEDS),
              new ReportItem("Internet", BigDecimal.valueOf(300.00), TransactionMainCategory.NEEDS),
              new ReportItem("Telefóny", BigDecimal.valueOf(960.00), TransactionMainCategory.NEEDS),
              new ReportItem("Lítačky", BigDecimal.valueOf(680.00), TransactionMainCategory.NEEDS),
              new ReportItem("Jedlo", BigDecimal.valueOf(10000.00), TransactionMainCategory.NEEDS),
              new ReportItem(
                  "Greenfox - Mišovci", BigDecimal.valueOf(0.00), TransactionMainCategory.LOANS),
              new ReportItem(
                  "Bývanie - rodičia", BigDecimal.valueOf(2500.00), TransactionMainCategory.LOANS),
              new ReportItem(
                  "Oblečenie", BigDecimal.valueOf(0.00), TransactionMainCategory.FUN_WANTS_GIFTS),
              new ReportItem(
                  "Netflix", BigDecimal.valueOf(120.00), TransactionMainCategory.FUN_WANTS_GIFTS),
              new ReportItem(
                  "Spotify", BigDecimal.valueOf(60.00), TransactionMainCategory.FUN_WANTS_GIFTS),
              new ReportItem(
                  "Kultúra", BigDecimal.valueOf(0.00), TransactionMainCategory.FUN_WANTS_GIFTS),
              new ReportItem(
                  "Rande", BigDecimal.valueOf(0.00), TransactionMainCategory.FUN_WANTS_GIFTS),
              new ReportItem(
                  "Eating out", BigDecimal.valueOf(0.00), TransactionMainCategory.FUN_WANTS_GIFTS),
              new ReportItem(
                  "Cesty na slovensko",
                  BigDecimal.valueOf(0.00),
                  TransactionMainCategory.FUN_WANTS_GIFTS),
              new ReportItem(
                  "Charita", BigDecimal.valueOf(0.00), TransactionMainCategory.FUN_WANTS_GIFTS),
              new ReportItem("Dôchodok", BigDecimal.valueOf(0.00), TransactionMainCategory.SAVINGS),
              new ReportItem(
                  "Krátkodobé", BigDecimal.valueOf(0.00), TransactionMainCategory.SAVINGS),
              new ReportItem(
                  "Finančná rezerva", BigDecimal.valueOf(0.00), TransactionMainCategory.SAVINGS),
              new ReportItem("", BigDecimal.valueOf(0.00), TransactionMainCategory.SAVINGS),
              new ReportItem("", BigDecimal.valueOf(0.00), TransactionMainCategory.SAVINGS),
              new ReportItem("", BigDecimal.valueOf(0.00), TransactionMainCategory.SAVINGS),
              new ReportItem("Neznáme", BigDecimal.valueOf(0.00), TransactionMainCategory.OTHERS));

      for (ReportItem reportItem : reportItems) {
        reportItemService.persistReportItem(reportItem);
      }
    }
  }
}
