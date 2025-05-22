package com.cyp.mybatisplus.cy.util;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * PageUtil
 *
 * @author cyp
 * @since 2025/5/19
 */
@SuppressWarnings("all")
public class PageUtil {
    @SuppressWarnings("unchecked")
    public static <R> Page<R> transType(Page<?> page, Class<R> clazz) {
        return (Page<R>) page;
    }
}
