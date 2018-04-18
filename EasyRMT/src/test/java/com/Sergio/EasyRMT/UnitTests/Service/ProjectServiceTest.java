package com.Sergio.EasyRMT.UnitTests.Service;

import com.Sergio.EasyRMT.Domain.ProjectDom;
import com.Sergio.EasyRMT.Domain.RequirementTypeDom;
import com.Sergio.EasyRMT.Model.Project;
import com.Sergio.EasyRMT.Model.RequirementType;
import com.Sergio.EasyRMT.Repository.ProjectRepository;
import com.Sergio.EasyRMT.Repository.ReqTypeRepository;
import com.Sergio.EasyRMT.Service.Converter.ProjectConverter;
import com.Sergio.EasyRMT.Service.Converter.ReqTypeConverter;
import com.Sergio.EasyRMT.Service.ProjectService;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.context.annotation.Description;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
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

    @BeforeEach
    public void initMocks(){
        projectRepository = mock(ProjectRepository.class);
        reqTypeRepository = mock(ReqTypeRepository.class);
        projectConverter = mock(ProjectConverter.class);
        reqTypeConverter = mock(ReqTypeConverter.class);
    }

    @Test
    @DisplayName("Get a list of projects")
    public void getListProjects_ReturnsList(){
        List<Project> projectList = mock(List.class);
        List<ProjectDom> projectDomList = mock(List.class);
        doReturn(projectList).when(projectRepository).findAll();
        doReturn(projectDomList).when(projectConverter).toDomain(projectList);

        ProjectService projectService = createProjectService();

        //Test conditions
        assertTrue(projectService.getProjects().equals(projectDomList));
        //Verify that method calls one to project repository
        verify(projectRepository, times(1)).findAll();

        //Verify that method calls one time to project converter
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
        //Verify that method calls one to project repository
        verify(projectRepository, times(1)).findByIdProject(anyInt());

        //Verify that method calls one time to project converter
        verify(projectConverter, times(1)).toDomain(project);
    }

    @Test
    @DisplayName("DeleteProject deletes a project")
    public void deleteProject_IdProjectProvided_DeletesProject(){
        doReturn(true).doReturn(false).when(projectRepository).existsByIdProject(anyInt());
        doNothing().when(projectRepository).deleteProjectByIdProject(anyInt());

        ProjectService projectService = createProjectService();

        assertTrue(projectService.deleteProject(7));
        //Verify that method calls two times to project repository.existsByIdProject
        verify(projectRepository, times(2)).existsByIdProject(anyInt());
        //Verify that projectRepository.deleteProjectByIdProject() is called once
        verify(projectRepository, times(1)).deleteProjectByIdProject(anyInt());

    }

    @Test
    @DisplayName("Delete Project fails when project does not exists in DB")
    public void deleteProject_IdProjectNotExist_ReturnFalse(){
        doReturn(false).when(projectRepository).existsByIdProject(anyInt());

        ProjectService projectService = createProjectService();

        //Test conditions
        assertFalse(projectService.deleteProject(7));
        //Verify that method calls one time to project repository.existsByIdProject
        verify(projectRepository, times(1)).existsByIdProject(anyInt());
        //Verify that projectRepository.deleteProjectByIdProject() is never called
        verify(projectRepository, times(0)).deleteProjectByIdProject(anyInt());

    }

    @Test
    @DisplayName("Create Project persists a project from a project provided")
    public void createProject_ProjectProvided_ReturnsProjectPersisted(){
        ProjectDom projectDom = mock(ProjectDom.class);
        Project project = mock(Project.class);
        ArrayList<String> reqTypesString = new ArrayList<>();
        reqTypesString.add("1");
        reqTypesString.add("2");

        when(projectDom.getStringReqTypes()).thenReturn(reqTypesString);
        when(projectConverter.toModel(projectDom)).thenReturn(project);
        when(projectRepository.save(project)).thenReturn(project);
        when(projectConverter.toDomain(project)).thenReturn(projectDom);

        ProjectService projectService = createProjectService();

        //Test conditions
        assertTrue(projectService.createProject(projectDom).equals(projectDom));
        //Verify that projectConverter.toModel has been called once
        verify(projectConverter, times(1)).toModel(projectDom);
        //verify that projectRepository.save has been called once
        verify(projectRepository, times(1)).save(project);
        //verify that projectConverter.toDomain has been called once
        verify(projectConverter, times(1)).toDomain(project);
    }

    @Test
    @DisplayName("Update Project persist an updated project provided")
    public void updateProject_ProjectWithChangesProvided_UpdatesProject(){
        ProjectDom projectDom = mock(ProjectDom.class);
        Project project = mock(Project.class);
        ProjectDom projectParam =new ProjectDom();
        ArrayList<RequirementType> reqTypes = mock(ArrayList.class);

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

        ProjectService projectService = createProjectService();
        //Test conditions
        assertTrue(projectService.updateProject(7,projectParam).equals(projectDom));
        //verify projectRepository.findByIdProject has been called once
        verify(projectRepository,times(1)).findByIdProject(anyInt());
        //verify projectRepository.save has been called once
        verify(projectRepository,times(1)).save(any(Project.class));
        //verify projectConverter.toModel has been called once
        verify(projectConverter,times(1)).toModel(any(ProjectDom.class));
        //verify projectConverter.toDomain has been called two times
        verify(projectConverter,times(2)).toDomain(any(Project.class));

    }

    @Test
    @Description("Get Requirement types returns a list of requirement types")
    public void getReqTypes_ReturnsList(){
        List<RequirementType> requirementTypeList = mock(List.class);
        List<RequirementTypeDom> requirementTypeDomList = mock(List.class);
        when(reqTypeRepository.findAll()).thenReturn(requirementTypeList);
        when(reqTypeConverter.toDomain(requirementTypeList)).thenReturn(requirementTypeDomList);

        ProjectService projectService = createProjectService();

        //Test conditions
        assertTrue(projectService.getReqTypes().equals(requirementTypeDomList));
        //Verify reqTypeRepositoru.findAll has been called once
        verify(reqTypeRepository, times(1)).findAll();
        //Verify reTypeConverter.ToDomain has been called once
        verify(reqTypeConverter, times(1)).toDomain(requirementTypeList);
    }

    private ProjectService createProjectService(){
        return new ProjectService(projectRepository, reqTypeRepository, projectConverter, reqTypeConverter);
    }
}
