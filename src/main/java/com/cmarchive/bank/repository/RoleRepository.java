package com.cmarchive.bank.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.cmarchive.bank.domain.Role;

@Repository
public interface RoleRepository extends CrudRepository<Role, Long> {

	public Role findByRole(String role);
	
}
