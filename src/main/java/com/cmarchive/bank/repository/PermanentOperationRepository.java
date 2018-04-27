package com.cmarchive.bank.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cmarchive.bank.domain.PermanentOperation;

@Repository
public interface PermanentOperationRepository extends JpaRepository<PermanentOperation, Long> {

	public List<PermanentOperation> findAllByOrderByJourAsc();
	
	public List<PermanentOperation> findByUserId(Long id);
	
}
