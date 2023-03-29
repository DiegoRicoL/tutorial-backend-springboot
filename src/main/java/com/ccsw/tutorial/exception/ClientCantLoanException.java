package com.ccsw.tutorial.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "This client already has 2 active loans.")
public class ClientCantLoanException extends RuntimeException {

    private static final long serialVersionUID = 6070542307155549476L;
    private static final String msg = "This client already has 2 active loans.";

    public ClientCantLoanException() {
        super(msg);
    }
}
