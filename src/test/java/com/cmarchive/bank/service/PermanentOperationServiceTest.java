package com.cmarchive.bank.service;

import static org.mockito.BDDMockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.cmarchive.bank.domain.PermanentOperation;
import com.cmarchive.bank.repository.PermanentOperationRepository;
import com.cmarchive.bank.service.impl.PermanentOperationServiceImpl;

@RunWith(MockitoJUnitRunner.class)
public class PermanentOperationServiceTest {

	@InjectMocks
	private PermanentOperationServiceImpl permanentOperationService;
	
	@Mock
	private PermanentOperationRepository permanentOperationRepository;
	
	@Test
	public void list_nominal() {
		List<PermanentOperation> permanentOperationList = mock(ArrayList.class);
		given(permanentOperationRepository.findAllByOrderByJourAsc()).willReturn(permanentOperationList);
		
		permanentOperationService.list();
		
		then(permanentOperationRepository).should().findAllByOrderByJourAsc();
	}
}
