package com.cmarchive.bank.integration;

import com.cmarchive.bank.BankApplication;
import com.cmarchive.bank.domain.User;
import com.cmarchive.bank.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = BankApplication.class)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class UserControllerIntegrationTest {

    private static final long ID_USER = 1L;
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserService userService;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
    }

    @WithMockUser(username = "test", roles = {"ADMIN"})
    @Test
    public void givenUsers_whenGetList_thenStatus200AndReturnUsers() throws Exception {
        MvcResult result = mockMvc.perform(get("/users/list")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(view().name("users/list"))
                .andReturn();

        List<User> users = (List<User>) result.getModelAndView().getModel().get("users");
        assertThat(users).hasSize(2);
    }

    @WithMockUser(username = "test", roles = {"ADMIN"})
    @Test
    public void givenId_whenGetView_thenStatus200AndReturnUser() throws Exception {
        MvcResult result = mockMvc.perform(get("/users/{id}", ID_USER)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(view().name("users/view"))
                .andReturn();

        User user = (User) result.getModelAndView().getModel().get("user");
        assertThat(user).isNotNull();
        assertThat(user.getNom()).isEqualToIgnoringCase("marchive");
        assertThat(user.getPrenom()).isEqualToIgnoringCase("cyril");
    }

    @WithMockUser(username = "test", roles = {"ADMIN"})
    @Test
    public void givenUserWithId_whenPutModifier_thenStatus3xxAndModifiedUser() throws Exception {
        User user = creerUser();

        mockMvc.perform(put("/users/modifier")
                .contentType(MediaType.APPLICATION_JSON)
                .param("nom", "nom")
                .param("password", "password")
                .param("id", ID_USER + "")
                .content(objectMapper.writeValueAsBytes(user)))
                .andExpect(status().is3xxRedirection());

        List<User> users = userService.list();
        assertThat(users).hasSize(2);
        boolean userPresent = users.stream()
                .anyMatch(u -> u.getNom().equals("nom"));
        assertThat(userPresent).isTrue();
    }

    @WithMockUser(username = "test", roles = {"ADMIN"})
    @Test
    public void givenUserIdNull_whenPutModifier_thenStatus3xxAndCreateddUser() throws Exception {
        User user = creerUser();

        mockMvc.perform(put("/users/modifier")
                .contentType(MediaType.APPLICATION_JSON)
                .param("nom", "nom")
                .param("password", "password")
                .content(objectMapper.writeValueAsBytes(user)))
                .andExpect(status().is3xxRedirection());

        List<User> users = userService.list();
        assertThat(users).hasSize(3);
        boolean userPresent = users.stream()
                .anyMatch(u -> u.getNom().equals("nom"));
        assertThat(userPresent).isTrue();
    }

    @WithMockUser(username = "test", roles = {"ADMIN"})
    @Test
    public void givenId_whenDeleteSupprimer_thenStatus3xxAndDeletedUser() throws Exception {
        mockMvc.perform(delete("/users/supprimer/{id}", ID_USER)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is3xxRedirection());

        List<User> users = userService.list();
        assertThat(users).hasSize(1);
        boolean userPresent = users.stream()
                .anyMatch(u -> u.getNom().equalsIgnoreCase("marchive"));
        assertThat(userPresent).isFalse();
    }

    private User creerUser() {
        User user = new User()
                .setEmail("email")
                .setId(ID_USER)
                .setNom("nom")
                .setPrenom("prenom");

        return user;
    }
}
