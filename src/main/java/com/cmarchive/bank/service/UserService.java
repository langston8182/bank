package com.cmarchive.bank.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.cmarchive.bank.component.MyUserDetails;
import com.cmarchive.bank.domain.Role;
import com.cmarchive.bank.domain.User;
import com.cmarchive.bank.repository.RoleRepository;
import com.cmarchive.bank.repository.UserRepository;

@Service
public class UserService implements UserDetailsService {

	private UserRepository userRepository;
	private RoleRepository roleRepository;

	@Autowired
	public UserService(UserRepository userRepository, RoleRepository roleRepository) {
		super();
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
	}
	
	public List<User> list() {
		return userRepository.findAllByOrderByNomAsc();
	}
	
	public User save(User user) {
		return userRepository.save(user);
	}
	
	public void delete(Long id) {
		userRepository.delete(id);
	}
	
	public User get(Long id) {
		return userRepository.findOne(id);
	}
	
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
	
	public void encodePassword(User user) {
		String password = new BCryptPasswordEncoder().encode(user.getPassword());
		user.setPassword(password);
	}

	public void setUserRole(User user) {
		user.getRoles().add(roleRepository.findByRole("ROLE_USER"));
	}
	
}
