# File Deduplication Application

## Overview
The **File Deduplication Application** is a simple yet powerful tool built with Spring Boot that helps you find and remove duplicate files from a directory. It works in two modes:

- **Web Mode:** A user-friendly interface where you can upload a folder and see the duplicate files.
- **CLI Mode:** A command-line interface that scans a folder and gives you options to delete duplicates or back them up (locally or to Cloudinary).

This app ensures that you don't waste storage space on duplicate files while giving you control over how to handle them.

---

## Features
- **Fast & Efficient:** Uses SHA-256 hashing to detect duplicate files accurately.
- **Two Modes:** Run it via the web UI or directly from the terminal.
- **Backup Options (CLI Only):** Choose to move duplicates to a backup folder or upload them to Cloudinary.
- **Easy to Use:** Simple configuration and minimal setup required.

---

## Requirements
- **Java 21** (or later)
- **Maven** installed
- **Cloudinary Account** (only if you want cloud backup)

---

## Setup & Installation

### 1. Clone the repository
```bash
git clone https://github.com/yourusername/file-deduplication-app.git
cd file-deduplication-app
```

### 2. Configure application settings
Create or update `src/main/resources/application.properties` with the following:

```properties
# Local backup directory (for CLI mode backup)
local.backup.path=/path/to/your/local/backup

# Cloudinary Configuration (only needed for cloud backup in CLI mode)
cloudinary.cloud_name=your_cloud_name
cloudinary.api_key=your_api_key
cloudinary.api_secret=your_api_secret
cloud.backup.folder=your_backup_folder_on_cloudinary
```
Replace `/path/to/your/local/backup` and the Cloudinary credentials with actual values.

---

## Running the Application

### 1. **Web Mode (Browser UI)**
Just run:
```bash
mvn spring-boot:run
```
Then open your browser and go to:
```
http://localhost:8080/
```
Upload a folder and view duplicate files. No backup options are available in this mode.

### 2. **CLI Mode (Terminal)**
Run with:
```bash
mvn spring-boot:run -Dspring-boot.run.arguments="--mode=local"
```
You'll be asked:
1. **Enter the folder path** to scan.
2. **Choose what to do with duplicates:** Delete, move to backup, or upload to Cloudinary.

### 3. **Running as a JAR**
Build the application:
```bash
mvn clean package
```
Run in web mode:
```bash
java -jar target/file-deduplication-app-1.0.0.jar
```
Run in CLI mode:
```bash
java -jar target/file-deduplication-app-1.0.0.jar --mode=local
```

---

## REST API (Optional)
You can also trigger the deduplication process via API:

```bash
GET http://localhost:8080/api/deduplicate?dir=/your/path&backup=none
```
- `dir`: Folder path to scan
- `backup`: `none`, `local`, or `cloud`

Example:
```bash
curl "http://localhost:8080/api/deduplicate?dir=/home/user/files&backup=local"
```

---

## Troubleshooting
- **Cloudinary Backup Fails?** Check your internet connection and credentials in `application.properties`.
- **Permission Issues?** Make sure your app has read/write access to the folders.
- **Port Already in Use?** Change the port by adding this to `application.properties`:
  ```properties
  server.port=9090
  ```

---

## Conclusion
This app makes file deduplication easy while giving you control over how to handle duplicates. Whether you prefer a clean web UI or a command-line approach, it’s got you covered.

Happy deduplicating! 🚀

