package com.Sergio.EasyRMT.Service.Converter;

import com.Sergio.EasyRMT.Domain.GroupDom;
import com.Sergio.EasyRMT.Domain.ProjectDom;
import com.Sergio.EasyRMT.Domain.RequirementTypeDom;
import com.Sergio.EasyRMT.Model.Project;
import com.Sergio.EasyRMT.Model.RequirementType;
import com.Sergio.EasyRMT.Repository.ReqTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class ProjectConverter {

    ReqTypeRepository reqTypeRepository;
    GroupConverter groupConverter;

    @Autowired
    public ProjectConverter(ReqTypeRepository reqTypeRepository, GroupConverter groupConverter) {
        this.reqTypeRepository = reqTypeRepository;
        this.groupConverter = groupConverter;
    }

    public List<ProjectDom> toDomain(List<Project> projectModelList) {
        List<ProjectDom> projectDomList = new ArrayList<>();
        for (Project project: projectModelList) {
            ProjectDom projectDom = toDomain(project);
            projectDomList.add(projectDom);
        }
        return projectDomList;
    }

    public Project toModel(ProjectDom projectDom) {
        Project project = new Project();
        List<RequirementType> reqTypeList = new ArrayList<>();
        project.setName(projectDom.getName());
        if (projectDom.getDescription() != null){
            project.setDescription(projectDom.getDescription());
        }
        project.setType(projectDom.getType());
        for(RequirementTypeDom reqTypeDom : projectDom.getRequirementTypes()){
            Optional<RequirementType> reqObtained = reqTypeRepository.findByIdType(reqTypeDom.getIdType());
            RequirementType reqType = reqObtained.get();
            reqTypeList.add(reqType);
        }
        project.setRequirementTypes(reqTypeList);

        return  project;
    }

    public ProjectDom toDomain(Project project){
        List<RequirementTypeDom> reqTypeDomList = new ArrayList<>();
        for (RequirementType reqType : project.getRequirementTypes()){
            RequirementTypeDom reqTypeDom = new RequirementTypeDom(
                    reqType.getIdType(),
                    reqType.getName(),
                    reqType.getType()
            );
            reqTypeDomList.add(reqTypeDom);
        }
        GroupDom group = groupConverter.toDomain(project.getGroup());
        if(project.getDescription()== null){
            project.setDescription("");
        }
        ProjectDom projectDom = new ProjectDom(
                project.getIdProject(),
                project.getName(),
                project.getDescription(),
                project.getType(),
                reqTypeDomList,
                group
        );
        return projectDom;
    }
}
