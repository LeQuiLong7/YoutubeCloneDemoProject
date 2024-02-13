package com.lql.userservice.exception.model;

public class UserNotFoundException extends RuntimeException{

    public UserNotFoundException(String id) {
        super(String.format("User with %s is not found!", id));
    }
}
