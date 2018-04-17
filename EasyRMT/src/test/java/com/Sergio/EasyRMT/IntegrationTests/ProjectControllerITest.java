package com.Sergio.EasyRMT.IntegrationTests;

import com.Sergio.EasyRMT.Domain.ProjectDom;
import com.Sergio.EasyRMT.Domain.RequirementTypeDom;
import com.Sergio.EasyRMT.Model.types.ProjectType;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProjectControllerITest {
    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    @DisplayName("Endpoint test for createProject view")
    public void getCreateProjectView() throws Exception {
        this.mockMvc.perform(get("/createProject"))
                .andExpect(status().isOk())
                .andExpect(view().name("createProject"));
    }

    /*@Test
    @DisplayName("Endpoint test for createProject")
    public void createProject() throws Exception {
        ArrayList<String> reqTypeString = new ArrayList<>();
        reqTypeString.add("1");
        reqTypeString.add("2");

        ProjectDom projectDom = new ProjectDom();
        projectDom.setName("Project_Test");
        projectDom.setType(ProjectType.AGILE);
        projectDom.setDescription("projectTest");
        projectDom.setStringReqTypes(reqTypeString);

        this.mockMvc.perform(post("/projects")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .sessionAttr("project", projectDom)
                            )
                            .andExpect(status().isOk())
                            .andExpect(view().name("project"))
                            .andExpect(model().attribute("project",hasProperty("name",notNullValue())))
                            .andExpect(model().attribute("project",hasProperty("description")))
                            .andExpect(model().attribute("project", hasProperty("projectType", notNullValue())));
    }*/

    @Test
    @DisplayName("Endpoint test for updateProject view")
    public void getUpdateProjectView() throws Exception {
        this.mockMvc.perform(get("/updateProject/8"))
                .andExpect(status().isOk())
                .andExpect(view().name("updateProject"));
    }

    @Test
    @DisplayName("Endpoint test for project view")
    public void getProjectView() throws Exception {
        this.mockMvc.perform(get("/project/8"))
                .andExpect(status().isOk())
                .andExpect(view().name("project"));
    }


}
