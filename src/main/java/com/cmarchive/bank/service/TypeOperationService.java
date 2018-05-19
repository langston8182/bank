package com.cmarchive.bank.service;

import java.util.List;

import com.cmarchive.bank.domain.TypeOperation;

public interface TypeOperationService {

	public List<TypeOperation> list();
	
	public TypeOperation getDebit();
	
	public TypeOperation getCredit();
	
}
