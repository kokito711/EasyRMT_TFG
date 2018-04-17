package com.Sergio.EasyRMT.UnitTests.Service;

import com.Sergio.EasyRMT.Domain.ProjectDom;
import com.Sergio.EasyRMT.Domain.RequirementTypeDom;
import com.Sergio.EasyRMT.Model.Project;
import com.Sergio.EasyRMT.Model.RequirementType;
import com.Sergio.EasyRMT.Repository.ProjectRepository;
import com.Sergio.EasyRMT.Repository.ReqTypeRepository;
import com.Sergio.EasyRMT.UnitTests.Service.Converter.ProjectConverter;
import com.Sergio.EasyRMT.UnitTests.Service.Converter.ReqTypeConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {

    ProjectRepository projectRepository;
    ReqTypeRepository reqTypeRepository;
    ProjectConverter projectConverter;
    ReqTypeConverter reqTypeConverter;

    @Autowired
    public ProjectService(ProjectRepository projectRepository, ReqTypeRepository reqTypeRepository, ProjectConverter projectConverter, ReqTypeConverter reqTypeConverter) {
        this.projectRepository = projectRepository;
        this.reqTypeRepository = reqTypeRepository;
        this.projectConverter = projectConverter;
        this.reqTypeConverter = reqTypeConverter;
    }

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
        Optional<Project> projectObtained = projectRepository.findByIdProject(id);
        Project projectModel = projectObtained.get();
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
     * The update method receives an id and a {@link ProjectDom} with data modified.
     * Then obtains de persisted project from database and compares if there is any change.
     * If the project information has been changed, this method will persist the changes.
     * @param id Project Id
     * @param project {@link ProjectDom} object with changed data
     * @return {@link ProjectDom} with persisted information
     */
    @Transactional(rollbackFor = Exception.class)
    public ProjectDom updateProject(int id, ProjectDom project) {
        Project projectModel = projectRepository.findByIdProject(id).get();
        ProjectDom projectDomPersisted = new ProjectDom();
        boolean descChanged = false;
        boolean reqTypes = false;
        if(projectModel != null){
            projectDomPersisted = projectConverter.toDomain(projectModel);
        }
        projectDomPersisted.setIdProject(projectModel.getIdProject());
        if (!project.getDescription().equals(projectDomPersisted.getDescription())){
            descChanged = true;
            projectDomPersisted.setDescription(project.getDescription());
        }
        if(project.getStringReqTypes().size()>0){
            for (String reqTypeId: project.getStringReqTypes()) {
                projectDomPersisted.getRequirementTypes().add(new RequirementTypeDom(Integer.parseInt(reqTypeId)));
            }
            reqTypes = true;
        }
        if (descChanged || reqTypes) {
            Project requested = projectConverter.toModel(projectDomPersisted);
            if(descChanged){
                projectModel.setDescription(requested.getDescription());
            }
            if(reqTypes){
                projectModel.setRequirementTypes(requested.getRequirementTypes());
            }
            projectModel = projectRepository.save(projectModel);
            projectDomPersisted = projectConverter.toDomain(projectModel);
        }
        return projectDomPersisted;
    }

    /**
     * This method calls the JPA Repository to delete a project with the idProject received as parameter.
     * Then, calls again the {@link ProjectRepository} to check if project has been deleted.
     * If not exists in database, method will return true and false if exists.
     * @param id idProject to be deleted
     * @return If not exists in database, method will return true and false if exists.
     */
    @Transactional(rollbackFor=Exception.class)
    public boolean deleteProject(int id) {
        if (projectRepository.existsByIdProject(id)){
            projectRepository.deleteProjectByIdProject(id);
            return !projectRepository.existsByIdProject(id);
        }
        else
        {
            return false;
        }
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


}
