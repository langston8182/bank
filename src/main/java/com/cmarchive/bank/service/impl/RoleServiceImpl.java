package com.cmarchive.bank.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmarchive.bank.domain.Role;
import com.cmarchive.bank.repository.RoleRepository;
import com.cmarchive.bank.service.RoleService;

@Service
public class RoleServiceImpl implements RoleService {

    public RoleRepository roleRepository;
    
    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        super();
        this.roleRepository = roleRepository;
    }

    @Override
    public Role get(String role) {
        return roleRepository.findByRole(role);
    }
    
}
