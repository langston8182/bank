package com.cmarchive.bank.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmarchive.bank.domain.Role;
import com.cmarchive.bank.repository.RoleRepository;

@Service
public class RoleService {

	public RoleRepository roleRepository;
	
	@Autowired
	public RoleService(RoleRepository roleRepository) {
		super();
		this.roleRepository = roleRepository;
	}



	public Role get(String role) {
		return roleRepository.findByRole(role);
	}
	
}
