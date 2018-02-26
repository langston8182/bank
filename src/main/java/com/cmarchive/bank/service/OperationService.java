package com.cmarchive.bank.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmarchive.bank.domain.Operation;
import com.cmarchive.bank.domain.PermanentOperation;
import com.cmarchive.bank.domain.User;
import com.cmarchive.bank.repository.OperationRepository;

@Service
public class OperationService {

	private OperationRepository operationRepository;

	@Autowired
	public OperationService(OperationRepository operationRepository) {
		super();
		this.operationRepository = operationRepository;
	}
	
	public List<Operation> list() {
		return operationRepository.findAllByOrderByDateOperationDescIntituleAsc();
	}
	
	public Operation get(long id) {
		return operationRepository.findOne(id);
	}
	
	public Operation save(Operation operation) {
		return operationRepository.save(operation);
	}
	
	public void delete(Long id) {
		operationRepository.delete(id);
	}
	
	public List<Operation> findByUser(Long idUtilisateur) {
		return operationRepository.findByUserId(idUtilisateur);
	}
	
	public Operation addOpPermanenteToOperations(int month, PermanentOperation permanentOperation) {
		User user = permanentOperation.getUser();
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.set(Calendar.MONTH, month - 1);
		calendar.set(Calendar.DAY_OF_MONTH, permanentOperation.getJour());
		
		Operation operation = new Operation();
		operation.setDateOperation(calendar.getTime());
		operation.setIntitule(permanentOperation.getIntitule());
		operation.setPrix(permanentOperation.getPrix());
		operation.setTypeOperation(permanentOperation.getTypeOperation());
		operation.setUser(user);
		operation.setPermanentOperation(permanentOperation);
		
		return operationRepository.save(operation);
	}
	
	public List<Operation> findByMonth(Long id, int month) {
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR));
		calendar.set(Calendar.MONTH, month - 1);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		
		Date start = calendar.getTime();
		
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		
		Date end = calendar.getTime();
		
		return operationRepository.findByUserIdAndDateOperationBetweenOrderByDateOperationAscIntituleAsc(id, start, end);
	}
}
