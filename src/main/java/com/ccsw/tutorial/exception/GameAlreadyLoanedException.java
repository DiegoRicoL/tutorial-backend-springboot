package com.ccsw.tutorial.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "This game is already being loaned.")
public class GameAlreadyLoanedException extends RuntimeException {

    private static final long serialVersionUID = 2692376284547155693L;
    private static final String msg = "This game is already being loaned.";

    public GameAlreadyLoanedException() {
        super(msg);
    }

}
