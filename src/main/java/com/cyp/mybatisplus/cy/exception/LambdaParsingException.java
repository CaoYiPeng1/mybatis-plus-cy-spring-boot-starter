package com.cyp.mybatisplus.cy.exception;

/**
 * LambdaParsingException
 *
 * @author cyp
 * @since 2025/5/20
 */
@SuppressWarnings("unused")
public class LambdaParsingException extends RuntimeException {
    public LambdaParsingException(String message) {
        super(message);
    }

    public LambdaParsingException(String message, Throwable cause) {
        super(message, cause);
    }

    public LambdaParsingException(Throwable cause) {
        super("无法解析Lambda 或 方法引用", cause);
    }
}
