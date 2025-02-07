package com.example.filededup.controller;

import com.example.filededup.service.FileDuplicationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
@RestController
@RequestMapping("/api")
public class FileController {

    private final FileDuplicationService duplicationService;

    public FileController(FileDuplicationService deduplicationService) {
        this.duplicationService = deduplicationService;
    }

    @GetMapping("/duplicate")
    public ResponseEntity<?> deduplicateFiles(
            @RequestParam("dir") String dir,
            @RequestParam(name = "backup", defaultValue = "none") String backup) {

        try {
            Map<String, Object> result = duplicationService.duplicateFiles(dir, backup);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }
}
