package com.nc.e_comm.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class TokenRevokedException extends RuntimeException{
    public TokenRevokedException(String message){
        super(message);
    }
}
