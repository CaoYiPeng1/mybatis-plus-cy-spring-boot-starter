package com.cyp.mybatisplus.cy.exception;

/**
 * JdbcTemplateSingletonException
 *
 * @author cyp
 * @since 2025/5/20
 */
@SuppressWarnings("unused")
public class JdbcTemplateSingletonException extends RuntimeException {
    public JdbcTemplateSingletonException() {
        super("JdbcTemplate不能被更改");
    }

    public JdbcTemplateSingletonException(String message) {
        super(message);
    }

    public JdbcTemplateSingletonException(String message, Throwable cause) {
        super(message, cause);
    }

    public JdbcTemplateSingletonException(Throwable cause) {
        super("JdbcTemplate不能被更改", cause);
    }
}
