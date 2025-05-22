package com.cyp.mybatisplus.cy.exception;

/**
 * UnauthorizedSortingFieldException
 *
 * @author cyp
 * @since 2025/5/20
 */
@SuppressWarnings("unused")
public class UnauthorizedSortingFieldException extends RuntimeException {
    public UnauthorizedSortingFieldException(String message) {
        super("此排序字段未授权: [ " + message + " ]");
    }
}
