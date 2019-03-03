package com.cmarchive.bank.controller;

import com.cmarchive.bank.domain.User;
import com.cmarchive.bank.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {
    private final static Long ID_USER = 1L;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    public void list_nominal() throws Exception {
        User user = creerUser();
        List<User> users = Collections.singletonList(user);
        given(userService.list()).willReturn(users);

        mockMvc.perform(get("/users/list")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(model().attribute("users", users))
                .andExpect(view().name("users/list"));

        then(userService).should().list();
    }


    public void view_nominal() throws Exception {
        User user = creerUser();
        given(userService.get(ID_USER)).willReturn(user);

        mockMvc.perform(get("/users/{id}", ID_USER)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(model().attribute("user", user))
                .andExpect(view().name("users/view"));

        then(userService).should().get(ID_USER);
    }

    @Test
    public void modifier_nominal() throws Exception {
        User user = creerUser();
        given(userService.get(ID_USER)).willReturn(user);

        mockMvc.perform(put("/users/modifier")
                .content(objectMapper.writeValueAsBytes(user))
                .param("nom", "nom")
                .param("password", "password")
                .param("id", String.valueOf(ID_USER))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/users/list"));

        then(userService).should().get(ID_USER);
        then(userService).should().save(user);
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void modifier_IdUserNull() throws Exception {
        User user = creerUser();
        given(userService.get(ID_USER)).willReturn(user);

        mockMvc.perform(put("/users/modifier")
                .content(objectMapper.writeValueAsBytes(user))
                .param("nom", "nom")
                .param("password", "password")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/users/list"));

        then(userService).should(never()).get(anyLong());
        then(userService).should().encodePassword(any(User.class));
        then(userService).should().save(any(User.class));
    }

    @Test
    public void modifier_NomNull() throws Exception {
        User user = creerUser();
        given(userService.get(ID_USER)).willReturn(user);

        mockMvc.perform(put("/users/modifier")
                .content(objectMapper.writeValueAsBytes(user))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(view().name("users/list"));

        verifyZeroInteractions(userService);
    }

    @Test
    public void supprimer_nominal() throws Exception {
        mockMvc.perform(delete("/users/supprimer/{id}", ID_USER)
        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/users/list"));

        then(userService).should().delete(ID_USER);
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