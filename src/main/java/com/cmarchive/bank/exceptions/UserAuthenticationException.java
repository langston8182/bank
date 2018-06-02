package com.cmarchive.bank.exceptions;

public class UserAuthenticationException extends RuntimeException {

    public UserAuthenticationException() {
        super();
    }

    public UserAuthenticationException(final String message) {
        super(message);
    }

}
