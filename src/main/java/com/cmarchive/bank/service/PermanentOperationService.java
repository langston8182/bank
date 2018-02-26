package com.cmarchive.bank.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cmarchive.bank.domain.Operation;
import com.cmarchive.bank.domain.PermanentOperation;
import com.cmarchive.bank.repository.PermanentOperationRepository;

@Service
public class PermanentOperationService {

	private PermanentOperationRepository permanentOperationRepository;

	public PermanentOperationService(PermanentOperationRepository permanentOperationRepository) {
		super();
		this.permanentOperationRepository = permanentOperationRepository;
	}
	
	public PermanentOperation get(Long id) {
		return permanentOperationRepository.findOne(id);
	}
	
	public List<PermanentOperation> list() {
		return permanentOperationRepository.findAllByOrderByJourAsc();
	}
	
	public PermanentOperation save(PermanentOperation permanentOperation) {
		return permanentOperationRepository.save(permanentOperation);
	}
	
	public void delete(Long id) {
		PermanentOperation permanentOperation = get(id);
		for (Operation operation : permanentOperation.getOperations()) {
			operation.setPermanentOperation(null);
		}
		
		permanentOperationRepository.delete(id);
	}
	
	public List<PermanentOperation> findByUser(Long id) {
		return permanentOperationRepository.findByUserId(id);
	}
}
