package com.railansantana.e_commerce.services.Exceptions;

public class DatabaseException extends RuntimeException{
    public DatabaseException(){
        super("Database exception");
    }
}
