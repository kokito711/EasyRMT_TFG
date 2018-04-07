package com.Sergio.EasyRMT.Service.Converter;

import com.Sergio.EasyRMT.Domain.ProjectDom;
import com.Sergio.EasyRMT.Domain.RequirementTypeDom;
import com.Sergio.EasyRMT.Model.Project;
import com.Sergio.EasyRMT.Model.RequirementType;
import com.Sergio.EasyRMT.Repository.ReqTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ProjectConverter {

    @Autowired
    ReqTypeRepository reqTypeRepository;

    public List<ProjectDom> toDomain(List<Project> projectModelList) {
        List<ProjectDom> projectDomList = new ArrayList<>();
        for (Project project: projectModelList) {
            List<RequirementType> requirementTypeList = project.getRequirementTypes();
            List<RequirementTypeDom> requirementTypeDomList = new ArrayList<>();
            for(RequirementType reqType : requirementTypeList){
                RequirementTypeDom reqTypeDom = new RequirementTypeDom(
                        reqType.getIdType(),
                        reqType.getName()
                );
                requirementTypeDomList.add(reqTypeDom);
            }
            ProjectDom projectDom = new ProjectDom(
              project.getIdProject(),
              project.getName(),
              project.getDescription(),
              project.getType(),
              requirementTypeDomList
            );
            projectDomList.add(projectDom);
        }
        return projectDomList;
    }

    public Project toModel(ProjectDom projectDom) {
        Project project = new Project();
        List<RequirementType> reqTypeList = new ArrayList<>();
        project.setName(project.getName());
        project.setDescription(project.getDescription());
        project.setType(project.getType());
        for(RequirementTypeDom reqTypeDom : projectDom.getRequirementTypes()){
            RequirementType reqType = reqTypeRepository.findByIdType(reqTypeDom.getIdType()).get();
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
                    reqType.getName()
            );
            reqTypeDomList.add(reqTypeDom);
        }
        ProjectDom projectDom = new ProjectDom(
                project.getIdProject(),
                project.getName(),
                project.getDescription(),
                project.getType(),
                reqTypeDomList
        );
        return projectDom;
    }
}
