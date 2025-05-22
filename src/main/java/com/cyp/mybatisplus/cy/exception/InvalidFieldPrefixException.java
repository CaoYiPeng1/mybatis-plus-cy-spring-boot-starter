package com.cyp.mybatisplus.cy.exception;

/**
 * InvalidFieldPrefixException
 *
 * @author cyp
 * @since 2025/5/20
 */
@SuppressWarnings("unused")
public class InvalidFieldPrefixException extends RuntimeException {
    public InvalidFieldPrefixException() {
        super("解析字段前缀标识符不合规，字段前缀请用+或-");
    }

    public InvalidFieldPrefixException(String message) {
        super(message);
    }
}
