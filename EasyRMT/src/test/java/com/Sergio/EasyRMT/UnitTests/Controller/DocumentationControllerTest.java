package com.Sergio.EasyRMT.UnitTests.Controller;

import com.Sergio.EasyRMT.Controller.DocumentationController;
import com.Sergio.EasyRMT.Domain.DocumentationDom;
import com.Sergio.EasyRMT.Service.DocumentService;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
public class DocumentationControllerTest {

    @Mock
    private DocumentService documentService;
    @Mock
    MultipartFile file;

    @BeforeEach
    public void initMocks(){
        documentService = mock(DocumentService.class);
        file = mock(MultipartFile.class);
    }

    @Test
    @DisplayName("upload file returns a 200OK when projectId and file are provided and file has been storage")
    public void uploadFile_ProjectIdAndFileProvided_FileStorage_Returns200OK(){
        when(documentService.uploadFile(file,1, null)).thenReturn(true);

        DocumentationController documentationController = createDocumentationController();
        ResponseEntity response = documentationController.uploadFile(1, file);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response);
        assertEquals("", response.getBody().toString());
        verify(documentService, times(1)).uploadFile(file,1, null);
    }

    @Test
    @DisplayName("upload file returns a INTERNALSERVERERROR when projectId and file are provided and file has not been storage")
    public void uploadFile_ProjectIdAndFileProvided_FileStorage_ReturnsINTERNALSERVERERROR(){
        when(documentService.uploadFile(file,1, null)).thenReturn(false);

        DocumentationController documentationController = createDocumentationController();
        ResponseEntity response = documentationController.uploadFile(1, file);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response);
        assertEquals("", response.getBody().toString());
        verify(documentService, times(1)).uploadFile(file,1, null);
    }

    @Test
    @DisplayName("upload file returns a 200Ok when projectId, objectId and file are provided and file has not been storage")
    public void uploadFile_ProjectIdObjectIdAndFileProvided_FileStorage_Returns200Ok(){
        when(documentService.uploadFile(file,1, 1)).thenReturn(true);

        DocumentationController documentationController = createDocumentationController();
        ResponseEntity response = documentationController.uploadFile(1,1, file);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response);
        assertEquals("", response.getBody().toString());
        verify(documentService, times(1)).uploadFile(file,1, 1);
    }

    @Test
    @DisplayName("upload file returns a INTERNALSERVERERROR when projectId, objectId and file are provided and file has not been storage")
    public void uploadFile_ProjectIdObjectIdAndFileProvided_FileStorage_ReturnsINTERNALSERVERERROR(){
        when(documentService.uploadFile(file,1, 1)).thenReturn(false);

        DocumentationController documentationController = createDocumentationController();
        ResponseEntity response = documentationController.uploadFile(1,1, file);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response);
        assertEquals("", response.getBody().toString());
        verify(documentService, times(1)).uploadFile(file,1, 1);
    }

    @Test
    @DisplayName("getFile method  returns a file when projectId and file id are provided")
    public void getFile_ProjectIdAndFileIdProvided_FileReturned(){
        DocumentationDom fileSystemFile = mock(DocumentationDom.class);
        when(documentService.getDBFile(1)).thenReturn(fileSystemFile);
        byte[] content = "content".getBytes();
        when(documentService.getFile(fileSystemFile)).thenReturn(content);
        when(fileSystemFile.getType()).thenReturn(MediaType.APPLICATION_JSON_VALUE);
        when(fileSystemFile.getName()).thenReturn("file");

        DocumentationController documentationController = createDocumentationController();
        HttpEntity<byte[]> obtained = documentationController.getFile(1,1);

        assertNotNull(obtained);
        assertEquals(obtained.getBody(),content);
        assertEquals(obtained.getHeaders().getContentType(), MediaType.APPLICATION_JSON);
        assertEquals(obtained.getHeaders().getContentLength(),content.length);
        verify(documentService, times(1)).getDBFile(1);
        verify(documentService, times(1)).getFile(fileSystemFile);
    }

    @Test
    @DisplayName("getFile method  returns a file when projectId, objectId and file id are provided")
    public void getFile_ProjectIdObjectIdAndFileIdProvided_FileReturned(){
        DocumentationDom fileSystemFile = mock(DocumentationDom.class);
        when(documentService.getDBFile(1)).thenReturn(fileSystemFile);
        byte[] content = "content".getBytes();
        when(documentService.getFile(fileSystemFile)).thenReturn(content);
        when(fileSystemFile.getType()).thenReturn(MediaType.APPLICATION_JSON_VALUE);
        when(fileSystemFile.getName()).thenReturn("file");

        DocumentationController documentationController = createDocumentationController();
        HttpEntity<byte[]> obtained = documentationController.getFile(1,1,1);

        assertNotNull(obtained);
        assertEquals(obtained.getBody(),content);
        assertEquals(obtained.getHeaders().getContentType(), MediaType.APPLICATION_JSON);
        assertEquals(obtained.getHeaders().getContentLength(),content.length);
        verify(documentService, times(1)).getDBFile(1);
        verify(documentService, times(1)).getFile(fileSystemFile);
    }

    @Test
    @DisplayName("delete file returns a 200Ok when file is deleted (project id provided)")
    public void deleteFile_ProjectIdFileIdProvided_FileDeleted_200OkReturned(){
        when(documentService.deleteFile(1)).thenReturn(true);

        DocumentationController documentationController = createDocumentationController();
        ResponseEntity responseEntity = documentationController.deleteFile(1,1);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity);
        assertEquals("", responseEntity.getBody().toString());
        verify(documentService, times(1)).deleteFile(1);
    }

    @Test
    @DisplayName("delete file returns a 200Ok when file is deleted (project id and object id provided)")
    public void deleteFile_ProjectIdObjectIdFileIdProvided_FileDeleted_200OkReturned(){
        when(documentService.deleteFile(1)).thenReturn(true);

        DocumentationController documentationController = createDocumentationController();
        ResponseEntity responseEntity = documentationController.deleteFile(1,1,1);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity);
        assertEquals("", responseEntity.getBody().toString());
        verify(documentService, times(1)).deleteFile(1);
    }

    @Test
    @DisplayName("delete file returns a INTERNALSERVERERROR when file is deleted (project id provided)")
    public void deleteFile_ProjectIdFileIdProvided_FileDeleted_INTERNALSERVERERROR(){
        when(documentService.deleteFile(1)).thenReturn(false);

        DocumentationController documentationController = createDocumentationController();
        ResponseEntity responseEntity = documentationController.deleteFile(1,1);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertNotNull(responseEntity);
        assertEquals("", responseEntity.getBody().toString());
        verify(documentService, times(1)).deleteFile(1);
    }

    @Test
    @DisplayName("delete file returns a INTERNALSERVERERROR when file is deleted (project id and object id provided)")
    public void deleteFile_ProjectIdObjectIdFileIdProvided_FileDeleted_INTERNALSERVERERROR(){
        when(documentService.deleteFile(1)).thenReturn(false);

        DocumentationController documentationController = createDocumentationController();
        ResponseEntity responseEntity = documentationController.deleteFile(1,1,1);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertNotNull(responseEntity);
        assertEquals("", responseEntity.getBody().toString());
        verify(documentService, times(1)).deleteFile(1);
    }

    private DocumentationController createDocumentationController(){
        return new DocumentationController(documentService);
    }

}
