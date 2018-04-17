package com.Sergio.EasyRMT.UnitTests.Controller;

import com.Sergio.EasyRMT.Controller.MainController;
import com.Sergio.EasyRMT.Domain.ProjectDom;
import com.Sergio.EasyRMT.UnitTests.Service.ProjectService;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
public class MainControllerTest {

    @Mock
    ProjectService projectService;

    @BeforeEach
    public void initMocks(){
        projectService = mock(ProjectService.class);
    }

    @Test
    @DisplayName("dashboard method returns a modelAndView object")
    public void dashboard_MAVrequested_MAVReturned(){
        ProjectDom p1 = mock(ProjectDom.class);
        ProjectDom p2 = mock(ProjectDom.class);
        List<ProjectDom> projectDomList = new ArrayList<>();
        projectDomList.add(p1);
        projectDomList.add(p2);

        ModelAndView expected = new ModelAndView("dashboard");
        expected.addObject("projectList", projectDomList);

        when(projectService.getProjects()).thenReturn(projectDomList);

        MainController mainController = createMainController();

        ModelAndView obtained = mainController.dashboard();

        //Test conditions
        assertEquals(expected.getView(),obtained.getView());
        assertFalse(obtained.getModel().isEmpty());
        //Verify project service has been called
        verify(projectService,times(1)).getProjects();
    }

    private MainController createMainController(){
        return new MainController(projectService);
    }
}