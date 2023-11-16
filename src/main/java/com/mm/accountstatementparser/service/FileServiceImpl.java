package com.mm.accountstatementparser.service;

import com.mm.accountstatementparser.entity.Balance;
import com.mm.accountstatementparser.entity.BalanceCategory;
import com.mm.accountstatementparser.entity.Transaction;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.Month;
import java.util.Currency;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {
  private final TransactionService transactionService;
  private final BalanceService balanceService;

  @Override
  public void parseFile(MultipartFile file) {

    List<String> fileLines;
    try {
      fileLines = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8)).lines().toList();
    } catch (IOException e) {
      throw new RuntimeException("Something went wrong with reading the file", e);
    }

    balanceService.persistBalance(getBalanceFromSpecialFileLine(fileLines.get(4)));
    balanceService.persistBalance(getBalanceFromSpecialFileLine(fileLines.get(5)));

    int numberOfHeaderLines = 10;
    for (int i = numberOfHeaderLines; i < fileLines.size(); i++) {
      transactionService.persistTransaction(getTransactionFromFileLine(fileLines.get(i))).toDto();
    }
  }

  private Balance getBalanceFromSpecialFileLine(String fileLine) {
    String[] data = fileLine.split(":");

    Month month = convertStringToLocalDate(data[0].substring(data[0].length() - 10)).getMonth();

    BigDecimal amount =
        convertStringToBigDecimal(data[1].replace(" ", "").replace("\"", "").replace("CZK", ""));

    BalanceCategory balanceCategory =
        (data[0].contains("Počáteční stav účtu"))
            ? BalanceCategory.INITIAL_BALANCE
            : BalanceCategory.FINAL_BALANCE;

    return Balance.builder().month(month).amount(amount).balanceCategory(balanceCategory).build();
  }

  private Transaction getTransactionFromFileLine(String fileLine) {
    fileLine = fileLine.replace("\"", "");
    String[] fileData = fileLine.split(";");
    Transaction transaction =
        Transaction.builder()
            .date(convertStringToLocalDate(fileData[1]))
            .amount(convertStringToBigDecimal(fileData[2]))
            .currency(Currency.getInstance(fileData[3]))
            .variableSymbol(fileData[9])
            .recipientMessage(fileData[12])
            .transactionNote(fileData[16])
            .build();
    transaction.setCategory(transaction.findMainCategory());
    return transaction;
  }

  private static String makeNullable(String string) {
    return Objects.equals(string, "") ? "0" : string;
  }

  private static BigDecimal convertStringToBigDecimal(String number) {
    try {
      if (number.contains(",")) number = number.replace(",", ".");
      return new BigDecimal(number);
    } catch (NumberFormatException e) {
      throw new RuntimeException("Number format can not be parsed to BigDecimal.", e);
    }
  }

  private static LocalDate convertStringToLocalDate(String stringDate) {
    String year = stringDate.substring(6, 10);
    String month = stringDate.substring(3, 5);
    String day = stringDate.substring(0, 2);
    return LocalDate.of(
        Integer.parseInt(stringDate.substring(6, 10)),
        Integer.parseInt(stringDate.substring(3, 5)),
        Integer.parseInt(stringDate.substring(0, 2)));
  }
}
