package com.prolion.services;

import com.prolion.dto.FolderSizeDTO;
import com.prolion.entities.FileMetadata;
import com.prolion.repositories.FileMetadataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FileService {
    private final FileMetadataRepository fileMetadataRepository;
    private static final String DOT = ".";

    /**
     * Fetches all folders present in the database.
     * @return A list of folder paths.
     */
    public List<String> getAllFolders() {
        return fileMetadataRepository.findAll().stream()
                .flatMap(file -> buildFolderPath(file.getPath()).stream())
                .filter(path -> !Files.isRegularFile(Paths.get(path)))
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    /**
     * Fetches folder sizes filtered by file type.
     * @param filetype The type of file to filter by.
     * @return A list of folder sizes.
     */
    public List<FolderSizeDTO> getFolderSizesByFileType(String filetype) {
        List<FileMetadata> files = (filetype == null || filetype.isEmpty())
                ? fileMetadataRepository.findAll()
                : fileMetadataRepository.findByFiletype(filetype);

        Map<String, Long> folderSizeMap = files.stream()
                .collect(Collectors.groupingBy(file -> getFolderPath(file.getPath()).orElse(file.getPath()),
                        Collectors.summingLong(FileMetadata::getFilesize)));

        return folderSizeMap.entrySet().stream()
                .map(entry -> new FolderSizeDTO(entry.getKey(), entry.getValue()))
                .sorted(Comparator.comparingLong(FolderSizeDTO::getSize).reversed())
                .collect(Collectors.toList());
    }


    private List<String> buildFolderPath(String path) {
        String[] folderParts = path.split("[\\\\/]");
        List<String> folders = new ArrayList<>();
        StringBuilder currentPath = new StringBuilder();

        for (String part : folderParts) {
            currentPath.append(part);
            if (!part.contains(DOT)) {
                folders.add(currentPath.toString());
            }
            currentPath.append(File.separator);
        }
        return folders;
    }

    private Optional<String> getFolderPath(String fullPath) {
        Path parentPath = Paths.get(fullPath).getParent();
        return (parentPath != null) ? Optional.of(parentPath.toString()) : Optional.of(fullPath);
    }

    /**
     * Deletes all file metadata from the database.
     */
    @Transactional
    public void purgeAllData() {
        fileMetadataRepository.deleteAll();
    }
}
