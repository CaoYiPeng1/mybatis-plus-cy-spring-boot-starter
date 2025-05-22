package com.cyp.mybatisplus.cy.exception;

/**
 * CreateMapException
 *
 * @author cyp
 * @since 2025/5/21
 */
@SuppressWarnings("unused")
public class CreateMapException extends RuntimeException {
    public CreateMapException(String message) {
        super(message);
    }

    public CreateMapException(String message, Throwable cause) {
        super(message, cause);
    }

    public CreateMapException(Throwable cause) {
        super("创建映射失败", cause);
    }
}
