package com.cmarchive.bank.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.cmarchive.bank.component.MyUserDetails;
import com.cmarchive.bank.domain.User;
import com.cmarchive.bank.repository.UserRepository;

@Service
public class UserService implements UserDetailsService {

	private UserRepository userRepository;

	@Autowired
	public UserService(UserRepository userRepository) {
		super();
		this.userRepository = userRepository;
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
}
