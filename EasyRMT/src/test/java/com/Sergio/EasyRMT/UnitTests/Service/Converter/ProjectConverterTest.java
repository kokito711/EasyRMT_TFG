package com.Sergio.EasyRMT.UnitTests.Service.Converter;

import com.Sergio.EasyRMT.Domain.ProjectDom;
import com.Sergio.EasyRMT.Domain.RequirementTypeDom;
import com.Sergio.EasyRMT.Model.Project;
import com.Sergio.EasyRMT.Model.RequirementType;
import com.Sergio.EasyRMT.Model.types.ProjectType;
import com.Sergio.EasyRMT.Repository.ReqTypeRepository;
import com.Sergio.EasyRMT.Service.Converter.ProjectConverter;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
public class ProjectConverterTest {

    @Mock
    private ReqTypeRepository reqTypeRepository;


    @BeforeEach
    public void initMocks(){
        reqTypeRepository = mock(ReqTypeRepository.class);
    }

    @Test
    @DisplayName("Method toDomain receives a list of Project and returns a list of ProjectDom")
    public void toDomain_ProjectListProvided_ProjectDomListReturned(){
        RequirementType requirementType = new RequirementType();
        requirementType.setIdType(1);
        requirementType.setName("reqType");
        ArrayList<RequirementType> listReqType = new ArrayList<>();
        listReqType.add(requirementType);

        Project project1 = new Project();
        project1.setIdProject(1);
        project1.setDescription("Description");
        project1.setType(ProjectType.AGILE);
        project1.setName("Project 1");
        project1.setRequirementTypes(listReqType);

        Project project2 = new Project();
        project2.setIdProject(1);
        project2.setDescription("Description");
        project2.setType(ProjectType.AGILE);
        project2.setName("Project 2");
        project2.setRequirementTypes(listReqType);

        ArrayList<Project> projectList = new ArrayList<>();
        projectList.add(project1);
        projectList.add(project2);

        ProjectConverter projectConverter = createProjectConverter();

        List<ProjectDom> list = projectConverter.toDomain(projectList);
        //Test conditions
        assertNotNull(list);
        assertTrue(list.toArray().length == 2);
        assertFalse(list.isEmpty());
    }

    @Test
    @DisplayName("Method toDomain receives a Project and returns a  ProjectDom")
    public void toDomain_ProjectProvided_ProjectDomReturned(){
        RequirementType requirementType = new RequirementType();
        requirementType.setIdType(1);
        requirementType.setName("reqType");
        ArrayList<RequirementType> listReqType = new ArrayList<>();
        listReqType.add(requirementType);

        Project project1 = new Project();
        project1.setIdProject(1);
        project1.setDescription("Description");
        project1.setType(ProjectType.AGILE);
        project1.setName("Project 1");
        project1.setRequirementTypes(listReqType);

        RequirementTypeDom requirementTypeDom = new RequirementTypeDom(1,"reqType");
        ArrayList<RequirementTypeDom> listReqTypeDom = new ArrayList<>();
        listReqTypeDom.add(requirementTypeDom);

        ProjectDom projectDomExpected = new ProjectDom(1,"Project 1",
                "Description",ProjectType.AGILE,listReqTypeDom);

        ProjectConverter projectConverter = createProjectConverter();

        ProjectDom projectDom = projectConverter.toDomain(project1);
        //Test conditions
        assertNotNull(projectDom);
        assertEquals(projectDomExpected,projectDom);
    }

    @Test
    @DisplayName("Method toModel receives a ProjectDom and returns a Project")
    public void toDomain_ProjectDomProvided_ProjectReturned(){
        RequirementType requirementType = mock(RequirementType.class);
        ArrayList<RequirementType> listReqType = new ArrayList<>();
        listReqType.add(requirementType);

        Project projectExpected = new Project();
        projectExpected.setDescription("Description");
        projectExpected.setType(ProjectType.AGILE);
        projectExpected.setName("Project 1");
        projectExpected.setRequirementTypes(listReqType);

        RequirementTypeDom requirementTypeDom = new RequirementTypeDom(1,"reqType");
        ArrayList<RequirementTypeDom> listReqTypeDom = new ArrayList<>();
        listReqTypeDom.add(requirementTypeDom);

        ProjectDom projectDom = new ProjectDom();
        projectDom.setName("Project 1");
        projectDom.setDescription("Description");
        projectDom.setType(ProjectType.AGILE);
        projectDom.setRequirementTypes(listReqTypeDom);

        when(reqTypeRepository.findByIdType(requirementTypeDom.getIdType())).thenReturn(Optional.of(requirementType));
        ProjectConverter projectConverter = createProjectConverter();

        Project project = projectConverter.toModel(projectDom);
        //Test conditions
        assertNotNull(project);
        assertEquals(projectExpected,project);
    }

    private ProjectConverter createProjectConverter(){
        return new ProjectConverter(reqTypeRepository);
    }
}
