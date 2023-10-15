package com.prolion.repositories;

import com.prolion.entities.FileMetadata;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface  FileMetadataRepository extends JpaRepository<FileMetadata, Long> {
    List<FileMetadata> findByFiletype(String filetype);
}
