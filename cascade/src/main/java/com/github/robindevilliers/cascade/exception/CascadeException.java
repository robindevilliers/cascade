package com.github.robindevilliers.cascade.exception;

public class CascadeException extends RuntimeException {

    public CascadeException(String message){
        super(message);
    }
    
    public CascadeException(String message, Throwable f){
        super(message, f);
    }
}
