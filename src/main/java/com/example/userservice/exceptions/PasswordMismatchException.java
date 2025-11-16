package com.example.userservice.exceptions;

public class PasswordMismatchException extends Exception{
    public PasswordMismatchException(String message){
        super("Password Mismatch. Unable to Login"+ message);
    }
}
