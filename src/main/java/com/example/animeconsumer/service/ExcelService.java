package com.example.animeconsumer.service;

import com.example.animeconsumer.domain.Anime;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class ExcelService {

    private static final Logger logger = LoggerFactory.getLogger(ExcelService.class);
    private static final String[] COLUMN_HEADERS = {"ID", "Mal ID", "Title", "Synopsis", "Score", "Members", "Broadcast", "Studios", "Images", "URL"};
    private static final String FILE_PATH = "animes.xlsx";
    private final Workbook workbook;
    private final Sheet sheet;
    private final AtomicInteger rowCount;

    public ExcelService() {
        workbook = new XSSFWorkbook();
        sheet = workbook.createSheet("Animes");
        rowCount = new AtomicInteger(0);
        createHeaderRow();
    }

    private void createHeaderRow() {
        Row headerRow = sheet.createRow(rowCount.getAndIncrement());
        for (int i = 0; i < COLUMN_HEADERS.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(COLUMN_HEADERS[i]);
        }
        logger.info("Cabeçalho criado com sucesso.");
    }

    public void writeAnimeToExcel(Anime anime) {
        logger.info("Iniciando a escrita do anime no Excel: " + anime);
        try {
            Row row = sheet.createRow(rowCount.getAndIncrement());

            row.createCell(0).setCellValue(anime.getId());
            row.createCell(1).setCellValue(anime.getMalId());
            row.createCell(2).setCellValue(anime.getTitle());
            row.createCell(3).setCellValue(anime.getSynopsis());
            row.createCell(4).setCellValue(anime.getScore());
            row.createCell(5).setCellValue(anime.getMembers());
            /*row.createCell(6).setCellValue(anime.getBroadcast() != null ? anime.getBroadcast().toString() : "N/A");
            row.createCell(7).setCellValue(anime.getStudios() != null ? anime.getStudios().toString() : "N/A");
            row.createCell(8).setCellValue(anime.getImages() != null ? anime.getImages().toString() : "N/A");*/
            row.createCell(9).setCellValue(anime.getUrl());

            saveExcelFile();
            logger.info("Dados do anime {} foram escritos no Excel", anime.getTitle());
        } catch (Exception e) {
            logger.error("Erro ao escrever dados no Excel", e);
        }
    }

    private void saveExcelFile() {
        try (FileOutputStream fileOut = new FileOutputStream(FILE_PATH)) {
            workbook.write(fileOut);
            logger.info("Arquivo Excel salvo com sucesso em {}", FILE_PATH);
        } catch (IOException e) {
            logger.error("Erro ao salvar o arquivo Excel", e);
        }
    }
}
