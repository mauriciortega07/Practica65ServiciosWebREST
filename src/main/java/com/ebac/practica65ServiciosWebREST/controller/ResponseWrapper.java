package com.ebac.practica65ServiciosWebREST.controller;

import lombok.Getter;
import org.springframework.http.ResponseEntity;

public class ResponseWrapper<T> {
    @Getter
    private final boolean isSucces;
    @Getter
    private final String message;
    @Getter
    private ResponseEntity<T> responseEntity;

    public ResponseWrapper(boolean isSucces, String message, ResponseEntity<T> responseEntity){
        this.isSucces = isSucces;
        this.message = message;
        this.responseEntity = responseEntity;
    }

    public ResponseWrapper(boolean isSucces, String message){
        this.isSucces = isSucces;
        this.message = message;
    }

}

