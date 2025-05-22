package com.cyp.mybatisplus.cy.postprocessor;


import com.cyp.mybatisplus.cy.util.CyJdbcTemplateUtil;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

/**
 * CyJdbcTemplateBeanPostProcessor
 *
 * @author cyp
 * @since 2025/5/20
 */
@SuppressWarnings("all")
@Component
public class CyJdbcTemplateBeanPostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessAfterInitialization(@NonNull Object bean, @NonNull String beanName) throws BeansException {
        if (bean instanceof JdbcTemplate) {
            CyJdbcTemplateUtil.setJdbcTemplate((JdbcTemplate) bean);
        }
        return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
    }
}