package com.Sergio.EasyRMT.Service;

import com.Sergio.EasyRMT.Domain.ProjectDom;
import com.Sergio.EasyRMT.Domain.RequirementTypeDom;
import com.Sergio.EasyRMT.Model.Project;
import com.Sergio.EasyRMT.Model.RequirementType;
import com.Sergio.EasyRMT.Repository.ProjectRepository;
import com.Sergio.EasyRMT.Repository.ReqTypeRepository;
import com.Sergio.EasyRMT.Service.Converter.ProjectConverter;
import com.Sergio.EasyRMT.Service.Converter.ReqTypeConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import javax.validation.Valid;
import java.util.List;

@Service
public class ProjectService {
    @Autowired
    ProjectRepository projectRepository;
    @Autowired
    ReqTypeRepository reqTypeRepository;
    @Autowired
    ProjectConverter projectConverter;
    @Autowired
    ReqTypeConverter reqTypeConverter;

    /**
     * This method gets a list of existing projects in db and provides it
     * @return List<ProjectDom> List of existing projects
     */
    @Transactional(readOnly = true)
    public List<ProjectDom> getProjects(){
        List<Project> projectModelList = projectRepository.findAll();
        List<ProjectDom> projectDomList = projectConverter.toDomain(projectModelList);
        return projectDomList;
    }

    //get project


    @Transactional(rollbackFor = Exception.class)
    public ProjectDom createProject(ProjectDom projectDom) {
        Project project = projectConverter.toModel(projectDom);
        ProjectDom projectCretated = projectConverter.toDomain(project);
        return projectCretated;
    }

    /**
     * This method returns the complete list of requirement types existing in db
     * @return List<RequirementTypeDom> A list with RequirementTypeDom
     * @See RequirementTypeDom
     */
    @Transactional(readOnly = true)
    public List<RequirementTypeDom> getReqTypes(){
        List<RequirementType> requirementTypeList = reqTypeRepository.findAll();
        List<RequirementTypeDom> requirementTypeDomList = reqTypeConverter.toDomain(requirementTypeList);
        return requirementTypeDomList;
    }
}
