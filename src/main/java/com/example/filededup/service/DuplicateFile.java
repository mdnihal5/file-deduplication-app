package com.example.filededup.service;

public class DuplicateFile {
    private String fileName;
    private String filePath;
    private String cloudinaryUrl;

    public DuplicateFile(String fileName, String filePath) {
        this.fileName = fileName;
        this.filePath = filePath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getCloudinaryUrl() {
        return cloudinaryUrl;
    }

    public void setCloudinaryUrl(String cloudinaryUrl) {
        this.cloudinaryUrl = cloudinaryUrl;
    }

    @Override
    public String toString() {
        return "File Name: " + fileName + ", File Path: " + filePath;
    }
}
