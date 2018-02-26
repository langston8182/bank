package com.cmarchive.bank.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.cmarchive.bank.domain.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long>{

	public List<User> findAllByOrderByNomAsc();
	
	public User findByEmail(String email);
	
}
