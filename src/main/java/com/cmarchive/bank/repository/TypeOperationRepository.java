package com.cmarchive.bank.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cmarchive.bank.domain.TypeOperation;

@Repository
public interface TypeOperationRepository extends JpaRepository<TypeOperation, Long>{

	public TypeOperation findByValueType(String value);
	
}
