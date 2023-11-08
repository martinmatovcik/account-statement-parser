package com.mm.csvparserservice.service;

import com.mm.csvparserservice.model.Transaction;
import com.mm.csvparserservice.repository.TransactionRepository;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.poi.hssf.usermodel.*;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
public class ReportServiceImplExcel implements ReportService{
    private final TransactionRepository transactionRepository;
    @Override
    public void generateReport(HttpServletResponse httpServletResponse) {
        List<Transaction> transactions = transactionRepository.findAll();
        String[] metaData = {
                "Transaction Id",
                "Fio Operation Id",
                "Date",
                "Amount",
                "Currency",
                "Recipient Account",
                "Recipient Account Name",
                "Bank Code",
                "Bank Name",
                "Constant Symbol",
                "Variable Symbol",
                "Specific Symbol",
                "Transaction Note",
                "Recipient Message",
                "Transaction Type",
                "Carried Out",
                "Transaction Specification",
                "BIC Code",
                "Fio Instruction Id"
        };

        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("Raw-Data");

        HSSFRow headerRow = sheet.createRow(0);
        headerRow.setRowStyle(setFontBold(workbook, true));
        createCellsInRow(headerRow, metaData);

        setFontBold(workbook, false);

        ServletOutputStream outputStream = null;
        try {
            outputStream = httpServletResponse.getOutputStream();
            workbook.write(outputStream);
            workbook.close();
            outputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private HSSFCellStyle setFontBold(HSSFWorkbook workbook, boolean makeBold) {
        HSSFCellStyle cellStyle = workbook.createCellStyle();
        HSSFFont boldFont = workbook.createFont();
        boldFont.setBold(makeBold);
        cellStyle.setFont(boldFont);

        return cellStyle;
    }

    private void createCellsInRow(HSSFRow row, String[] data) {
        for (int i = 0; i < data.length; i++) {
            row.createCell(i).setCellValue(data[i]);
        }
    }
}
