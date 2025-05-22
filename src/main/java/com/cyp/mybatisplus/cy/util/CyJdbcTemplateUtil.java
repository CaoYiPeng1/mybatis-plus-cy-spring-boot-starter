package com.cyp.mybatisplus.cy.util;

import com.cyp.mybatisplus.cy.exception.JdbcTemplateSingletonException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * CyJdbcTemplateUtil
 *
 * @author cyp
 * @since 2025/5/20
 */
@Slf4j
public class CyJdbcTemplateUtil {
    @Getter
    private static JdbcTemplate jdbcTemplate;

    public static void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        if (CyJdbcTemplateUtil.jdbcTemplate != null) {
            throw new JdbcTemplateSingletonException();
        }
        synchronized (CyJdbcTemplateUtil.class) {
            if (CyJdbcTemplateUtil.jdbcTemplate != null) {
                throw new JdbcTemplateSingletonException();
            }
            CyJdbcTemplateUtil.jdbcTemplate = jdbcTemplate;
            log.info("CyJdbcTemplate全局初始化成功");
        }
    }
}
