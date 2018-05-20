package com.cmarchive.bank.exceptions;

public class PermanentOperationNotFoundException extends RuntimeException {

    public PermanentOperationNotFoundException() {
        super();
    }

    public PermanentOperationNotFoundException(final String message) {
        super(message);
    }

}
