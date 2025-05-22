package com.cyp.mybatisplus.cy.exception;

/**
 * PropertyColumnNameNotFoundException
 *
 * @author cyp
 * @since 2025/5/20
 */
@SuppressWarnings("unused")
public class PropertyColumnNameNotFoundException extends RuntimeException {
    public PropertyColumnNameNotFoundException(String message) {
        super(message);
    }

    public PropertyColumnNameNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public PropertyColumnNameNotFoundException(Throwable cause) {
        super("属性对应的列名获取失败", cause);
    }
}
