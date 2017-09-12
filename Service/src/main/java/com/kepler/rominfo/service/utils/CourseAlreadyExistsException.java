package com.kepler.rominfo.service.utils;

public class CourseAlreadyExistsException extends Exception {
    public CourseAlreadyExistsException() {}

    public CourseAlreadyExistsException(String message) {
        super(message);
    }
}
