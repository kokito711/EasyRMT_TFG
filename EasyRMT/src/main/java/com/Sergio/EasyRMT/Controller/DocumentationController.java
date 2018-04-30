/*
 * Copyright (c) $today.year.Sergio López Jiménez and Universidad de Valladolid
 *                             All rights reserved
 */
package com.Sergio.EasyRMT.Controller;

import com.Sergio.EasyRMT.Service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Nullable;

@RestController
public class DocumentationController {
    final String PATH_BASE = "/project/{projectId}/";
    final String UPLOAD_PATH = "uploadFile";
    DocumentService documentService;

    @Autowired
    public DocumentationController(DocumentService documentService) {
        this.documentService = documentService;
    }

    /**
     * TODO this javadoc
     * @param projectId
     * @param file
     * @return
     */
    @RequestMapping(value = PATH_BASE+UPLOAD_PATH, method = RequestMethod.POST)
    public ResponseEntity uploadFile(@PathVariable int projectId, @RequestParam("file")MultipartFile file){
        return upload(projectId, null ,file);
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
