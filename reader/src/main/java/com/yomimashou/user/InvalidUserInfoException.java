package com.yomimashou.user;

public class InvalidUserInfoException extends RuntimeException {

    public InvalidUserInfoException(String message) {
        super(message);
    }
}
