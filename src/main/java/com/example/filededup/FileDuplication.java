package com.example.filededup;

import com.example.filededup.service.FileDuplicationService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.util.Map;
import java.util.Scanner;
@SpringBootApplication
public class FileDuplication {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(FileDuplication.class, args);
        FileDuplicationService duplicationService = context.getBean(FileDuplicationService.class);

        if (args.length > 0 && args[0].equalsIgnoreCase("--mode=local")) {
            Scanner scanner = new Scanner(System.in);

            System.out.print("Enter directory path to scan: ");
            String directoryPath = scanner.nextLine();

            System.out.print("Do you want to backup duplicate files? (yes/no): ");
            String backupChoice = scanner.nextLine().trim();

            String backupType = "none";
            if (backupChoice.equalsIgnoreCase("yes")) {
                System.out.print("Choose backup type (cloud/local): ");
                backupType = scanner.nextLine().trim();
                if (!backupType.equalsIgnoreCase("cloud") && !backupType.equalsIgnoreCase("local")) {
                    System.out.println("Invalid backup type. Defaulting to local backup.");
                    backupType = "local";
                }
            }

            try {
                Map<String, Object> result = duplicationService.duplicateFiles(directoryPath, backupType);
                System.out.println("Deduplication completed!");
                System.out.println("Total Files Processed: " + result.get("processedFiles"));
                System.out.println("Total Duplicates Found: " + result.get("duplicatesFound"));
            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
            }
        }
    }
}
