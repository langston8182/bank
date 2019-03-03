package com.cmarchive.bank.service.impl;

import com.cmarchive.bank.domain.PermanentOperation;
import com.cmarchive.bank.exceptions.PermanentOperationNotFoundException;
import com.cmarchive.bank.repository.PermanentOperationRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@RunWith(MockitoJUnitRunner.class)
public class PermanentOperationServiceImplTest {
    private static final Long ID = 1l;

    @InjectMocks
    private PermanentOperationServiceImpl permanentOperationService;

    @Mock
    private PermanentOperationRepository permanentOperationRepository;

    @Test
    public void get_nominal() {
        PermanentOperation permanentOperation = mock(PermanentOperation.class);
        given(permanentOperationRepository.findOne(ID)).willReturn(permanentOperation);

        PermanentOperation resultat = permanentOperationService.get(ID);

        then(permanentOperationRepository).should().findOne(ID);
        assertThat(resultat).isNotNull();
        assertThat(resultat).isEqualToComparingFieldByField(permanentOperation);
    }

    @Test
    public void get_AucunResultat() {
        given(permanentOperationRepository.findOne(ID)).willReturn(null);

        Throwable thrown = catchThrowable(() -> permanentOperationService.get(ID));

        then(permanentOperationRepository).should().findOne(ID);
        assertThat(thrown).isNotNull();
        assertThat(thrown).isExactlyInstanceOf(PermanentOperationNotFoundException.class);
    }

    @Test
    public void get_IdNull() {
        Throwable thrown = catchThrowable(() -> permanentOperationService.get(null));

        verifyNoMoreInteractions(permanentOperationRepository);
        assertThat(thrown).isNotNull();
        assertThat(thrown).isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void list_nominal() {
        List<PermanentOperation> permanentOperations = mock(List.class);
        given(permanentOperationRepository.findAllByOrderByJourAsc()).willReturn(permanentOperations);

        List<PermanentOperation> resultat = permanentOperationService.list();

        then(permanentOperationRepository).should().findAllByOrderByJourAsc();
        assertThat(resultat).isNotNull();
        assertThat(resultat).isEqualTo(permanentOperations);
    }

    @Test
    public void save_nominal() {
        PermanentOperation permanentOperation = mock(PermanentOperation.class);
        given(permanentOperationRepository.save(permanentOperation)).willReturn(permanentOperation);

        PermanentOperation resultat = permanentOperationService.save(permanentOperation);

        then(permanentOperationRepository).should().save(permanentOperation);
        assertThat(resultat).isNotNull();
        assertThat(resultat).isEqualToComparingFieldByField(permanentOperation);
    }

    @Test
    public void delete_nominal() {
        given(permanentOperationRepository.findOne(ID)).willReturn(mock(PermanentOperation.class));

        permanentOperationService.delete(ID);

        then(permanentOperationRepository).should().findOne(ID);
        then(permanentOperationRepository).should().delete(ID);
    }

    @Test
    public void delete_PermanentOperationNonTrouve() {
        given(permanentOperationRepository.findOne(ID)).willReturn(null);

        Throwable thrown = catchThrowable(() -> permanentOperationService.delete(ID));

        then(permanentOperationRepository).should().findOne(ID);
        verifyNoMoreInteractions(permanentOperationRepository);
        assertThat(thrown).isNotNull();
        assertThat(thrown).isExactlyInstanceOf(PermanentOperationNotFoundException.class);
    }

    @Test
    public void findByUser_nominal() {
        List<PermanentOperation> permanentOperations = mock(List.class);
        given(permanentOperationRepository.findByUserId(ID)).willReturn(permanentOperations);

        List<PermanentOperation> resultat = permanentOperationService.findByUser(ID);

        then(permanentOperationRepository).should().findByUserId(ID);
        assertThat(resultat).isNotNull();
        assertThat(resultat).isEqualTo(permanentOperations);
    }
}