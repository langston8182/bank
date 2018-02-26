package com.cmarchive.bank.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.cmarchive.bank.domain.TypeOperation;

@Repository
public interface TypeOperationRepository extends CrudRepository<TypeOperation, Long>{

	public TypeOperation findByValueType(String value);
	
}
