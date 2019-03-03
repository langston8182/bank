package com.cmarchive.bank.service.impl;

import com.cmarchive.bank.domain.Operation;
import com.cmarchive.bank.domain.PermanentOperation;
import com.cmarchive.bank.domain.TypeOperation;
import com.cmarchive.bank.domain.User;
import com.cmarchive.bank.exceptions.OperationNotFoundException;
import com.cmarchive.bank.repository.OperationRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.LocalDate;
import java.time.Year;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class OperationServiceImplTest {

    @InjectMocks
    private OperationServiceImpl operationService;

    @Mock
    private OperationRepository operationRepository;

    @Test
    public void list_nominal() {
        List operations = mock(List.class);
        given(operationRepository.findAllByOrderByDateOperationDescIntituleAsc()).willReturn(operations);

        List<Operation> resultat = operationService.list();

        then(operationRepository).should().findAllByOrderByDateOperationDescIntituleAsc();
        assertThat(resultat).isEqualTo(operations);
    }

    @Test
    public void get_nominal() {
        Operation operation = mock(Operation.class);
        Long id = 1L;
        given(operationRepository.findOne(anyLong())).willReturn(operation);

        Operation resultat = operationService.get(id);

        then(operationRepository).should().findOne(id);
        assertThat(resultat).isEqualTo(operation);
    }

    @Test
    public void get_OperationNonTrouvee() {
        Long id = 1L;
        given(operationRepository.findOne(anyLong())).willReturn(null);

        Throwable thrown = catchThrowable(() -> operationService.get(id));

        then(operationRepository).should().findOne(id);
        assertThat(thrown).isNotNull();
        assertThat(thrown).isInstanceOf(OperationNotFoundException.class);
    }

    @Test
    public void save_nominal() {
        Operation operation = mock(Operation.class);

        Operation resultat = operationService.save(operation);

        then(operationRepository).should().save(operation);
        assertThat(resultat).isEqualTo(resultat);
    }

    @Test
    public void delete_nominal() {
        Long id = 1L;

        operationService.delete(id);

        then(operationRepository).should().delete(id);
    }

    @Test
    public void findByUser_nominal() {
        Long id = 1L;
        List operations = mock(List.class);
        given(operationRepository.findByUserId(id)).willReturn(operations);

        List<Operation> resultat = operationService.findByUser(id);

        then(operationRepository).should().findByUserId(id);
        assertThat(resultat).isEqualTo(operations);
    }

    @Test
    public void addOpPermanenteToOperations() {
        int month = 1;
        ArgumentCaptor<Operation> operationCapture = ArgumentCaptor.forClass(Operation.class);
        PermanentOperation permanentOperation = creerPermanentOperation();
        LocalDate localDate = LocalDate.of(Year.now().getValue(), month, permanentOperation.getJour());
        given(operationRepository.save(any(Operation.class))).willReturn(mock(Operation.class));

        operationService.addOpPermanenteToOperations(month, permanentOperation);

        then(operationRepository).should().save(operationCapture.capture());
        Operation resultat = operationCapture.getValue();
        assertThat(resultat.getIntitule()).isEqualToIgnoringCase(permanentOperation.getIntitule());
        assertThat(resultat.getPrix()).isEqualTo(permanentOperation.getPrix());
        assertThat(resultat.getTypeOperation()).isEqualTo(permanentOperation.getTypeOperation());
        assertThat(resultat.getUser()).isEqualToComparingFieldByField(permanentOperation.getUser());
        assertThat(resultat.getPermanentOperation()).isEqualToComparingFieldByField(permanentOperation);
        assertThat(resultat.getDateOperation()).isEqualByComparingTo(localDate);
    }

    @Test
    public void findByMonth() {
        Long id = 1L;
        int month = 1;
        LocalDate start = LocalDate.now().withMonth(month).with(TemporalAdjusters.firstDayOfMonth());
        LocalDate end = LocalDate.now().withMonth(month).with(TemporalAdjusters.lastDayOfMonth());
        List<Operation> operations = mock(List.class);
        given(operationRepository.findByUserIdAndDateOperationBetweenOrderByDateOperationAscIntituleAsc(
                id, start, end)).willReturn(operations);

        List<Operation> resultat = operationService.findByMonth(id, month);

        then(operationRepository).should().findByUserIdAndDateOperationBetweenOrderByDateOperationAscIntituleAsc(
                id, start, end);
        assertThat(resultat).isEqualTo(operations);
    }

    private PermanentOperation creerPermanentOperation() {
        TypeOperation typeOperation = new TypeOperation();
        User user = new User()
                .setPassword("xxx")
                .setEmail("test@yahoo.fr")
                .setNom("nom")
                .setPrenom("prenom");

        return new PermanentOperation()
                .setUser(user)
                .setJour(1)
                .setIntitule("intitule")
                .setPrix(1)
                .setTypeOperation(typeOperation);
    }
}