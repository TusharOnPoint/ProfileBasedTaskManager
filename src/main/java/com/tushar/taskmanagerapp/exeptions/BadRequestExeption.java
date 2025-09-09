package com.tushar.taskmanagerapp.exeptions;

public class BadRequestExeption extends RuntimeException{
    public BadRequestExeption (String ex){
        super(ex);
    }
}
