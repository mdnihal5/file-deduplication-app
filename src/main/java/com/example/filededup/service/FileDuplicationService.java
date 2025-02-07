package com.example.filededup.service;

import com.example.filededup.util.HashFile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.*;

@Service
public class FileDuplicationService {

    private final CloudBackupService cloudBackupService;

    @Value("${local.backup.path}")
    private String localBackupPath;

    public FileDuplicationService(CloudBackupService cloudBackupService) {
        this.cloudBackupService = cloudBackupService;
    }

    public Map<String, Object> duplicateFiles(String directoryPath, String backupType) throws Exception {
        File dir = new File(directoryPath);
        if (!dir.exists() || !dir.isDirectory()) {
            throw new Exception("Invalid directory path: " + directoryPath);
        }

        Map<String, File> hashToFileMap = new HashMap<>();
        List<DuplicateFile> duplicates = new ArrayList<>();
        int processedFiles = 0;
        List<File> allFiles = new ArrayList<>();

        collectFiles(dir, allFiles);

        for (File file : allFiles) {
            if (file.isFile()) {
                processedFiles++;
                try {
                    String fileHash = HashFile.getHash(file);
                    if (hashToFileMap.containsKey(fileHash)) {
                        DuplicateFile duplicate = new DuplicateFile(file.getName(), file.getAbsolutePath());
                        duplicates.add(duplicate);
                        System.out.println("Duplicate found: " + file.getAbsolutePath());

                        if (backupType.equalsIgnoreCase("cloud")) {
                            try {
                                Map<String, Object> uploadResult = cloudBackupService.backupFile(file);
                                String backupUrl = (String) uploadResult.get("secure_url");
                                duplicate.setCloudinaryUrl(backupUrl);
                                if (file.delete()) {
                                    System.out.println(
                                            "Deleted duplicate file after cloud backup: " + file.getAbsolutePath());
                                } else {
                                    System.out.println("Failed to delete duplicate file after cloud backup: "
                                            + file.getAbsolutePath());
                                }
                            } catch (Exception e) {
                                System.err.println("Cloud backup failed for file " + file.getAbsolutePath() +
                                        ", file not deleted. Error: " + e.getMessage());
                                duplicate.setCloudinaryUrl("Backup Failed: " + e.getMessage());
                            }
                        } else if (backupType.equalsIgnoreCase("local")) {
                            moveToLocalBackup(file);
                        } else {
                            if (file.delete()) {
                                System.out.println("Deleted duplicate file: " + file.getAbsolutePath());
                            } else {
                                System.out.println("Failed to delete duplicate file: " + file.getAbsolutePath());
                            }
                        }
                    } else {
                        hashToFileMap.put(fileHash, file);
                    }
                } catch (Exception e) {
                    System.err.println("Error processing file " + file.getAbsolutePath() + ": " + e.getMessage());
                }
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("processedFiles", processedFiles);
        result.put("duplicatesFound", duplicates.size());
        result.put("duplicateFiles", duplicates);
        return result;
    }

    private void collectFiles(File dir, List<File> allFiles) {
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    collectFiles(file, allFiles);
                } else {
                    allFiles.add(file);
                }
            }
        }
    }

    private void moveToLocalBackup(File file) {
        try {
            File backupDir = new File(localBackupPath);
            if (!backupDir.exists()) {
                backupDir.mkdirs();
            }
            File backupFile = new File(backupDir, file.getName());
            Files.move(file.toPath(), backupFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Moved duplicate file to local backup: " + backupFile.getAbsolutePath());
        } catch (IOException e) {
            System.err.println("Failed to move file to local backup: " + e.getMessage());
        }
    }
}
