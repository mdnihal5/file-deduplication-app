package com.example.filededup.controller;

import com.example.filededup.service.FileDuplicationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.util.Map;

@Controller
@RequestMapping("/")
public class FileUploadController {

    private final FileDuplicationService duplicationService;

    public FileUploadController(FileDuplicationService duplicationService) {
        this.duplicationService = duplicationService;
    }

    @GetMapping
    public String index() {
        return "index";
    }

    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("files") MultipartFile[] files, Model model) {
        try {
            File tempDir = Files.createTempDirectory("uploadedFiles").toFile();

            for (MultipartFile file : files) {
                String filePath = file.getOriginalFilename();
                File dest = new File(tempDir, filePath);
                dest.getParentFile().mkdirs();
                file.transferTo(dest);
            }

            Map<String, Object> result = duplicationService.duplicateFiles(tempDir.getAbsolutePath(), "none");
            model.addAttribute("result", result);
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        return "result";
    }
}
