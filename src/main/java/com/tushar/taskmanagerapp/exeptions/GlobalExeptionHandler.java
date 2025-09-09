package com.tushar.taskmanagerapp.exeptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.tushar.taskmanagerapp.dto.Response;

@ControllerAdvice
public class GlobalExeptionHandler {

    @ExceptionHandler(NotFoundExeption.class)
    public ResponseEntity<Response<?>> handleNotFoundExeption(Exception e){
        Response<?> response = Response.builder()
                                    .statusCode(HttpStatus.NOT_FOUND.value())
                                    .message(e.getMessage())
                                    .build();
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
    
    @ExceptionHandler(BadRequestExeption.class)
    public ResponseEntity<Response<?>> handleBadRequestExeption(Exception e){
        Response<?> response = Response.builder()
                                    .statusCode(HttpStatus.BAD_REQUEST.value())
                                    .message(e.getMessage())
                                    .build();
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Response<?>> handleAllUnknownExeption(Exception e){
        Response<?> response = Response.builder()
                                    .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                                    .message(e.getMessage())
                                    .build();
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
