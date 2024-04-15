package com.wego.interview.carpark.inbound.web;

public class InvalidInputException extends RuntimeException {

    public InvalidInputException(String message) {
        super(message);
    }
}
