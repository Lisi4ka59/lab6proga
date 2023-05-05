package com.lisi4ka.utils;

public class PackagedResponse {
    private String message;
    public PackagedResponse(String message, ResponseStatus status){
        this.message = message;
        this.status = status;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    ResponseStatus status;
}

