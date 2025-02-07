package com.example.filededup.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Map;

@Service
public class CloudBackupService {

    private Cloudinary cloudinary;

    @Value("${cloudinary.cloud_name}")
    private String cloudName;

    @Value("${cloudinary.api_key}")
    private String apiKey;

    @Value("${cloudinary.api_secret}")
    private String apiSecret;

    @Value("${cloud.backup.folder}")
    private String backupFolder;

    private void initCloudinary() {
        if (cloudinary == null) {
            Map<String, String> config = ObjectUtils.asMap(
                    "cloud_name", cloudName,
                    "api_key", apiKey,
                    "api_secret", apiSecret);
            cloudinary = new Cloudinary(config);
        }
    }

    public Map<String, Object> backupFile(File file) throws Exception {
        initCloudinary();
        try {
            System.out.println("file: " + file);
            Map<String, Object> uploadResult = cloudinary.uploader().upload(file,
                    ObjectUtils.asMap("folder", backupFolder, "resource_type", "raw"));
            System.out.println("Backup successful. Cloudinary response: " + uploadResult);
            return uploadResult;
        } catch (Exception e) {
            throw new Exception("Cloudinary backup failed: " + e.getMessage());
        }
    }
}
