package com.mm.csvparserservice.service;

import com.mm.csvparserservice.dto.TransactionDto;
import com.mm.csvparserservice.model.Transaction;
import com.mm.csvparserservice.repository.TransactionRepository;
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
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

  private final TransactionRepository transactionRepository;

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

  @Override
  public List<TransactionDto> parseCSV(String file) {

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
  public List<TransactionDto> getAllTransactionDtos() {
    return transactionRepository.findAll().stream()
        .map(Transaction::toDto)
        .collect(Collectors.toList());
  }

  @Override
  public List<List<Object>> getAllTransactionDtosAsData() {
    return getAllTransactionDtos().stream()
        .map(TransactionDto::toData)
        .collect(Collectors.toList());
  }

  private List<String> readFileAndGetFileLines(String file) {
    try {
      return Files.readAllLines(Path.of("file/fio-10-23.csv")); // dummy value for now
      //      return Files.readAllLines(Path.of(file));
    } catch (IOException e) {
      throw new RuntimeException("Something went wrong with reading the file", e);
    }
  }

  private Transaction getTransactionFromFileLine(String fileLine) {
    fileLine = fileLine.replace("\"", "");
    String[] fileData = fileLine.split(";");
    Transaction transaction = Transaction.builder()
        .fioOperationId(Long.parseLong(fileData[0]))
        .date(convertStringToLocalDate(fileData[1]))
        .amount(convertStringToBigDecimal(fileData[2]))
        .currency(Currency.getInstance(fileData[3]))
        .recipientAccount(fileData[4])
        .recipientAccountName(fileData[5])
        .bankCode(Long.parseLong(makeNullable(fileData[6])))
        .bankName(fileData[7])
        .constantSymbol(fileData[8])
        .variableSymbol(fileData[9])
        .specificSymbol(fileData[10])
        .transactionNote(fileData[11])
        .recipientMessage(fileData[12])
        .transactionType(fileData[13])
        .carriedOut(fileData[14])
        .transactionSpecification(fileData[15])
        .note(fileData[16])
        .bicCode(fileData[17])
        .fioInstructionId(Long.parseLong(makeNullable(fileData[18])))
        .build();
    transaction.setMainCategory(transaction.findMainCategory());
    return transaction;
  }
}
