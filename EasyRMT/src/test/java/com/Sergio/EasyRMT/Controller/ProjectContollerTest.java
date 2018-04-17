package com.Sergio.EasyRMT.Controller;

import com.Sergio.EasyRMT.Service.ProjectService;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.mockito.Mockito.mock;

@RunWith(SpringJUnit4ClassRunner.class)
public class ProjectContollerTest {

    @Mock
    ProjectService projectService;

    @BeforeEach
    public void initMocks(){
        projectService = mock(ProjectService.class);
    }
    @Test
    @DisplayName("createProjectView method returns a modelAndView object")
    public void createProjectView_MAVProvided_ReturnsMAV(){

    }

    private MainController createMainController(){
        return new MainController(projectService);
    }
}
