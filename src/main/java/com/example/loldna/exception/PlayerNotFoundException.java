package com.example.loldna.exception;

public class PlayerNotFoundException extends RuntimeException {

    public PlayerNotFoundException(String msg) {
        super(msg);
    }
}
