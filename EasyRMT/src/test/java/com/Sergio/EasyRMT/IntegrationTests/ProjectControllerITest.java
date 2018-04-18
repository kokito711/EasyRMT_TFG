package com.Sergio.EasyRMT.IntegrationTests;

import com.Sergio.EasyRMT.Model.Project;
import com.Sergio.EasyRMT.Repository.ProjectRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProjectControllerITest {
    @Autowired
    private WebApplicationContext wac;
    @Autowired
    ProjectRepository projectRepository;
    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @AfterAll
    public void rollback(){
        List<Project> projectList = projectRepository.findAll();
        int size = projectList.size();
        Project project = projectList.get(size-1);
        int id = project.getIdProject();
        projectRepository.deleteProjectByIdProject(id);
    }

    @Test
    @DisplayName("Endpoint test for createProject view")
    public void getCreateProjectView() throws Exception {
        this.mockMvc.perform(get("/createProject"))
                .andExpect(status().isOk())
                .andExpect(view().name("createProject"));
    }

    @Test
    @DisplayName("Endpoint test for createProject")
    public void createProject() throws Exception {
        this.mockMvc.perform(post("/projects")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .param("name", "Project_Test")
                                .param("type", "AGILE")
                                .param("description", "projectTest")
                                .param("stringReqTypes", "1,2")
                            )
                            .andExpect(status().isOk())
                            .andExpect(view().name("project"))
                            .andExpect(model().attribute("project",hasProperty("name",notNullValue())))
                            .andExpect(model().attribute("project",hasProperty("description")))
                            .andExpect(model().attribute("project", hasProperty("type", notNullValue())));
    }

    @Test
    @DisplayName("Endpoint test for updateProject view")
    public void getUpdateProjectView() throws Exception {
        this.mockMvc.perform(get("/updateProject/8"))
                .andExpect(status().isOk())
                .andExpect(view().name("updateProject"));
    }

    @Test
    @DisplayName("Endpoint test for updateProject")
    public void updateProject() throws Exception {
        List<Project> projectList = projectRepository.findAll();
        int size = projectList.size();
        Project project = projectList.get(size-1);
        int id = project.getIdProject();
        this.mockMvc.perform(post("/project/"+String.valueOf(id))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("description", "projectTestUpdated")
                .param("stringReqTypes", "3,4")
        )
                .andExpect(status().isOk())
                .andExpect(view().name("project"))
                .andExpect(model().attribute("project",hasProperty("description")))
                .andExpect(model().attribute("project", hasProperty("stringReqTypes")));
    }

    @Test
    @DisplayName("Endpoint test for project view")
    public void getProjectView() throws Exception {
        this.mockMvc.perform(get("/project/8"))
                .andExpect(status().isOk())
                .andExpect(view().name("project"));
    }

    @Test
    @DisplayName("Endpoint test for delete project correct request")
    public void deleteProject_ReturnsOk() throws Exception {
        List<Project> projectList = projectRepository.findAll();
        int size = projectList.size();
        Project project = projectList.get(size-1);
        int id = project.getIdProject();
        this.mockMvc.perform(delete("/project/"+String.valueOf(id))).andExpect(status().isOk());
    }
    @Test
    @DisplayName("Endpoint test for delete project error request")
    public void deleteProject_ReturnsError() throws Exception {
        List<Project> projectList = projectRepository.findAll();
        int size = projectList.size();
        Project project = projectList.get(size-1);
        int id = project.getIdProject()+1;
        this.mockMvc.perform(delete("/project/"+String.valueOf(id)))
                .andExpect(status().isInternalServerError());
    }
}
