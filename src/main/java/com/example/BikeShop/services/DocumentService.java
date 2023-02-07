package com.example.BikeShop.services;

import com.example.BikeShop.models.Cheque;
import com.example.BikeShop.models.Product;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class DocumentService {

    private final XSSFWorkbook xssfWorkbook;

    private final XWPFDocument xwpfDocument;

    private XSSFSheet sheet;

    private XWPFTable table;

    private final List<Cheque> chequeList;

    public DocumentService(List<Cheque> chequeList) {
        this.chequeList = chequeList;
        xssfWorkbook = new XSSFWorkbook();
        xwpfDocument = new XWPFDocument();
        table = xwpfDocument.createTable();
    }

    private void createCellExcel(Row row, int columnCount, Object value, CellStyle style) {
        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
        if (value instanceof Integer)
            cell.setCellValue((Integer) value);
        else if (value instanceof Boolean)
            cell.setCellValue((Boolean) value);
        else
            cell.setCellValue(value.toString());
        cell.setCellStyle(style);
    }

    private void writeHeaderLineExcel() {
        sheet = xssfWorkbook.createSheet("Cheque");
        Row row = sheet.createRow(0);
        CellStyle style = xssfWorkbook.createCellStyle();
        XSSFFont font = xssfWorkbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);
        createCellExcel(row, 0, "Дата продажи", style);
        createCellExcel(row, 1, "Товар", style);
        createCellExcel(row, 2, "Категория товара", style);
        createCellExcel(row, 3, "Цена за единицу товара", style);
        createCellExcel(row, 4, "Количество товара", style);
        createCellExcel(row, 5, "Итоговая цена", style);
    }

    private void writeDataLinesExcel() {
        int rowCount = 1;
        CellStyle style = xssfWorkbook.createCellStyle();
        XSSFFont font = xssfWorkbook.createFont();
        font.setFontHeight(14);
        style.setFont(font);
        for (Cheque cheque : chequeList) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;
            Product product = cheque.getProduct();
            String fullPrice = String.valueOf(product.getPrice() * cheque.getCount());
            createCellExcel(row, columnCount++, cheque.getChequeInfo().getDatePrint(), style);
            createCellExcel(row, columnCount++, product.getName(), style);
            createCellExcel(row, columnCount++, product.getCategory().getName(), style);
            createCellExcel(row, columnCount++, product.getPrice(), style);
            createCellExcel(row, columnCount++, cheque.getCount(), style);
            createCellExcel(row, columnCount, fullPrice, style);
        }
    }

    public void exportExcel(String currentDateTime) throws IOException {
        writeHeaderLineExcel();
        writeDataLinesExcel();
        OutputStream outputStream = Files.newOutputStream(Paths.get("files/cheque_" + currentDateTime + ".xlsx"));
        xssfWorkbook.write(outputStream);
        xssfWorkbook.close();
        outputStream.close();
    }

    private void writeHeaderLineWord() {
        XWPFTableRow tableRowOne = table.getRow(0);
        tableRowOne.getCell(0).setText("Дата продажи");
        tableRowOne.addNewTableCell().setText("Товар");
        tableRowOne.addNewTableCell().setText("Категория товара");
        tableRowOne.addNewTableCell().setText("Цена за единицу товара");
        tableRowOne.addNewTableCell().setText("Количество товара");
        tableRowOne.addNewTableCell().setText("Итоговая цена");
    }

    private void writeDataLinesWord() {
        CellStyle style = xssfWorkbook.createCellStyle();
        XSSFFont font = xssfWorkbook.createFont();
        font.setFontHeight(14);
        style.setFont(font);
        for (Cheque cheque : chequeList) {
            XWPFTableRow tableRow = table.createRow();
            int columnCount = 0;
            Product product = cheque.getProduct();
            String fullPrice = String.valueOf(product.getPrice() * cheque.getCount());
            tableRow.getCell(columnCount++).setText(String.valueOf(cheque.getChequeInfo().getDatePrint()));
            tableRow.getCell(columnCount++).setText(String.valueOf(product.getCategory().getName()));
            tableRow.getCell(columnCount++).setText(String.valueOf(product.getName()));
            tableRow.getCell(columnCount++).setText(String.valueOf(product.getPrice()));
            tableRow.getCell(columnCount++).setText(String.valueOf(cheque.getCount()));
            tableRow.getCell(columnCount).setText(fullPrice);
        }
    }

    public void exportWord(String currentDateTime) throws IOException {
        writeHeaderLineWord();
        writeDataLinesWord();
        FileOutputStream fileOutputStream = new FileOutputStream("files/cheque_" + currentDateTime + ".docx");
        xwpfDocument.write(fileOutputStream);
        fileOutputStream.close();
        xwpfDocument.close();
    }
}
