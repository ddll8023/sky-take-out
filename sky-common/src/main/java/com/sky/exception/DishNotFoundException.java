package com.sky.exception;

/**
 * 菜品未找到异常
 */
public class DishNotFoundException extends BaseException {
    public DishNotFoundException(String message) {
        super(message);
    }
}
