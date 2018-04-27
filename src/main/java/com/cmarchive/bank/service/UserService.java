package com.cmarchive.bank.service;

import java.util.List;

import com.cmarchive.bank.domain.User;

public interface UserService {

	public List<User> list();
	
	public User save(User user);
	
	public void delete(Long id);
	
	public User get(Long id);
	
	public User findByEmail(String email);

	public void encodePassword(User user);

	public void setUserRole(User user);
	
}
