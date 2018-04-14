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
     * @return List of {@link ProjectDom} with existing projects
     */
    @Transactional(readOnly = true)
    public List<ProjectDom> getProjects(){
        List<Project> projectModelList = projectRepository.findAll();
        List<ProjectDom> projectDomList = projectConverter.toDomain(projectModelList);
        return projectDomList;
    }

    /**
     * This method gets a existing project in db and provides it.
     * @param id project id to be found
     * @return {@link ProjectDom} or null if not exist
     */
    @Transactional(readOnly = true)
    public ProjectDom getProject(int id){
        Project projectModel = projectRepository.findByIdProject(id).get();
        ProjectDom projectDom = projectConverter.toDomain(projectModel);
        return projectDom;
    }

    /**
     * Method which interacts with {@link ProjectRepository}, {@link com.Sergio.EasyRMT.Controller.ProjectController}
     * and {@link ProjectConverter}
     * The ProjectController method "createProject" calls this method and provides it a {@link ProjectDom} object filled
     * with information. Then, the method createProject, parse the attributes to an {@link Project} object using
     * ProjectConverter class. When the conversion  finish, createProject call {@link ProjectRepository} to persist data.
     * @param projectDom {@link ProjectDom} object with information.
     * @return {@link ProjectDom} object with information persisted
     *
     */
    @Transactional(rollbackFor = Exception.class)
    public ProjectDom createProject(ProjectDom projectDom) {
        for (String id: projectDom.getStringReqTypes()) {
            projectDom.getRequirementTypes().add(new RequirementTypeDom(Integer.parseInt(id)));
        }
        Project project = projectConverter.toModel(projectDom);
        ProjectDom projectCretated = projectConverter.toDomain(projectRepository.save(project));
        return projectCretated;
    }



    /**
     * This method returns the complete list of requirement types existing in db
     * @return List of {@link RequirementTypeDom} with RequirementTypeDom related with {@link ProjectDom}
     */
    @Transactional(readOnly = true)
    public List<RequirementTypeDom> getReqTypes(){
        List<RequirementType> requirementTypeList = reqTypeRepository.findAll();
        List<RequirementTypeDom> requirementTypeDomList = reqTypeConverter.toDomain(requirementTypeList);
        return requirementTypeDomList;
    }

    public ProjectDom updateProject(int id, ProjectDom project) {
        return project;
    }

    @Transactional(rollbackFor=Exception.class)
    public boolean deleteProject(int id) {
        projectRepository.deleteProjectByIdProject(id);
        return !projectRepository.existsByIdProject(id);
    }
}
