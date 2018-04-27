package com.cmarchive.bank.service;

import com.cmarchive.bank.domain.TypeOperation;

public interface TypeOperationService {

	public Iterable<TypeOperation> list();
	
	public TypeOperation getDebit();
	
	public TypeOperation getCredit();
	
}
