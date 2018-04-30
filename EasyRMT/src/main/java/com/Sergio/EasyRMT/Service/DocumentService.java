/*
 * Copyright (c) $today.year.Sergio López Jiménez and Universidad de Valladolid
 *                             All rights reserved
 */
package com.Sergio.EasyRMT.Service;

import com.Sergio.EasyRMT.Domain.DocumentationDom;
import com.Sergio.EasyRMT.Model.Documentation;
import com.Sergio.EasyRMT.Model.ObjectEntity;
import com.Sergio.EasyRMT.Model.Project;
import com.Sergio.EasyRMT.Repository.DocumentationRepository;
import com.Sergio.EasyRMT.Repository.ObjectRepository;
import com.Sergio.EasyRMT.Repository.ProjectRepository;
import com.Sergio.EasyRMT.Service.Converter.DocumentationConverter;
import javassist.bytecode.stackmap.TypeData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class DocumentService {

    private static final Logger LOGGER = Logger.getLogger( TypeData.ClassName.class.getName() );
    ObjectRepository objectRepository;
    ProjectRepository projectRepository;
    DocumentationConverter documentationConverter;
    DocumentationRepository documentationRepository;

//TODO javadoc
    @Autowired
    public DocumentService(ObjectRepository objectRepository, ProjectRepository projectRepository,
                            DocumentationConverter documentationConverter,
                            DocumentationRepository documentationRepository) {
        this.objectRepository = objectRepository;
        this.projectRepository = projectRepository;
        this.documentationConverter = documentationConverter;
        this.documentationRepository = documentationRepository;
    }

    /**
     *
     * @param file
     * @param projectId
     * @param objectId
     * @return
     */
    @Transactional(rollbackFor = Exception.class, noRollbackFor=IOException.class)
    public boolean uploadFile(MultipartFile file, int projectId, @Nullable Integer objectId){
        if(file.isEmpty()){
            return false;
        }
        String path = "./"+Integer.toString(projectId);
        // Determines the path when file is going to be saved
        if(objectId != null){
            path = path.concat('/'+Integer.toString(objectId));
        }
        File filepath = new File(path+'/'+file.getOriginalFilename());
        Double size = (((double)file.getSize())/1024)/1024;
        DocumentationDom document = new DocumentationDom();
        document.setName(file.getOriginalFilename());
        document.setPath(path);
        document.setType(file.getContentType());
        document.setSize(size);
        //Save file calling space allocator service
        if(!filePersist(file,path, document)){
            return false;
        }
        Documentation documentation = documentationConverter.toModel(document);
        Optional<Project> project = projectRepository.findByIdProject(projectId);
        documentation.setProject(project.get());
        if(objectId !=null) {
            ObjectEntity object = objectRepository.findOne(objectId);
        }
        documentationRepository.save(documentation);
        return true;
    }

    //TODO JAVADoc

    /**
     *
     * @param file
     * @param path
     * @return
     */
    private boolean filePersist(MultipartFile file, String path, DocumentationDom documentationDom){
        /**
         *  Check if a folder exists.
         *  If not exists creates the new directory
         *  then, saves file
         */
        try {
            File folder = new File(path);
            if (!(folder.exists() && folder.isDirectory())) {
                boolean status =folder.mkdirs();
            }
            /**
             * checks if file exists
             * If exists, save file but with date (yyyyMMdd) before name.
             * else just save file.
             */
            File filepath = new File(path+'/'+file.getOriginalFilename());
            if(filepath.exists()){
                Date date = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
                String formatDate = sdf.format(date);
                path = path.concat('/'+formatDate+file.getOriginalFilename());
                documentationDom.setPath(path);
            }else {
                path = path.concat('/'+file.getOriginalFilename());
            }

            byte[] bytes = file.getBytes();
            Path path1 = Paths.get(path);
            Files.write(path1,bytes);
            return true;

        }
        catch (IOException e){
            LOGGER.log(Level.WARNING, "System failed to create and persist file");
            return false;
        }
    }

    /*private String generatePath( String path, DocumentationDom documentationDom){

    }*/
}
