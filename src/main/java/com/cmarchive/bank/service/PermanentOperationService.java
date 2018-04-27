package com.cmarchive.bank.service;

import java.util.List;

import com.cmarchive.bank.domain.PermanentOperation;

public interface PermanentOperationService {

	public PermanentOperation get(Long id);
	
	public List<PermanentOperation> list();
	
	public PermanentOperation save(PermanentOperation permanentOperation);
	
	public void delete(Long id);
	
	public List<PermanentOperation> findByUser(Long id);
}
