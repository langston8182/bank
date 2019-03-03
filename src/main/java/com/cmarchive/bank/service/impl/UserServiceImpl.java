package com.cmarchive.bank.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.cmarchive.bank.domain.User;
import com.cmarchive.bank.exceptions.UserNotFoundException;
import com.cmarchive.bank.repository.UserRepository;
import com.cmarchive.bank.service.RoleService;
import com.cmarchive.bank.service.UserService;

@Service
public class UserServiceImpl implements UserDetailsService, UserService {

    private UserRepository userRepository;
    private RoleService roleService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleService roleService) {
        super();
        this.userRepository = userRepository;
        this.roleService = roleService;
    }
    
    @Override
    public List<User> list() {
        return userRepository.findAllByOrderByNomAsc();
    }
    
    @Override
    public User save(User user) {
        return userRepository.save(user);
    }
    
    @Override
    public void delete(Long id) {
        userRepository.delete(id);
    }
    
    @Override
    public User get(Long id) {
    	Assert.notNull(id, "Id cannot be null");
    	
    	User user = userRepository.findOne(id);
    	
    	if (user == null) {
            throw new UserNotFoundException("User with id " + id + " not found.");
        }
        
        return user;
    }
    
    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        
        return user;
    }

    @Override
    public void encodePassword(User user) {
        String password = new BCryptPasswordEncoder().encode(user.getPassword());
        user.setPassword(password);
    }

    @Override
    public void setUserRole(User user) {
        user.getRoles().add(roleService.findByRole("ROLE_USER"));
    }
    
}
