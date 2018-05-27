package com.Sergio.EasyRMT.IntegrationTests;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MainControllerITest {
    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;


    @Before
    public void setup() throws Exception {

        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .apply(springSecurity())
                .build();
    }

    @Test
    @DisplayName("Endpoint test for index page")
    public void getIndexPage() throws Exception {
        this.mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"));
    }

    @Test
    @DisplayName("Endpoint test for dashboard page")
    @WithMockUser(username="User", roles={"PROJECT_MANAGER"})
    public void getDashboardPage() throws Exception {
         this.mockMvc.perform(get("/dashboard"))
                .andExpect(status().isOk())
                .andExpect(view().name("dashboard"))
                .andDo(print());
    }

    @Test
    @DisplayName("Endpoint test for admin dashboard page")
    @WithMockUser(username="Admin", roles={"ADMIN"})
    public void getAdminDashboardPage() throws Exception {
        this.mockMvc.perform(get("admin/dashboard"))
                .andExpect(status().isOk())
                .andExpect(view().name("dashboard"))
                .andDo(print());
    }
}
