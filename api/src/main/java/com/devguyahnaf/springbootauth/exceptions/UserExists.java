package com.devguyahnaf.springbootauth.exceptions;

public class UserExists extends Exception {
    public UserExists(String email) {
        super("User already exists for the email '" + email + "'.");
    }
}
