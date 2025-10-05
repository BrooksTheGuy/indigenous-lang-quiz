package com.example.indigenous.service;

import com.example.indigenous.model.Translation;
import com.example.indigenous.repository.QuizAttemptRepository;
import com.example.indigenous.repository.TranslationRepository;
import com.example.indigenous.repository.UserRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.*;

@Service
public class AdminService {

    public record ImportResult(int created, int updated, int skipped, List<String> errors) {}
    public record Counts(long users, long attempts, long translations) {}

    private final UserRepository users;
    private final QuizAttemptRepository attempts;
    private final TranslationRepository translations;

    public AdminService(UserRepository users,
                        QuizAttemptRepository attempts,
                        TranslationRepository translations) {
        this.users = users;
        this.attempts = attempts;
        this.translations = translations;
    }

    // === DASHBOARD ===
    public Counts counts() {
        return new Counts(users.count(), attempts.count(), translations.count());
    }

    public List<com.example.indigenous.model.User> findRecentUsers(int limit) {
        // Using repository method; limit in memory is fine for top10
        var list = users.findTop10ByOrderByCreatedAtDesc();
        return list.size() > limit ? list.subList(0, limit) : list;
    }

    // === UPLOAD EXCEL ===
    public ImportResult importExcel(MultipartFile file, String optionalLang) {
        int created = 0, updated = 0, skipped = 0;
        List<String> errors = new ArrayList<>();

        try (InputStream is = file.getInputStream();
             Workbook wb = WorkbookFactory.create(is)) {

            Sheet sheet = wb.getSheetAt(0);
            if (sheet == null) {
                errors.add("No sheet found in workbook.");
                return new ImportResult(created, updated, skipped, errors);
            }

            // Expect columns (any order): Language | English | Translation
            Row header = sheet.getRow(0);
            if (header == null) {
                errors.add("Missing header row.");
                return new ImportResult(created, updated, skipped, errors);
            }

            Map<String,Integer> idx = new HashMap<>();
            for (int c = 0; c < header.getLastCellNum(); c++) {
                var name = s(header.getCell(c));
                if (name != null && !name.isBlank()) {
                    idx.put(name.trim().toLowerCase(), c);
                }
            }

            Integer iLang = firstPresent(idx, "language", "lang");
            Integer iEnglish = firstPresent(idx, "english", "source", "source text");
            Integer iTrans = firstPresent(idx, "translation", "translated", "translated text");

            if (iEnglish == null || iTrans == null) {
                errors.add("Header must include at least English and Translation columns.");
                return new ImportResult(created, updated, skipped, errors);
            }

            for (int r = 1; r <= sheet.getLastRowNum(); r++) {
                Row row = sheet.getRow(r);
                if (row == null) { skipped++; continue; }

                String lang = optionalLang != null && !optionalLang.isBlank()
                        ? optionalLang : val(row, iLang);
                String english = val(row, iEnglish);
                String trans = val(row, iTrans);

                if (lang == null || lang.isBlank() ||
                        english == null || english.isBlank() ||
                        trans == null || trans.isBlank()) {
                    skipped++; continue;
                }

                lang = lang.trim().toUpperCase();
                english = english.trim();
                trans = trans.trim();

                var existing = translations.findByLangAndSourceText(lang, english);
                if (existing.isEmpty()) {
                    translations.save(new Translation(lang, english, trans));
                    created++;
                } else {
                    var t = existing.get();
                    t.setTranslatedText(trans);
                    translations.save(t);
                    updated++;
                }
            }
        } catch (Exception e) {
            errors.add("Failed to parse Excel: " + e.getMessage());
        }
        return new ImportResult(created, updated, skipped, errors);
    }

    // === DOWNLOAD TEMPLATE ===
    public byte[] buildTemplate(String lang) {
        try (Workbook wb = new XSSFWorkbook()) {
            Sheet sh = wb.createSheet("Translations");
            Row h = sh.createRow(0);
            h.createCell(0).setCellValue("Language");
            h.createCell(1).setCellValue("English");
            h.createCell(2).setCellValue("Translation");

            Row ex = sh.createRow(1);
            ex.createCell(0).setCellValue(lang != null && !lang.isBlank() ? lang.toUpperCase() : "OSHIWAMBO");
            ex.createCell(1).setCellValue("Hello");
            ex.createCell(2).setCellValue("Wa uhala po");

            sh.autoSizeColumn(0); sh.autoSizeColumn(1); sh.autoSizeColumn(2);

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            wb.write(out);
            return out.toByteArray();
        } catch (Exception e) {
            return new byte[0];
        }
    }

    // === helpers ===
    private static Integer firstPresent(Map<String,Integer> idx, String... names) {
        for (var n : names) if (idx.containsKey(n)) return idx.get(n);
        return null;
    }
    private static String val(Row r, Integer c) {
        if (r == null || c == null) return null;
        return s(r.getCell(c));
    }
    private static String s(Cell c) {
        if (c == null) return null;
        c.setCellType(CellType.STRING);
        return c.getStringCellValue();
    }
}
