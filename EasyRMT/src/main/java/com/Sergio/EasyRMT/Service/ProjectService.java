package com.Sergio.EasyRMT.Service;

import com.Sergio.EasyRMT.Domain.ProjectDom;
import com.Sergio.EasyRMT.Domain.RequirementTypeDom;
import com.Sergio.EasyRMT.Model.Group;
import com.Sergio.EasyRMT.Model.Project;
import com.Sergio.EasyRMT.Model.RequirementType;
import com.Sergio.EasyRMT.Repository.GroupRepository;
import com.Sergio.EasyRMT.Repository.ObjectRepository;
import com.Sergio.EasyRMT.Repository.ProjectRepository;
import com.Sergio.EasyRMT.Repository.ReqTypeRepository;
import com.Sergio.EasyRMT.Service.Converter.ProjectConverter;
import com.Sergio.EasyRMT.Service.Converter.ReqTypeConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class ProjectService {

    ProjectRepository projectRepository;
    ReqTypeRepository reqTypeRepository;
    ProjectConverter projectConverter;
    ReqTypeConverter reqTypeConverter;
    DocumentService documentService;
    GroupRepository groupRepository;
    ObjectRepository objectRepository;
    TraceabilityService traceabilityService;
    FeatureService featureService;
    EpicService epicService;
    UseCaseService useCaseService;
    UserStoryService userStoryService;
    RequirementService requirementService;

    @Autowired
    public ProjectService(ProjectRepository projectRepository, ReqTypeRepository reqTypeRepository, ProjectConverter projectConverter,
                          ReqTypeConverter reqTypeConverter, DocumentService documentService, GroupRepository groupRepository,
                          ObjectRepository objectRepository, TraceabilityService traceabilityService, FeatureService featureService,
                          EpicService epicService, UseCaseService useCaseService, UserStoryService userStoryService,
                          RequirementService requirementService) {
        this.projectRepository = projectRepository;
        this.reqTypeRepository = reqTypeRepository;
        this.projectConverter = projectConverter;
        this.reqTypeConverter = reqTypeConverter;
        this.documentService = documentService;
        this.groupRepository = groupRepository;
        this.objectRepository = objectRepository;
        this.traceabilityService = traceabilityService;
        this.featureService = featureService;
        this.epicService = epicService;
        this.useCaseService = useCaseService;
        this.userStoryService = userStoryService;
        this.requirementService = requirementService;
    }

    /**
     * This method gets a list of existing projects in db and provides it
     * @return List of {@link ProjectDom} with existing projects
     */
    @Transactional(readOnly = true)
    public List<ProjectDom> getProjects(int groupId){
        List<Project> projectModelList = projectRepository.findByGroup(groupId);
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
        Group group = groupRepository.findOne(projectDom.getGroupId());
        Project project = projectConverter.toModel(projectDom);
        project.setGroup(group);
        project = projectRepository.save(project);
        ProjectDom projectCretated = projectConverter.toDomain(project);
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
        Group group;
        boolean groupChgd = false;
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
        if(project.getGroupId()!= projectModel.getGroup().getGroup_id()){
            group = groupRepository.findOne(project.getGroupId());
            projectModel.setGroup(group);
            groupChgd = true;
        }
        if (descChanged || reqTypes) {
            Project requested = projectConverter.toModel(projectDomPersisted);
            if (descChanged) {
                projectModel.setDescription(requested.getDescription());
            }
            if (reqTypes) {
                projectModel.setRequirementTypes(requested.getRequirementTypes());
            }
        }
        if(descChanged || reqTypes || groupChgd){
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
            documentService.deleteFiles(id,null);
            projectRepository.deleteProjectByIdProject(id);
            return !projectRepository.existsByIdProject(id);
        }
        else
        {
            return false;
        }
    }

   /*@Transactional
    public List<ObjectDom> getProjectTraceability(int projectId){
        List<ObjectEntity> objects = objectRepository.findByProjectId(projectId);
        List<ObjectDom> objectDomList = new ArrayList<>();
       getObjectDomList(objects, objectDomList);
       return  objectDomList;
    }*/


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


    /**
     * This method returns a requirement type existing in db
     * @return {@link RequirementTypeDom} with RequirementTypeDom related with {@link ProjectDom}
     */
    @Transactional(readOnly = true)
    public RequirementTypeDom getReqType(int reqTypeId){
        Optional<RequirementType> requirementTypeList = reqTypeRepository.findByIdType(reqTypeId);
        RequirementTypeDom requirementTypeDomList = reqTypeConverter.toDomain(requirementTypeList.get());
        return requirementTypeDomList;
    }

    @Transactional(readOnly = true)
    public Map<String,Integer> getTracedStats(int idProject){
        Map<String, Integer> stats = new HashMap<>();
        Integer tracedReqs = projectRepository.getTracedObjects(idProject);
        Integer notTracedReqs = projectRepository.getNotTracedObjects(idProject);
        stats.put("tracedReqs", tracedReqs);
        stats.put("notTracedReqs", notTracedReqs);

        return stats;
    }

    public Map<String, List> getStateStats(int idProject) {
        List<Integer> requirementsList = new ArrayList<>();
        List<Integer> objectLvl1Serie= new ArrayList<>();
        List<Integer> objectLvl2Serie= new ArrayList<>();
        Map<String,List> stats = new HashMap<>();
        Project project = projectRepository.findByIdProject(idProject).get();
        switch (project.getType()){
            case AGILE:
                objectLvl1Serie = projectRepository.getEpicsState(idProject);
                objectLvl2Serie = projectRepository.getUserStoriesState(idProject);
                break;
            case NOT_AGILE:
                objectLvl1Serie = projectRepository.getFeaturesState(idProject);
                objectLvl2Serie = projectRepository.getUseCasesState(idProject);
                break;
        }
        stats.put("objectLvl1Serie", objectLvl1Serie);
        stats.put("objectLvl2Serie", objectLvl2Serie);
        requirementsList = projectRepository.getRequirementsState(idProject);
        stats.put("requirements", requirementsList);
        return stats;
    }

    /*private void getObjectDomList(List<ObjectEntity> objects, List<ObjectDom> objectDomList) {
        for (ObjectEntity objectEntity : objects){
            ObjectDom object = new ObjectDom();
            try {
                FeatureDom feature = featureService.getFeature(objectEntity.getIdobject());
                object.setFeatureDom(feature);
            }catch (Exception e){
                try {
                    EpicDom epic = epicService.getEpic(objectEntity.getIdobject());
                    object.setEpicDom(epic);
                }
                catch (Exception f){
                    try {
                        UseCaseDom useCase = useCaseService.getUseCase(objectEntity.getIdobject());
                        object.setUseCaseDom(useCase);
                    }
                    catch (Exception g){
                        try {
                            UserStoryDom userStory = userStoryService.getUserStory(objectEntity.getIdobject());
                            object.setUserStoryDom(userStory);
                        }
                        catch (Exception h){
                            RequirementDom requirement = requirementService.getRequirement(objectEntity.getIdobject());
                            object.setRequirementDom(requirement);
                        }
                    }
                }
            }
            object.setIdObject(objectEntity.getIdobject());
            object.setProject(projectConverter.toDomain(objectEntity.getProject()));
            object.setTraceability(new ArrayList<>());
            for(ObjectEntity trace : objectEntity.getTraced()){
                TraceDom traceDom = traceabilityService.getTraceability(trace.getIdobject());
                object.getTraceability().add(traceDom);
            }
            objectDomList.add(object);
        }
    }*/

}
