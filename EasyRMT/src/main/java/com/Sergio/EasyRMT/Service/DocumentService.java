package com.Sergio.EasyRMT.Service;

import com.Sergio.EasyRMT.Domain.DocumentationDom;
import com.Sergio.EasyRMT.Model.Documentation;
import com.Sergio.EasyRMT.Model.ObjectEntity;
import com.Sergio.EasyRMT.Model.Project;
import com.Sergio.EasyRMT.Repository.DocumentationRepository;
import com.Sergio.EasyRMT.Repository.ObjectRepository;
import com.Sergio.EasyRMT.Repository.ProjectRepository;
import com.Sergio.EasyRMT.Service.Converter.DocumentationConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Nullable;
import java.util.Optional;

@Service
public class DocumentService {

    ObjectRepository objectRepository;
    ProjectRepository projectRepository;
    DocumentationConverter documentationConverter;
    DocumentationRepository documentationRepository;
    SpaceAllocatorService spaceAllocatorService;


    @Autowired
    public DocumentService(ObjectRepository objectRepository, ProjectRepository projectRepository,
                            DocumentationConverter documentationConverter, SpaceAllocatorService spaceAllocatorService,
                            DocumentationRepository documentationRepository) {
        this.objectRepository = objectRepository;
        this.projectRepository = projectRepository;
        this.documentationConverter = documentationConverter;
        this.spaceAllocatorService = spaceAllocatorService;
        this.documentationRepository = documentationRepository;
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean uploadFile(MultipartFile file, int projectId, @Nullable Integer objectId){
        if(file.isEmpty()){
            return false;
        }
        String path = "/";
        // Determines the path when file is going to be saved
        if(objectId.equals(null)){
            path.concat(Integer.toString(projectId));
        }
        else{
            path.concat(Integer.toString(projectId)+'/'+objectId);
        }
        Double size = Double.longBitsToDouble((file.getSize()/1024)/1024);
        DocumentationDom document = new DocumentationDom();
        document.setName(file.getOriginalFilename());
        document.setPath(path);
        document.setType(file.getContentType());
        document.setSize(size);

        Documentation documentation = documentationConverter.toModel(document);
        Optional<Project> project = projectRepository.findByIdProject(projectId);
        documentation.setProject(project.get());
        if(!objectId.equals(null)) {
            ObjectEntity object = objectRepository.findOne(objectId);
        }
        else {
            documentation.setObject(null);
        }

        //Save file calling space allocator service
        if(!spaceAllocatorService.filePersist(file,path)){
            return false;
        }
        documentationRepository.save(documentation);
        return true;
    }
}
