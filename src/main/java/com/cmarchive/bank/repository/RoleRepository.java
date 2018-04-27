package com.cmarchive.bank.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cmarchive.bank.domain.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

	public Role findByRole(String role);
	
}
