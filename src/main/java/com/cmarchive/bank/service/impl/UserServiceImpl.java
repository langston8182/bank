package com.cmarchive.bank.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.cmarchive.bank.component.MyUserDetails;
import com.cmarchive.bank.domain.User;
import com.cmarchive.bank.repository.RoleRepository;
import com.cmarchive.bank.repository.UserRepository;
import com.cmarchive.bank.service.UserService;

@Service
public class UserServiceImpl implements UserDetailsService, UserService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository) {
        super();
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
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
        return userRepository.findOne(id);
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
        
        return new MyUserDetails(user);
    }
    
    @Override
    public void encodePassword(User user) {
        String password = new BCryptPasswordEncoder().encode(user.getPassword());
        user.setPassword(password);
    }

    @Override
    public void setUserRole(User user) {
        user.getRoles().add(roleRepository.findByRole("ROLE_USER"));
    }
    
}