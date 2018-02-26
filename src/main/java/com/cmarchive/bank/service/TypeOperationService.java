package com.cmarchive.bank.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmarchive.bank.domain.TypeOperation;
import com.cmarchive.bank.repository.TypeOperationRepository;

@Service
public class TypeOperationService {

	private TypeOperationRepository typeOperationRepository;

	@Autowired
	public TypeOperationService(TypeOperationRepository typeOperationRepository) {
		super();
		this.typeOperationRepository = typeOperationRepository;
	}
	
	public Iterable<TypeOperation> list() {
		return typeOperationRepository.findAll();
	}
	
	public TypeOperation getDebit() {
		return typeOperationRepository.findByValueType("DEBIT");
	}
	
	public TypeOperation getCredit() {
		return typeOperationRepository.findByValueType("CREDIT");
	}
	
}
