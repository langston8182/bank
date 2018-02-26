package com.cmarchive.bank.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.cmarchive.bank.domain.Operation;

@Repository
public interface OperationRepository extends CrudRepository<Operation, Long>{

	public List<Operation> findAllByOrderByDateOperationDescIntituleAsc();
	
	public List<Operation> findByUserId(Long id);
	
	public List<Operation> findByUserIdAndDateOperationBetweenOrderByDateOperationAscIntituleAsc(Long id, Date start, Date end);
	
}
