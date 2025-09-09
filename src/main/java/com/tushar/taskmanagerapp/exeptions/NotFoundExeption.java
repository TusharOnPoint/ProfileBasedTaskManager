package com.tushar.taskmanagerapp.exeptions;

public class NotFoundExeption extends RuntimeException{
    public NotFoundExeption (String ex){
        super(ex);
    }
}
