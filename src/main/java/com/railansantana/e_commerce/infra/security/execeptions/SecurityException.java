package com.railansantana.e_commerce.infra.security.execeptions;

public class SecurityException extends RuntimeException {
    public SecurityException(String message) {
        super(message);
    }
}
