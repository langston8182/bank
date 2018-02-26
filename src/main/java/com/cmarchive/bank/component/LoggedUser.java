package com.cmarchive.bank.component;

import org.springframework.stereotype.Component;

import com.cmarchive.bank.domain.User;

@Component
public class LoggedUser {

	
	public LoggedUser() {
		super();
	}

	private User user;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
}
