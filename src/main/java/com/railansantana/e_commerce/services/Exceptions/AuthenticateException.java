package com.railansantana.e_commerce.services.Exceptions;

public class AuthenticateException extends RuntimeException{
    public AuthenticateException(String msg){
        super(msg);
    }
}
