package com.cmarchive.bank.service;

import java.util.List;

import com.cmarchive.bank.domain.Operation;
import com.cmarchive.bank.domain.PermanentOperation;

public interface OperationService {

	public List<Operation> list();
	
	public Operation get(long id);
	
	public Operation save(Operation operation);
	
	public void delete(Long id);
	
	public List<Operation> findByUser(Long idUtilisateur);
	
	public Operation addOpPermanenteToOperations(int month, PermanentOperation permanentOperation);
	
	public List<Operation> findByMonth(Long id, int month);
}
