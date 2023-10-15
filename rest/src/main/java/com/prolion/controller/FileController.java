package com.prolion.controller;

import com.prolion.dto.FolderSizeDTO;
import com.prolion.services.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class FileController {
    private final FileService fileService;

    @GetMapping("/folders")
    public List<String> getFolders() {
        return fileService.getAllFolders();
    }

    @GetMapping("/filesizes")
    public List<FolderSizeDTO> getFileSizes(@RequestParam(required = false) String filetype) {
        return fileService.getFolderSizesByFileType(filetype);
    }

    @DeleteMapping("/purge")
    public ResponseEntity<String> purgeData() {
        fileService.purgeAllData();
        return new ResponseEntity<>("All data purged successfully.", HttpStatus.OK);
    }
}
