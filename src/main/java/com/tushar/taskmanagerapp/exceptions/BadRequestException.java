package com.tushar.taskmanagerapp.exceptions;

public class BadRequestException extends RuntimeException{
    public BadRequestException (String ex){
        super(ex);
    }
}
