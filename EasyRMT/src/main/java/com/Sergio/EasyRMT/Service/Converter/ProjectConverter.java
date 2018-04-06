package com.Sergio.EasyRMT.Service.Converter;

import com.Sergio.EasyRMT.Domain.ProjectDom;
import com.Sergio.EasyRMT.Domain.RequirementTypeDom;
import com.Sergio.EasyRMT.Model.Project;
import com.Sergio.EasyRMT.Model.RequirementType;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ProjectConverter {
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
}
