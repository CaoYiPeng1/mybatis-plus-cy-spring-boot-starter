package com.cyp.mybatisplus.cy.util;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cyp.mybatisplus.cy.exception.InvalidGetterMethodException;
import com.cyp.mybatisplus.cy.exception.MapEntityConversionException;
import com.cyp.mybatisplus.cy.exception.PropertyColumnNameNotFoundException;
import com.cyp.mybatisplus.cy.functionalinterface.FunctionalInterfaceUtil;
import com.cyp.mybatisplus.cy.functionalinterface.SFunction;
import com.google.common.base.CaseFormat;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * MybatisPlusEntityParseUtil
 *
 * @author cyp
 * @since 2025/5/19
 */
@SuppressWarnings("all")
@Slf4j
public class MybatisPlusEntityParseUtil {
    private final static String GET = "get";
    private final static String IS = "is";

    public static String getSqlSelectIn(String tableName, String columnName, List<?> keys) {
        StringBuilder format = new StringBuilder(String.format("select * from %s where %s in (", tableName, columnName));
        AtomicReference<Boolean> isFirst = new AtomicReference<>(true);
        StringBuilder format1 = new StringBuilder();
        keys.forEach((str) -> {
            if (isFirst.get()) {
                isFirst.set(false);
                format1.append(str);
            } else {
                format1.append(",").append(str);
            }
        });
        format.append(format1);
        format.append(")");
        format.append(" ORDER BY FIELD (");
        format.append(columnName);
        format.append(",");
        format.append(format1);
        format.append(")");
        return format.toString();
    }

    public static <T, N> String getSqlSelectIn(SFunction<T, N> function, List<N> keys) {
        String columnName = getColumnName(function);
        String tableName = getTableName(function);
        List<String> keyStrings = keys.stream().map((str) -> " \"" + str + "\" ").toList();
        return getSqlSelectIn(tableName, columnName, keyStrings);
    }

    public static <T, N> String getSqlSelectInSecurity(SFunction<T, N> function, List<N> keys) {
        String columnName = getColumnName(function);
        String tableName = getTableName(function);
        List<String> keyParams = keys.stream().map((str) -> "?").toList();
        return getSqlSelectIn(tableName, columnName, keyParams);
    }

    public static <T, N> List<Map<String, Object>> getSqlSelectInSecurityAndExecute(SFunction<T, N> function, List<N> keys) {
        String sqlSelectIn = getSqlSelectInSecurity(function, keys);
        log.info("sqlSelectIn: ==> {}", sqlSelectIn);
        keys = Stream.concat(keys.stream(), keys.stream())
                .collect(Collectors.toList());
        Object[] keysArray = keys.toArray();
        log.info("sqlParam: ==> {}", Arrays.toString(keysArray));
        return CyJdbcTemplateUtil.getJdbcTemplate().queryForList(sqlSelectIn, keysArray);
    }

    public static <T> List<T> mapToEntityList(Class<T> entityClass, List<Map<String, Object>> mapList) {
        try {
            Map<String, Field> columnToFieldMap = getColumnToFieldMap(entityClass);
            List<T> result = new ArrayList<>();
            for (Map<String, Object> row : mapList) {
                T instance = entityClass.getDeclaredConstructor().newInstance();
                for (Map.Entry<String, Object> entry : row.entrySet()) {
                    String columnName = entry.getKey();
                    Object value = entry.getValue();
                    Field field = columnToFieldMap.get(columnName.toLowerCase());
                    if (field != null) {
                        field.setAccessible(true);
                        field.set(instance, value);
                    }
                }
                result.add(instance);
            }
            return result;
        } catch (Exception e) {
            throw new MapEntityConversionException(e);
        }
    }

    private static Map<String, Field> getColumnToFieldMap(Class<?> entityClass) {
        Map<String, Field> columnToFieldMap = new HashMap<>();
        for (Field field : entityClass.getDeclaredFields()) {
            String columnName = getColumnNameFromAnnotation(field);
            if (columnName != null && !columnName.isEmpty()) {
                columnToFieldMap.put(columnName.toLowerCase(), field);
            }
        }
        return columnToFieldMap;
    }

    public static String getColumnName(SFunction<?, ?> function) {
        Method targetMethod = FunctionalInterfaceUtil.getLambdaInnerMethod(function);
        String methodName = targetMethod.getName();
        String fieldName = convertGetterToFieldName(methodName);
        return MybatisPlusEntityParseUtil.getColumnName(targetMethod.getDeclaringClass(), fieldName);
    }

    public static String getColumnName(Class<?> entityClass, String propertyName) {
        try {
            Field field = entityClass.getDeclaredField(propertyName);
            field.setAccessible(true);
            return getColumnNameFromAnnotation(field);
        } catch (NoSuchFieldException e) {
            throw new PropertyColumnNameNotFoundException(e);
        }
    }

    public static String getTableName(SFunction<?, ?> function) {
        Method targetMethod = FunctionalInterfaceUtil.getLambdaInnerMethod(function);
        Class<?> entityClass = targetMethod.getDeclaringClass();
        return getTableNameFromAnnotation(entityClass);
    }

    private static String getColumnNameFromAnnotation(Field field) {
        TableField tableField = field.getAnnotation(TableField.class);
        TableId tableId = field.getAnnotation(TableId.class);

        if (tableId != null && !tableId.value().isEmpty()) {
            return tableId.value();
        } else if (tableField != null && !tableField.value().isEmpty()) {
            return tableField.value();
        } else {
            return camelCaseTransSnakeCase(field.getName());
        }
    }

    private static <T> String getTableNameFromAnnotation(Class<T> entityClass) {
        TableName tableName = entityClass.getAnnotation(TableName.class);
        if (tableName != null && !tableName.value().isEmpty()) {
            return tableName.value();
        } else {
            return camelCaseTransSnakeCase(entityClass.getSimpleName());
        }
    }

    public static String convertGetterToFieldName(String methodName) {
        if (methodName.startsWith(GET)) {
            return methodName.substring(3, 4).toLowerCase(Locale.ROOT) + methodName.substring(4);
        } else if (methodName.startsWith(IS)) {
            return methodName.substring(2, 3).toLowerCase(Locale.ROOT) + methodName.substring(3);
        }
        throw new InvalidGetterMethodException();
    }

    public static String camelCaseTransSnakeCase(String camelCase) {
        return CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, camelCase);
    }
}
