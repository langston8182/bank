package com.cmarchive.bank.exceptions;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException() {
        super();
    }

    public UserNotFoundException(final String message) {
        super(message);
    }

}
