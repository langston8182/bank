package com.cmarchive.bank.exceptions;

public class UserNotFoundException extends RuntimeException {

	private String msg;

	public UserNotFoundException(String msg) {
		super();
		this.msg = msg;
	}
	
	
	
}
