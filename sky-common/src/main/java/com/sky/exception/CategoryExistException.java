package com.sky.exception;

/**
 * 分类已存在异常
 */
public class CategoryExistException extends BaseException {
    public CategoryExistException(String message) {
        super(message);
    }
}
