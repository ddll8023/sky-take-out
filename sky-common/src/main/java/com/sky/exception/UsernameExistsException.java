package com.sky.exception;

/**
* 用户名已存在异常
* */

public class UsernameExistsException extends BaseException {
    public UsernameExistsException(String message) {
        super(message);
    }
}
