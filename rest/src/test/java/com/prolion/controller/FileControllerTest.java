package com.prolion.controller;


import com.prolion.dto.FolderSizeDTO;
import com.prolion.services.FileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FileController.class)
public class FileControllerTest {

    private MockMvc mockMvc;

    @MockBean
    private FileService fileService;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void testGetFolders() throws Exception {
        when(fileService.getAllFolders()).thenReturn(Arrays.asList("folder1", "folder2"));

        mockMvc.perform(get("/api/folders")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[\"folder1\",\"folder2\"]"));
    }

    @Test
    public void testGetFileSizes() throws Exception {
        when(fileService.getFolderSizesByFileType("txt"))
                .thenReturn(Arrays.asList(new FolderSizeDTO("folder1", 100L)));


        mockMvc.perform(get("/api/filesizes")
                        .param("filetype", "txt")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"folder\":\"folder1\",\"size\":100}]"));
    }

    @Test
    public void testPurgeData() throws Exception {
        mockMvc.perform(delete("/api/purge")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("All data purged successfully."));
    }
}
