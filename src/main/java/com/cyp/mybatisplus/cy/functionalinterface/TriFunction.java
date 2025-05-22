package com.cyp.mybatisplus.cy.functionalinterface;

/**
 * TriFunction
 *
 * @author cyp
 * @since 2025/5/20
 */
@FunctionalInterface
public interface TriFunction<T, U, V, R> {
    R apply(T t, U u, V v);
}   