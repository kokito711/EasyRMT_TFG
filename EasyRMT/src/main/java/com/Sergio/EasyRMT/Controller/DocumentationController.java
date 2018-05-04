/*
 * Copyright (c) $today.year.Sergio López Jiménez and Universidad de Valladolid
 *                             All rights reserved
 */
package com.Sergio.EasyRMT.Controller;

import com.Sergio.EasyRMT.Domain.DocumentationDom;
import com.Sergio.EasyRMT.Service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Nullable;

@RestController
public class DocumentationController {
    final String PATH_BASE = "/project/{projectId}/";
    final String UPLOAD_PATH = "uploadFile";
    final String OBJECT_PATH = "object/{objectId}/";
    DocumentService documentService;

    @Autowired
    public DocumentationController(DocumentService documentService) {
        this.documentService = documentService;
    }

    /**
     * This method receives a file to be stored in project path. Then calls method upload which will persist file and
     * a register in database to find file
     * @param projectId id of project
     * @param file MultipartFile received
     * @return response entity 200 if file has been stored and 500 if not
     */
    @RequestMapping(value = PATH_BASE+UPLOAD_PATH, method = RequestMethod.POST)
    public ResponseEntity uploadFile(@PathVariable int projectId, @RequestParam("file")MultipartFile file){
        return upload(projectId, null ,file);
    }

    /**
     * This method receives a file to be stored in an object path. Then calls method upload which will persist file and
     * a register in database to find file
     * @param projectId id of project
     * @param objectId od of object
     * @param file MultipartFile received
     * @return response entity 200 if file has been stored and 500 if not
     */
    @RequestMapping(value = PATH_BASE+OBJECT_PATH+UPLOAD_PATH, method = RequestMethod.POST)
    public ResponseEntity uploadFile(@PathVariable int projectId, @PathVariable int objectId,
                                     @RequestParam("file")MultipartFile file){
        return upload(projectId, objectId ,file);
    }

    /**
     * This method returns a file stored in project path. Method calls {@link DocumentService} to get the file and serve it
     * @param projectId id of project
     * @param fileId id of file
     * @return response entity 200 if file has been stored and 500 if not
     */
    @RequestMapping(value = PATH_BASE+"file/{fileId}", method = RequestMethod.GET)
    public HttpEntity<byte[]> getFile(@PathVariable int projectId, @PathVariable int fileId){
        DocumentationDom file = documentService.getDBFile(fileId);
        HttpEntity<byte[]> response = download(projectId, null,file);
        return response;
    }
    /**
    * This method returns a file stored in object path. Method calls {@link DocumentService} to get the file and serve it
    * @param projectId id of project
    * @param objectId id of object
    * @param fileId id of file
    * @return response entity 200 if file has been stored and 500 if not
    */
    @RequestMapping(value = PATH_BASE+OBJECT_PATH+"file/{fileId}", method = RequestMethod.GET)
    public HttpEntity<byte[]> getFile(@PathVariable int projectId, @PathVariable int objectId, @PathVariable int fileId){
        DocumentationDom file = documentService.getDBFile(fileId);
        HttpEntity<byte[]> response = download(projectId, null,file);
        return response;
    }

    /**
     * This method is a request to delete a file that is own by a project
     * @param projectId id of project
     * @param fileId id of file
     * @return status of deletion. 200 if ok and 500 if not ok
     */
    @RequestMapping(value = PATH_BASE+"file/{fileId}", method = RequestMethod.DELETE)
    public ResponseEntity deleteFile(@PathVariable int projectId, @PathVariable int fileId){
        boolean response = documentService.deleteFile(fileId);
        if(response){
            return ResponseEntity.status(HttpStatus.OK).body("");
        }
        else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("");
        }
    }

    /**
     * This method is a request to delete a file that is own by an object
     * @param projectId id of project
     * @param objectId id of object
     * @param fileId id of file
     * @return status of deletion. 200 if ok and 500 if not ok
     */
    @RequestMapping(value = PATH_BASE+OBJECT_PATH+UPLOAD_PATH+"file/{fileId}", method = RequestMethod.DELETE)
    public ResponseEntity deleteFile(@PathVariable int projectId, @PathVariable int objectId, @PathVariable int fileId){
        boolean response = documentService.deleteFile(fileId);
        if(response){
            return ResponseEntity.status(HttpStatus.OK).body("");
        }
        else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("");
        }
    }

    private HttpEntity<byte[]> download(int projectId, @Nullable Integer objectId, DocumentationDom file) {
        /*
        Creation of header
         */
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.valueOf(file.getType()));
        header.set(HttpHeaders.CONTENT_DISPOSITION,
                "attachment;filename="+ file.getName());
        //Get File from documentService
        byte[] content = documentService.getFile(file);
        header.setContentLength(content.length);
        HttpEntity<byte[]> response = new HttpEntity<>(content, header);
        return response;
    }

    private ResponseEntity upload(int projectId, @Nullable Integer objectId, MultipartFile file){

        boolean uploaded = documentService.uploadFile(file,projectId,objectId);
        if(uploaded){
            return ResponseEntity.status(HttpStatus.OK).body("");
        }
        else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("");
        }
    }
}
