package com.cyp.mybatisplus.cy.functionalinterface;

import java.io.Serializable;
import java.util.function.Function;

/**
 * SFunction
 *
 * @author cyp
 * @since 2025/5/20
 */
@SuppressWarnings("all")
@FunctionalInterface
public interface SFunction<T, R> extends Function<T, R>, Serializable {
}
