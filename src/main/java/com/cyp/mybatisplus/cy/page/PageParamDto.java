package com.cyp.mybatisplus.cy.page;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cyp.mybatisplus.cy.exception.InvalidFieldPrefixException;
import com.cyp.mybatisplus.cy.exception.UnauthorizedSortingFieldException;
import com.cyp.mybatisplus.cy.functionalinterface.FunctionalInterfaceUtil;
import com.cyp.mybatisplus.cy.functionalinterface.SFunction;
import com.cyp.mybatisplus.cy.util.MybatisPlusEntityParseUtil;
import com.google.common.base.CaseFormat;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Method;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * PageParamDto
 * <E>是拓展dto类结构
 *
 * @author cyp
 * @since 2025/5/19
 */
@SuppressWarnings("all")
public class PageParamDto<E> {
    private static final String ADD = "+";
    private static final String SUB = "-";
    private final Set<String> validateSortFieldList = new HashSet<>();
    private final Map<String, String> propertyName2ColumnNameMap = new HashMap<>();
    @Getter
    @Setter
    private Integer pageNum;
    @Getter
    @Setter
    private Integer pageSize;
    @Getter
    @Setter
    private List<String> sortFieldList = new ArrayList<>();
    @Getter
    @Setter
    private E entityDto;

    public PageParamDto() {
    }

    public PageParamDto(Integer pageNum, Integer pageSize) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
    }

    public PageParamDto(Integer pageNum, Integer pageSize, List<String> sortFieldList) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.sortFieldList = sortFieldList;
    }

    public PageParamDto(Integer pageNum, Integer pageSize, List<String> sortFieldList, E entityDto) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.sortFieldList = sortFieldList;
        this.entityDto = entityDto;
    }

    public <R, R1> CyPage<R> execute(Class<R1> clazz, Function<Page<R1>, Page<R>> selectPageFunc) {
        return new CyPage<>(selectPageFunc.apply(page()));
    }

    public <R> CyPage<R> execute(Function<Page<?>, Page<R>> selectPageFunc) {
        return new CyPage<>(selectPageFunc.apply(page()));
    }

    public <R> CyPage<R> executeByTypePage(Function<Page<E>, Page<R>> selectPageFunc) {
        return new CyPage<>(selectPageFunc.apply(page()));
    }


    public <R> Page<R> page() {
        return pageResolveSortField((sortEnum, sortField) -> {
            sortField = propertyName2ColumnNameMap.get(sortField);
            return sortEnum.isASC() ? new OrderItem().setAsc(true).setColumn(sortField) : new OrderItem().setAsc(false).setColumn(sortField);
        });
    }

    public <R> Page<R> pageResolveSortField(BiFunction<SortEnum, String, OrderItem> sortFunc) {
        Page<R> page = new Page<>(pageNum, pageSize);
        page.setOrders(
                sortFieldList.stream().collect(Collectors
                                .toMap(sortField -> sortField.substring(1), sortField -> sortField.substring(0, 1), (k1, k2) -> k1, LinkedHashMap::new))
                        .entrySet().stream()
                        .filter(entry -> {
                            boolean contains = validateSortFieldList.contains(entry.getKey());
                            if (!contains) {
                                throw new UnauthorizedSortingFieldException(entry.getKey());
                            }
                            return true;
                        }) //校验合法字段
                        .map(entry -> sortFunc.apply(SortEnum.getTrance((entry.getValue())), entry.getKey()))
                        .collect(Collectors.toList()));
        return page;
    }

    @SafeVarargs
    public final <R> PageParamDto<E> validateSortFields(SFunction<R, ?>... functions) {
        Stream.of(functions).forEach(this::validateSortFieldsStep);
        return this;
    }

    private <R> void validateSortFieldsStep(SFunction<R, ?> function) {
        Method targetMethod = FunctionalInterfaceUtil.getLambdaInnerMethod(function);
        String methodName = targetMethod.getName();
        String fieldName = MybatisPlusEntityParseUtil.convertGetterToFieldName(methodName);
        String columnName = MybatisPlusEntityParseUtil.getColumnName(targetMethod.getDeclaringClass(), fieldName);
        propertyName2ColumnNameMap.put(fieldName, columnName);
        validateSortFieldList.add(fieldName);
    }

    public String camelCaseTransSnakeCase(String camelCase) {
        return CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, camelCase);
    }


    public enum SortEnum {
        ASC,
        DESC,
        ;

        public static SortEnum getTrance(String param) {
            if (ADD.equals(param)) {
                return ASC;
            }
            if (SUB.equals(param)) {
                return DESC;
            }
            throw new InvalidFieldPrefixException();
        }

        public boolean isASC() {
            return this.equals(ASC);
        }

        public boolean isDESC() {
            return this.equals(DESC);
        }

    }

}
