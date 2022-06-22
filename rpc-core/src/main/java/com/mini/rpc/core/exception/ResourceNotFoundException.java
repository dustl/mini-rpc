package com.mini.rpc.core.exception;

public class ResourceNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 2L;

    public ResourceNotFoundException() {
        super();
    }

    public ResourceNotFoundException(String msg) {
        super(msg);
    }

    public ResourceNotFoundException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public ResourceNotFoundException(Throwable cause) {
        super(cause);
    }
}
