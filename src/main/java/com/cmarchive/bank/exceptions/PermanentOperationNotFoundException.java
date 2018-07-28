package com.cmarchive.bank.exceptions;

public class PermanentOperationNotFoundException extends RuntimeException {

	private String msg;

	public PermanentOperationNotFoundException(String msg) {
		super();
		this.msg = msg;
	}
	
	
}
