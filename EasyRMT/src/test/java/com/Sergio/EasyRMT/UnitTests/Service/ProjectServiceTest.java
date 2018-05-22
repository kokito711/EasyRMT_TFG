package com.Sergio.EasyRMT.UnitTests.Service;

import com.Sergio.EasyRMT.Domain.ProjectDom;
import com.Sergio.EasyRMT.Domain.RequirementTypeDom;
import com.Sergio.EasyRMT.Model.Group;
import com.Sergio.EasyRMT.Model.Project;
import com.Sergio.EasyRMT.Model.RequirementType;
import com.Sergio.EasyRMT.Model.types.ProjectType;
import com.Sergio.EasyRMT.Repository.GroupRepository;
import com.Sergio.EasyRMT.Repository.ObjectRepository;
import com.Sergio.EasyRMT.Repository.ProjectRepository;
import com.Sergio.EasyRMT.Repository.ReqTypeRepository;
import com.Sergio.EasyRMT.Service.Converter.ProjectConverter;
import com.Sergio.EasyRMT.Service.Converter.ReqTypeConverter;
import com.Sergio.EasyRMT.Service.*;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.*;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@RunWith(SpringJUnit4ClassRunner.class)
public class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;
    @Mock
    private ReqTypeRepository reqTypeRepository;
    @Mock
    private ProjectConverter projectConverter;
    @Mock
    private ReqTypeConverter reqTypeConverter;
    @Mock
    private DocumentService documentService;
    @Mock
    private GroupRepository groupRepository;
    @Mock
    private ObjectRepository objectRepository;
    @Mock
    private TraceabilityService traceabilityService;
    @Mock
    private FeatureService featureService;
    @Mock
    private EpicService epicService;
    @Mock
    private UseCaseService useCaseService;
    @Mock
    private UserStoryService userStoryService;
    @Mock
    private RequirementService requirementService;

    @BeforeEach
    public void initMocks(){
        projectRepository = mock(ProjectRepository.class);
        reqTypeRepository = mock(ReqTypeRepository.class);
        projectConverter = mock(ProjectConverter.class);
        reqTypeConverter = mock(ReqTypeConverter.class);
        documentService = mock(DocumentService.class);
        groupRepository = mock(GroupRepository.class);
        objectRepository = mock(ObjectRepository.class);
        traceabilityService = mock(TraceabilityService.class);
        featureService = mock(FeatureService.class);
        epicService = mock(EpicService.class);
        useCaseService = mock(UseCaseService.class);
        userStoryService = mock(UserStoryService.class);
        requirementService = mock(RequirementService.class);
    }

    @Test
    @DisplayName("Get a list of projects")
    public void getListProjects_ReturnsList(){
        List<Project> projectList = mock(List.class);
        List<ProjectDom> projectDomList = mock(List.class);
        doReturn(projectList).when(projectRepository).findByGroup(1);
        doReturn(projectDomList).when(projectConverter).toDomain(projectList);

        ProjectService projectService = createProjectService();

        //Test conditions
        assertTrue(projectService.getProjects(1).equals(projectDomList));
        verify(projectRepository, times(1)).findByGroup(1);
        verify(projectConverter, times(1)).toDomain(projectList);
    }

    @Test
    @DisplayName("Get a project returns project")
    public void getProject_IdPersistedProvided_ReturnsProject(){
        Project project = mock(Project.class);
        ProjectDom projectDom = mock(ProjectDom.class);
        when(projectRepository.findByIdProject(anyInt())).thenReturn(Optional.of(project));
        doReturn(projectDom).when(projectConverter).toDomain(project);

        ProjectService projectService = createProjectService();

        //Test conditions
        assertTrue( projectService.getProject(7).equals(projectDom));
        verify(projectRepository, times(1)).findByIdProject(anyInt());
        verify(projectConverter, times(1)).toDomain(project);
    }

    @Test
    @DisplayName("DeleteProject deletes a project")
    public void deleteProject_IdProjectProvided_DeletesProject(){
        doReturn(true).doReturn(false).when(projectRepository).existsByIdProject(anyInt());
        doNothing().when(projectRepository).deleteProjectByIdProject(anyInt());
        doNothing().when(documentService).deleteFiles(7, null);

        ProjectService projectService = createProjectService();

        assertTrue(projectService.deleteProject(7));
        verify(projectRepository, times(2)).existsByIdProject(anyInt());
        verify(projectRepository, times(1)).deleteProjectByIdProject(anyInt());
        verify(documentService, times(1)).deleteFiles(7, null);
    }

    @Test
    @DisplayName("Delete Project fails when project does not exists in DB")
    public void deleteProject_IdProjectNotExist_ReturnFalse(){
        doReturn(false).when(projectRepository).existsByIdProject(anyInt());

        ProjectService projectService = createProjectService();

        assertFalse(projectService.deleteProject(7));
        verify(projectRepository, times(1)).existsByIdProject(anyInt());
        verify(projectRepository, times(0)).deleteProjectByIdProject(anyInt());
        verify(documentService, times(0)).deleteFiles(7, null);

    }

    @Test
    @DisplayName("Create Project persists a project from a project provided")
    public void createProject_ProjectProvided_ReturnsProjectPersisted(){
        ProjectDom projectDom = mock(ProjectDom.class);
        Project project = mock(Project.class);
        ArrayList<String> reqTypesString = new ArrayList<>();
        reqTypesString.add("1");
        reqTypesString.add("2");
        Group group = mock(Group.class);

        when(projectDom.getStringReqTypes()).thenReturn(reqTypesString);
        when(projectDom.getGroupId()).thenReturn(1);
        when(projectConverter.toModel(projectDom)).thenReturn(project);
        when(projectRepository.save(project)).thenReturn(project);
        when(projectConverter.toDomain(project)).thenReturn(projectDom);
        when(groupRepository.findOne(1)).thenReturn(group);

        ProjectService projectService = createProjectService();

        assertTrue(projectService.createProject(projectDom).equals(projectDom));
        verify(projectConverter, times(1)).toModel(projectDom);
        verify(projectRepository, times(1)).save(project);
        verify(projectConverter, times(1)).toDomain(project);
        verify(groupRepository, times(1)).findOne(1);
    }

    @Test
    @DisplayName("Update Project persist an updated project provided")
    public void updateProject_ProjectWithChangesProvided_UpdatesProject(){
        ProjectDom projectDom = mock(ProjectDom.class);
        Project project = mock(Project.class);
        ProjectDom projectParam =new ProjectDom();
        List<RequirementType> reqTypes = mock(List.class);
        Group group = mock(Group.class);
        ArrayList<String> reqTypesString = new ArrayList<>();
        reqTypesString.add("1");
        reqTypesString.add("2");
        projectParam.setDescription("description1");
        projectParam.setStringReqTypes(reqTypesString);

        when(projectRepository.findByIdProject(anyInt())).thenReturn(Optional.of(project));
        when(projectConverter.toDomain(project)).thenReturn(projectDom);
        when(projectDom.getIdProject()).thenReturn(7);
        when(projectDom.getDescription()).thenReturn("description");
        when(projectDom.getStringReqTypes()).thenReturn(reqTypesString);
        when(projectConverter.toModel(projectDom)).thenReturn(project);
        when(project.getDescription()).thenReturn("description");
        when(project.getRequirementTypes()).thenReturn(reqTypes);
        when(projectRepository.save(project)).thenReturn(project);
        when(project.getGroup()).thenReturn(group);
        when(group.getGroup_id()).thenReturn(1);
        when(projectDom.getGroupId()).thenReturn(2);
        when(groupRepository.findOne(1)).thenReturn(group);

        ProjectService projectService = createProjectService();
        //Test conditions
        assertTrue(projectService.updateProject(7,projectParam).equals(projectDom));
        verify(projectRepository,times(1)).findByIdProject(anyInt());
        verify(projectRepository,times(1)).save(any(Project.class));
        verify(projectConverter,times(1)).toModel(any(ProjectDom.class));
        verify(projectConverter,times(2)).toDomain(any(Project.class));

    }

    @Test
    @DisplayName("Get Requirement types returns a list of requirement types")
    public void getReqTypes_ReturnsList(){
        List<RequirementType> requirementTypeList = mock(List.class);
        List<RequirementTypeDom> requirementTypeDomList = mock(List.class);
        when(reqTypeRepository.findAll()).thenReturn(requirementTypeList);
        when(reqTypeConverter.toDomain(requirementTypeList)).thenReturn(requirementTypeDomList);

        ProjectService projectService = createProjectService();

        //Test conditions
        assertTrue(projectService.getReqTypes().equals(requirementTypeDomList));
        verify(reqTypeRepository, times(1)).findAll();
        verify(reqTypeConverter, times(1)).toDomain(requirementTypeList);
    }

    @Test
    @DisplayName("Get Requirement type returns a  requirement type")
    public void getReqType_ReqTypeIdProvided_ReturnsRequirementType(){
        RequirementType requirementType = mock(RequirementType.class);
        RequirementTypeDom requirementTypeDom = mock(RequirementTypeDom.class);
        when(reqTypeRepository.findByIdType(anyInt())).thenReturn(Optional.of(requirementType));
        doReturn(requirementTypeDom).when(reqTypeConverter).toDomain(requirementType);

        ProjectService projectService = createProjectService();

        //Test conditions
        assertTrue( projectService.getReqType(7).equals(requirementTypeDom));
        verify(reqTypeRepository, times(1)).findByIdType(anyInt());
        verify(reqTypeConverter, times(1)).toDomain(requirementType);
    }

    @Test
    @DisplayName("Get Traced stats type returns a map with traceability stats")
    public void getTracedStats_ProjectIdProvided_MapReturned(){
        Integer tracedReqs = 1;
        Integer notTracedReqs = 1;
        when(projectRepository.getTracedObjects(1)).thenReturn(tracedReqs);
        when(projectRepository.getNotTracedObjects(1)).thenReturn(notTracedReqs);

        ProjectService projectService = createProjectService();
        Map<String,Integer> expected = new HashMap<>();
        expected.put("tracedReqs", tracedReqs);
        expected.put("notTracedReqs", notTracedReqs);

        //Test conditions
        assertEquals(projectService.getTracedStats(1), expected);
        verify(projectRepository, times(1)).getTracedObjects(1);
        verify(projectRepository, times(1)).getNotTracedObjects(1);
    }

    @Test
    @DisplayName("Get State stats type returns a map with Agile State stats")
    public void getStateStats_AgileProjectIdProvided_MapReturned(){
        List<Integer> requirementsList = mock(List.class);
        List<Integer> objectLvl1Serie= mock(List.class);
        List<Integer> objectLvl2Serie= mock(List.class);
        Project project = mock(Project.class);
        when(projectRepository.findByIdProject(1)).thenReturn(Optional.of(project));
        when(project.getType()).thenReturn(ProjectType.AGILE);
        when(projectRepository.getEpicsState(1)).thenReturn(objectLvl1Serie);
        when(projectRepository.getUserStoriesState(1)).thenReturn(objectLvl2Serie);
        when(projectRepository.getRequirementsState(1)).thenReturn(requirementsList);

        ProjectService projectService = createProjectService();
        Map<String,List> expected = new HashMap<>();
        expected.put("objectLvl1Serie", objectLvl1Serie);
        expected.put("objectLvl2Serie", objectLvl2Serie);
        expected.put("requirements", requirementsList);

        //Test conditions
        assertEquals(projectService.getStateStats(1), expected);
        verify(projectRepository, times(1)).findByIdProject(1);
        verify(projectRepository, times(1)).getEpicsState(1);
        verify(projectRepository, times(1)).getUserStoriesState(1);
        verify(projectRepository, times(1)).getRequirementsState(1);
        verify(projectRepository, times(0)).getFeaturesState(1);
        verify(projectRepository, times(0)).getUseCasesState(1);
    }

    @Test
    @DisplayName("Get State stats type returns a map with traditional State stats")
    public void getStateStats_TraditionalProjectIdProvided_MapReturned(){
        List<Integer> requirementsList = mock(List.class);
        List<Integer> objectLvl1Serie= mock(List.class);
        List<Integer> objectLvl2Serie= mock(List.class);
        Project project = mock(Project.class);
        when(projectRepository.findByIdProject(1)).thenReturn(Optional.of(project));
        when(project.getType()).thenReturn(ProjectType.NOT_AGILE);
        when(projectRepository.getFeaturesState(1)).thenReturn(objectLvl1Serie);
        when(projectRepository.getUseCasesState(1)).thenReturn(objectLvl2Serie);
        when(projectRepository.getRequirementsState(1)).thenReturn(requirementsList);

        ProjectService projectService = createProjectService();
        Map<String,List> expected = new HashMap<>();
        expected.put("objectLvl1Serie", objectLvl1Serie);
        expected.put("objectLvl2Serie", objectLvl2Serie);
        expected.put("requirements", requirementsList);

        //Test conditions
        assertEquals(projectService.getStateStats(1), expected);
        verify(projectRepository, times(1)).findByIdProject(1);
        verify(projectRepository, times(0)).getEpicsState(1);
        verify(projectRepository, times(0)).getUserStoriesState(1);
        verify(projectRepository, times(1)).getRequirementsState(1);
        verify(projectRepository, times(1)).getFeaturesState(1);
        verify(projectRepository, times(1)).getUseCasesState(1);
    }

    private ProjectService createProjectService(){
        return new ProjectService(projectRepository, reqTypeRepository, projectConverter, reqTypeConverter,
                documentService, groupRepository, objectRepository, traceabilityService, featureService,
                epicService, useCaseService, userStoryService, requirementService);
    }
}
