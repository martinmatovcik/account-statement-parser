package com.mm.csvparserservice.service;

import com.mm.csvparserservice.dto.TransactionDto;
import com.mm.csvparserservice.model.Transaction;
import com.mm.csvparserservice.repository.TransactionRepository;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.apache.poi.hssf.usermodel.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

  private final TransactionRepository transactionRepository;

  @Override
  public List<TransactionDto> getTransactionDtoListFromCSV(String file) {

    List<String> fileLines = readFileAndGetFileLines(file);
    List<TransactionDto> transactions = new ArrayList<>();

    int numberOfHeaderLines = 10;
    for (int i = numberOfHeaderLines; i < fileLines.size(); i++) {
      transactions.add(persistTransaction(getTransactionFromFileLine(fileLines.get(i))).toDto());
    }

    return transactions;
  }

  @Override
  public Transaction persistTransaction(Transaction transaction) {
    return transactionRepository.save(transaction);
  }

  @Override
  public List<TransactionDto> getAllTransactions() {
    return transactionRepository.findAll().stream()
        .map(Transaction::toDto)
        .collect(Collectors.toList());
  }

  private List<String> readFileAndGetFileLines(String file) {
    try {
      return Files.readAllLines(Path.of("file/fio-example.csv")); // dummy value for now
      //      return Files.readAllLines(Path.of(file));
    } catch (IOException e) {
      throw new RuntimeException("Something went wrong with reading the file", e);
    }
  }

  private Transaction getTransactionFromFileLine(String fileLine) {
    //    fileLine = fileLine.substring(1, fileLine.length() - 1);
    //    String[] fileData = fileLine.split("\";\"");
    fileLine = fileLine.replace("\"", "");
    String[] fileData = fileLine.split(";");
    return new Transaction(
        null,
        Long.parseLong(fileData[0]),
        convertStringToLocalDate(fileData[1]),
        convertStringToBigDecimal(fileData[2]),
        Currency.getInstance(fileData[3]),
        fileData[4],
        fileData[5],
        Long.parseLong(makeNullable(fileData[6])),
        fileData[7],
        fileData[8],
        fileData[9],
        fileData[10],
        fileData[11],
        fileData[12],
        fileData[13],
        fileData[14],
        fileData[15],
        fileData[16],
        Long.parseLong(makeNullable(fileData[17])));
  }

  private static String makeNullable(String string) {
    return Objects.equals(string, "") ? "0" : string;
  }

  private static BigDecimal convertStringToBigDecimal(String number) {
    try {
      return new BigDecimal(number.replace(',', '.'));
    } catch (NumberFormatException e) {
      throw new RuntimeException("Number format can not be parsed to BigDecimal.", e);
    }
  }

  private static LocalDate convertStringToLocalDate(String stringDate) {
    return LocalDate.of(
        Integer.parseInt(stringDate.substring(6, 10)),
        Integer.parseInt(stringDate.substring(3, 5)),
        Integer.parseInt(stringDate.substring(0, 2)));
  }
}
