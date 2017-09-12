package com.kepler.rominfo.service.utils;

public class InvalidDateException extends Exception {
    public InvalidDateException() {}

    public InvalidDateException(String message) {
        super(message);
    }
}
