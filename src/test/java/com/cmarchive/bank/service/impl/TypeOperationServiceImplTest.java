package com.cmarchive.bank.service.impl;

import com.cmarchive.bank.domain.TypeOperation;
import com.cmarchive.bank.repository.TypeOperationRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class TypeOperationServiceImplTest {
    private final static String CREDIT = "CREDIT";
    private final static String DEBIT = "DEBIT";

    @InjectMocks
    private TypeOperationServiceImpl typeOperationService;

    @Mock
    private TypeOperationRepository typeOperationRepository;

    @Test
    public void list_nominal() {
        List<TypeOperation> typeOperations = mock(List.class);
        given(typeOperationRepository.findAll()).willReturn(typeOperations);

        List<TypeOperation> resultat = typeOperationService.list();

        then(typeOperationRepository).should().findAll();
        assertThat(resultat).isNotNull();
        assertThat(resultat).isEqualTo(typeOperations);
    }

    @Test
    public void getDebit_nominal() {
        TypeOperation typeOperation = mock(TypeOperation.class);
        given(typeOperationRepository.findByValueType(DEBIT)).willReturn(typeOperation);

        TypeOperation resultat = typeOperationService.getDebit();

        then(typeOperationRepository).should().findByValueType(DEBIT);
        assertThat(resultat).isNotNull();
        assertThat(resultat).isEqualToComparingFieldByField(typeOperation);
    }

    @Test
    public void getCredit_nominal() {
        TypeOperation typeOperation = mock(TypeOperation.class);
        given(typeOperationRepository.findByValueType(CREDIT)).willReturn(typeOperation);

        TypeOperation resultat = typeOperationService.getCredit();

        then(typeOperationRepository).should().findByValueType(CREDIT);
        assertThat(resultat).isNotNull();
        assertThat(resultat).isEqualToComparingFieldByField(typeOperation);
    }
}