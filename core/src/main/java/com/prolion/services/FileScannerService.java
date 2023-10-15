package com.prolion.services;

import com.prolion.entities.FileMetadata;
import com.prolion.repositories.FileMetadataRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class FileScannerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileScannerService.class);
    private final FileMetadataRepository fileMetadataRepository;

    @Value("${file.scan.directory}")
    private String rootDirPath;

    @Scheduled(fixedRateString = "${file.scan.frequency}")
    public void scheduledScan() {
        LOGGER.info("Initiating scheduled directory scan...");

        clearExistingMetadata();
        scanDirectory(rootDirPath);
    }

    private void clearExistingMetadata() {
        fileMetadataRepository.deleteAll();
    }

    public void scanDirectory(String rootDirPath) {
        try (Stream<Path> paths = Files.walk(Paths.get(rootDirPath))) {
            paths.filter(Files::isRegularFile)
                    .forEach(this::processFile);
        } catch (IOException e) {
            LOGGER.error("Error walking the file tree starting from: {}", rootDirPath, e);
        }
    }

    private void processFile(Path path) {
        try {
            BasicFileAttributes attributes = Files.readAttributes(path, BasicFileAttributes.class);
            FileMetadata metadata = buildFileMetadata(path, attributes);
            fileMetadataRepository.save(metadata);
        } catch (IOException e) {
            LOGGER.error("Error reading attributes for file: {}", path, e);
        }
    }

    private FileMetadata buildFileMetadata(Path path, BasicFileAttributes attributes) {
        FileMetadata metadata = new FileMetadata();
        metadata.setPath(path.toString());
        metadata.setFilename(path.getFileName().toString());
        metadata.setFiletype(getFileExtension(path));
        metadata.setFilesize(attributes.size());
        metadata.setModificationDate(LocalDateTime.ofInstant(attributes.lastModifiedTime().toInstant(), ZoneId.systemDefault()));
        metadata.setScanDate(LocalDateTime.now());
        return metadata;
    }

    private String getFileExtension(Path path) {
        String filename = path.getFileName().toString();
        int dotIndex = filename.lastIndexOf('.');
        if (dotIndex > 0 && dotIndex < filename.length() - 1) {
            return filename.substring(dotIndex + 1);
        }
        return "";
    }
}