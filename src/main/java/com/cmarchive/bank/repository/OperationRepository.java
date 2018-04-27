package com.cmarchive.bank.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cmarchive.bank.domain.Operation;

@Repository
public interface OperationRepository extends JpaRepository<Operation, Long>{

	public List<Operation> findAllByOrderByDateOperationDescIntituleAsc();
	
	public List<Operation> findByUserId(Long id);
	
	public List<Operation> findByUserIdAndDateOperationBetweenOrderByDateOperationAscIntituleAsc(Long id, LocalDate start, LocalDate end);
	
}
