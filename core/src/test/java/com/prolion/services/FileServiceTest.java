package com.prolion.services;

import com.prolion.dto.FolderSizeDTO;
import com.prolion.entities.FileMetadata;
import com.prolion.repositories.FileMetadataRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class FileServiceTest {

    @Mock
    private FileMetadataRepository fileMetadataRepository;

    @InjectMocks
    private FileService fileService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetAllFolders() {
        FileMetadata file1 = new FileMetadata();
        file1.setPath("path/to/file1.txt");
        FileMetadata file2 = new FileMetadata();
        file2.setPath("path/to/file2.txt");
        when(fileMetadataRepository.findAll()).thenReturn(Arrays.asList(file1, file2));

        List<String> result = fileService.getAllFolders();

        assertEquals(2, result.size());
        assertEquals("path", result.get(0));
        assertEquals("path\\to", result.get(1));
    }

    @Test
    public void testGetFolderSizesByFileType() {
        FileMetadata file1 = new FileMetadata();
        file1.setPath("path/to/file1.txt");
        file1.setFilesize(100L);
        FileMetadata file2 = new FileMetadata();
        file2.setPath("path/to/file2.txt");
        file2.setFilesize(200L);

        when(fileMetadataRepository.findByFiletype("txt")).thenReturn(Arrays.asList(file1, file2));

        List<FolderSizeDTO> result = fileService.getFolderSizesByFileType("txt");

        assertEquals(1, result.size());
        assertEquals(300L, result.get(0).getSize());
    }

    @Test
    public void testPurgeAllData() {
        fileService.purgeAllData();
        verify(fileMetadataRepository, times(1)).deleteAll();
    }
}
