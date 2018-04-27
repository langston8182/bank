package com.cmarchive.bank.exceptions;

public class OperationNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 6864377248289915186L;

    public OperationNotFoundException() {
        super();
    }

    public OperationNotFoundException(final String message) {
        super(message);
    }
}
