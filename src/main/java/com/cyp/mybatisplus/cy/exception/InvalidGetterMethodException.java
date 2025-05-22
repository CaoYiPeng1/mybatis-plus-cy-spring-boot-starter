package com.cyp.mybatisplus.cy.exception;

/**
 * InvalidGetterMethodException
 *
 * @author cyp
 * @since 2025/5/20
 */
@SuppressWarnings("unused")
public class InvalidGetterMethodException extends RuntimeException {
    public InvalidGetterMethodException() {
        super("不是合法的 getter 方法");
    }

    public InvalidGetterMethodException(String message) {
        super(message);
    }
}
