package com.cmarchive.bank.controller;

import com.cmarchive.bank.component.LoggedUser;
import com.cmarchive.bank.domain.PermanentOperation;
import com.cmarchive.bank.domain.TypeOperation;
import com.cmarchive.bank.domain.User;
import com.cmarchive.bank.service.impl.PermanentOperationServiceImpl;
import com.cmarchive.bank.service.impl.TypeOperationServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
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
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringRunner.class)
@WebMvcTest(PermanentOperationController.class)
public class PermanentOperationControllerTest {
    private final static Long ID_USER = 1L;
    private final static Long ID_PO = 1L;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PermanentOperationServiceImpl permanentOperationService;

    @MockBean
    private TypeOperationServiceImpl typeOperationService;

    @MockBean
    private LoggedUser loggedUser;

    @Test
    public void list_nominal() throws Exception {
        User user = mock(User.class);
        List<PermanentOperation> permanentOperations = Collections.singletonList(creerPermanentOperation());
        List<TypeOperation> typeOperations = Collections.singletonList(creerTypeOperation());
        given(user.getId()).willReturn(ID_USER);
        given(loggedUser.getUser()).willReturn(user);
        given(permanentOperationService.findByUser(ID_USER)).willReturn(permanentOperations);
        given(typeOperationService.list()).willReturn(typeOperations);

        mockMvc.perform(get("/permanentsOperation/list")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(model().attribute("permanentsOperation", permanentOperations))
                .andExpect(model().attribute("typesOperation", typeOperations))
                .andExpect(view().name("permanentsOperation/list"));

        then(permanentOperationService).should().findByUser(ID_USER);
    }

    @Test
    public void ajouter_nominal() throws Exception {
        User user = mock(User.class);
        LoggedUser loggedUser = mock(LoggedUser.class);
        PermanentOperation permanentOperation = creerPermanentOperation();
        given(loggedUser.getUser()).willReturn(user);

        mockMvc.perform(post("/permanentsOperation/ajouter")
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsBytes(permanentOperation)))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/permanentsOperation/list"));

        then(permanentOperationService).should().save(any(PermanentOperation.class));
    }

    @Test
    public void modifier_nominal() throws Exception {
        List<TypeOperation> typeOperations = Collections.singletonList(creerTypeOperation());
        PermanentOperation permanentOperation = creerPermanentOperation();
        List<PermanentOperation> permanentOperations = Collections.singletonList(permanentOperation);
        given(permanentOperationService.get(ID_PO)).willReturn(permanentOperation);
        given(permanentOperationService.list()).willReturn(permanentOperations);
        given(typeOperationService.list()).willReturn(typeOperations);

        mockMvc.perform(put("/permanentsOperation/modifier/{id}", ID_PO)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(model().attribute("permanentsOperation", permanentOperations))
                .andExpect(model().attribute("typesOperation", typeOperations))
                .andExpect(model().attribute("permanentOperation", permanentOperation))
                .andExpect(view().name("permanentsOperation/list"));

        then(permanentOperationService).should().get(ID_PO);
        then(permanentOperationService).should().list();
        then(typeOperationService).should().list();
    }

    @Test
    public void supprimer_nominal() throws Exception {
        mockMvc.perform(delete("/permanentsOperation/supprimer/{id}", ID_PO)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/permanentsOperation/list"));

        then(permanentOperationService).should().delete(ID_PO);
    }

    private PermanentOperation creerPermanentOperation() {
        PermanentOperation permanentOperation = new PermanentOperation()
                .setId(1L)
                .setJour(1)
                .setIntitule("Intitule")
                .setPrix(1)
                .setTypeOperation(creerTypeOperation());

        return permanentOperation;
    }

    private TypeOperation creerTypeOperation() {
        TypeOperation typeOperation = new TypeOperation()
                .setValue("typeOperation");

        return typeOperation;
    }
}