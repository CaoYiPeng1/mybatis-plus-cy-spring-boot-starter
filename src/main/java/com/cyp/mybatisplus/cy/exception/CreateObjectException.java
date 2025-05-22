package com.cyp.mybatisplus.cy.exception;

/**
 * CreateObjectException
 *
 * @author cyp
 * @since 2025/5/21
 */
@SuppressWarnings("unused")
public class CreateObjectException extends RuntimeException {
    public CreateObjectException(String message) {
        super(message);
    }

    public CreateObjectException(String message, Throwable cause) {
        super(message, cause);
    }

    public CreateObjectException(Throwable cause) {
        super("创建对象失败", cause);
    }
}
