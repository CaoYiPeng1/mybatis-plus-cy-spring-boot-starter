package com.cyp.mybatisplus.cy.list;

import com.cyp.mybatisplus.cy.entity.M2mDictMap;
import com.cyp.mybatisplus.cy.functionalinterface.FunctionalInterfaceUtil;
import com.cyp.mybatisplus.cy.functionalinterface.SFunction;
import com.cyp.mybatisplus.cy.functionalinterface.TriFunction;
import com.cyp.mybatisplus.cy.util.BeanCyUtils;
import com.cyp.mybatisplus.cy.util.MybatisPlusEntityParseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * JdbcArrayList
 *
 * @author cyp
 * @since 2025/5/20
 */
@SuppressWarnings("all")
@Slf4j
public class JdbcArrayList<E> extends CyArrayList<E> {
    public JdbcArrayList() {
    }

    public JdbcArrayList(List<E> list) {
        super(list);
    }

    @SuppressWarnings("unchecked")
    public <M, R> JdbcArrayList<M> autoExecuteSelectInSqlByElementProperty(SFunction<M, R> tableNameAndFieldNameFunc, Function<E, R> inParamValueFunc) {
        JdbcArrayList<R> distinctKey = distinctByKey(inParamValueFunc);
        if (CollectionUtils.isEmpty(distinctKey)) {
            return new JdbcArrayList<>();
        }
        List<Map<String, Object>> maps = MybatisPlusEntityParseUtil.getSqlSelectInSecurityAndExecute(tableNameAndFieldNameFunc, distinctKey);
        Method targetMethod = FunctionalInterfaceUtil.getLambdaInnerMethod(tableNameAndFieldNameFunc);
        Class<M> entityClass = (Class<M>) targetMethod.getDeclaringClass();
        List<M> ms = MybatisPlusEntityParseUtil.mapToEntityList(entityClass, maps);
        return new JdbcArrayList<>(ms);
    }

    public <R, R1> Map<R, R1> distinctByKeyAndCreateNewByHandlerAndO2oMapByKey(Function<E, R> keyFunc1, SFunction<R1, R> keyFunc3) {
        return autoExecuteSelectInSqlByElementProperty(keyFunc3, keyFunc1).o2oMapByKey(keyFunc3);
    }

    public <R, R1> Map<R, List<R1>> distinctByKeyAndCreateNewByHandlerAndO2mMapByKey(Function<E, R> keyFunc1, SFunction<R1, R> keyFunc3) {
        return autoExecuteSelectInSqlByElementProperty(keyFunc3, keyFunc1).o2mMapByKey(keyFunc3);
    }

    public <R, R1, R3> Map<R, M2mDictMap<R, R1, R3>> distinctByKeyAndCreateNewByHandlerAndM2mDictMapByKey(
            Function<E, R> keyFunc1, SFunction<R1, R> keyFunc3
            , Function<R1, R> keyFunc4, SFunction<R3, R> keyFunc6) {
        JdbcArrayList<R1> select1 = autoExecuteSelectInSqlByElementProperty(keyFunc3, keyFunc1);
        Map<R, List<R1>> dictMap1 = select1.o2mMapByKey(keyFunc3);
        Map<R, R3> dictMap2 = select1.autoExecuteSelectInSqlByElementProperty(keyFunc6, keyFunc4).o2oMapByKey(keyFunc6);
        Map<R, CyArrayList<R3>> dictMap2Buff = dictMap1.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey
                , e -> new CyArrayList<>(e.getValue().stream().map(e1 -> dictMap2.get(keyFunc4.apply(e1))).collect(Collectors.toCollection(ArrayList::new)))));
        return M2mDictMap.createBy2Map(dictMap1.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> new CyArrayList<>(e.getValue())))
                , dictMap2Buff);
    }

    public <R, R1, R2> JdbcArrayList<R2> distinctByKeyAndCreateNewByHandlerAndO2oMapByKeyAndCreateNewByDictMap(Function<E, R> keyFunc1, SFunction<R1, R> keyFunc3, BiFunction<E, R1, R2> resultMap4) {
        Map<R, R1> dictMap = autoExecuteSelectInSqlByElementProperty(keyFunc3, keyFunc1).o2oMapByKey(keyFunc3);
        return this.createNewByDictMap(keyFunc1, dictMap, resultMap4);
    }

    public <R, R1> JdbcArrayList<Map<String, Object>> distinctByKeyAndCreateNewByHandlerAndO2oMapByKeyAndCreateNewByDictMap(Function<E, R> keyFunc1, SFunction<R1, R> keyFunc3) {
        Map<R, R1> dictMap = autoExecuteSelectInSqlByElementProperty(keyFunc3, keyFunc1).o2oMapByKey(keyFunc3);
        return this.createNewByDictMap(keyFunc1, dictMap, (r1, r2) -> BeanCyUtils.m2oGetMap(r1, r2, FunctionalInterfaceUtil.getLambdaInnerMethod(keyFunc3).getDeclaringClass()));
    }

    public <R, R1, R2> JdbcArrayList<R2> distinctByKeyAndCreateNewByHandlerAndO2mMapByKeyAndCreateNewByDictMap(Function<E, R> keyFunc1, SFunction<R1, R> keyFunc3, BiFunction<E, List<R1>, R2> resultMap4) {
//        Map<R, List<R1>> dictMap = distinctByKey(keyFunc1).createNewByHandler(handlerFunc2).o2mMapByKey(keyFunc3);
        Map<R, List<R1>> dictMap = autoExecuteSelectInSqlByElementProperty(keyFunc3, keyFunc1).o2mMapByKey(keyFunc3);
        return this.createNewByDictMap(keyFunc1, dictMap, resultMap4);
    }

    public <R, R1> JdbcArrayList<Map<String, Object>> distinctByKeyAndCreateNewByHandlerAndO2mMapByKeyAndCreateNewByDictMap(Function<E, R> keyFunc1, SFunction<R1, R> keyFunc3) {
        Map<R, List<R1>> dictMap = autoExecuteSelectInSqlByElementProperty(keyFunc3, keyFunc1).o2mMapByKey(keyFunc3);
        return this.createNewByDictMap(keyFunc1, dictMap, (r1, r2s) -> BeanCyUtils.o2mGetMap(r1, r2s, FunctionalInterfaceUtil.getLambdaInnerMethod(keyFunc3).getDeclaringClass()));
    }

    public <R, R1, R2, R3> JdbcArrayList<R2> distinctByKeyAndCreateNewByHandlerAndM2mDictMapByKeyAndCreateNewByDictMap(
            Function<E, R> keyFunc1, SFunction<R1, R> keyFunc3
            , Function<R1, R> keyFunc4, SFunction<R3, R> keyFunc6
            , TriFunction<E, CyArrayList<R1>, CyArrayList<R3>, R2> resultMap7) {
        Map<R, M2mDictMap<R, R1, R3>> m2mDictMap = this.distinctByKeyAndCreateNewByHandlerAndM2mDictMapByKey(keyFunc1, keyFunc3, keyFunc4, keyFunc6);
        return this.createNewByDictMap(keyFunc1, m2mDictMap, resultMap7);
    }

    public <R, R1, R3> JdbcArrayList<Map<String, Object>> distinctByKeyAndCreateNewByHandlerAndM2mDictMapByKeyAndCreateNewByDictMap(
            Function<E, R> keyFunc1, SFunction<R1, R> keyFunc3
            , Function<R1, R> keyFunc4, SFunction<R3, R> keyFunc6) {
        Map<R, M2mDictMap<R, R1, R3>> m2mDictMap = this.distinctByKeyAndCreateNewByHandlerAndM2mDictMapByKey(keyFunc1, keyFunc3, keyFunc4, keyFunc6);
        return this.createNewByDictMap(keyFunc1, m2mDictMap, (r1, r2s, r3s) -> BeanCyUtils.m2mGetMap(r1, r2s, FunctionalInterfaceUtil.getLambdaInnerMethod(keyFunc3).getDeclaringClass(), r3s, FunctionalInterfaceUtil.getLambdaInnerMethod(keyFunc6).getDeclaringClass()));
    }

    public <R, R1, R3> JdbcArrayList<Map<String, Object>> ezDistinctByKeyAndCreateNewByHandlerAndM2mDictMapByKeyAndCreateNewByDictMap(
            Function<E, R> keyFunc1, SFunction<R1, R> keyFunc3
            , Function<R1, R> keyFunc4, SFunction<R3, R> keyFunc6) {
        Map<R, M2mDictMap<R, R1, R3>> m2mDictMap = this.distinctByKeyAndCreateNewByHandlerAndM2mDictMapByKey(keyFunc1, keyFunc3, keyFunc4, keyFunc6);
        return this.createNewByDictMap(keyFunc1, m2mDictMap, (r1, r2s, r3s) -> BeanCyUtils.o2mGetMap(r1, r3s, FunctionalInterfaceUtil.getLambdaInnerMethod(keyFunc6).getDeclaringClass()));
    }

    public <R, R1, R2> JdbcArrayList<R2> m2o(Function<E, R> keyFunc1, SFunction<R1, R> keyFunc3, BiFunction<E, R1, R2> resultMap4) {
        return this.distinctByKeyAndCreateNewByHandlerAndO2oMapByKeyAndCreateNewByDictMap(keyFunc1, keyFunc3, resultMap4);
    }

    public <R, R1> JdbcArrayList<Map<String, Object>> m2o(Function<E, R> keyFunc1, SFunction<R1, R> keyFunc3) {
        return this.distinctByKeyAndCreateNewByHandlerAndO2oMapByKeyAndCreateNewByDictMap(keyFunc1, keyFunc3);
    }

    public <R, R1, R2> JdbcArrayList<R2> o2m(Function<E, R> keyFunc1, SFunction<R1, R> keyFunc3, BiFunction<E, List<R1>, R2> resultMap4) {
        return this.distinctByKeyAndCreateNewByHandlerAndO2mMapByKeyAndCreateNewByDictMap(keyFunc1, keyFunc3, resultMap4);
    }

    public <R, R1> JdbcArrayList<Map<String, Object>> o2m(Function<E, R> keyFunc1, SFunction<R1, R> keyFunc3) {
        return this.distinctByKeyAndCreateNewByHandlerAndO2mMapByKeyAndCreateNewByDictMap(keyFunc1, keyFunc3);
    }

    public <R, R1, R2, R3> JdbcArrayList<R2> m2m(
            Function<E, R> keyFunc1, SFunction<R1, R> keyFunc3
            , Function<R1, R> keyFunc4, SFunction<R3, R> keyFunc6
            , TriFunction<E, CyArrayList<R1>, CyArrayList<R3>, R2> resultMap7) {
        return this.distinctByKeyAndCreateNewByHandlerAndM2mDictMapByKeyAndCreateNewByDictMap(keyFunc1, keyFunc3, keyFunc4, keyFunc6, resultMap7);
    }

    public <R, R1, R3> JdbcArrayList<Map<String, Object>> m2m(
            Function<E, R> keyFunc1, SFunction<R1, R> keyFunc3
            , Function<R1, R> keyFunc4, SFunction<R3, R> keyFunc6) {
        return this.distinctByKeyAndCreateNewByHandlerAndM2mDictMapByKeyAndCreateNewByDictMap(keyFunc1, keyFunc3, keyFunc4, keyFunc6);
    }

    public <R, R1, R3> JdbcArrayList<Map<String, Object>> ezM2m(
            Function<E, R> keyFunc1, SFunction<R1, R> keyFunc3
            , Function<R1, R> keyFunc4, SFunction<R3, R> keyFunc6) {
        return this.ezDistinctByKeyAndCreateNewByHandlerAndM2mDictMapByKeyAndCreateNewByDictMap(keyFunc1, keyFunc3, keyFunc4, keyFunc6);
    }

    public <R, R1> Map<R, R1> m2oDict(Function<E, R> keyFunc1, SFunction<R1, R> keyFunc3) {
        return this.distinctByKeyAndCreateNewByHandlerAndO2oMapByKey(keyFunc1, keyFunc3);
    }

    public <R, R1> Map<R, R1> m2oDict(Function<E, R> keyFunc1, Function<CyArrayList<R>, List<R1>> handlerFunc2, Function<R1, R> keyFunc3) {
        return this.distinctByKeyAndCreateNewByHandlerAndO2oMapByKey(keyFunc1, handlerFunc2, keyFunc3);
    }

    public <R, R1> Map<R, List<R1>> o2mDict(Function<E, R> keyFunc1, SFunction<R1, R> keyFunc3) {
        return this.distinctByKeyAndCreateNewByHandlerAndO2mMapByKey(keyFunc1, keyFunc3);
    }

    public <R, R1> Map<R, List<R1>> o2mDict(Function<E, R> keyFunc1, Function<CyArrayList<R>, List<R1>> handlerFunc2, Function<R1, R> keyFunc3) {
        return this.distinctByKeyAndCreateNewByHandlerAndO2mMapByKey(keyFunc1, handlerFunc2, keyFunc3);
    }

    public <R, R1, R3> Map<R, M2mDictMap<R, R1, R3>> m2mDict(
            Function<E, R> keyFunc1, SFunction<R1, R> keyFunc3
            , Function<R1, R> keyFunc4, SFunction<R3, R> keyFunc6) {
        return this.distinctByKeyAndCreateNewByHandlerAndM2mDictMapByKey(keyFunc1, keyFunc3, keyFunc4, keyFunc6);
    }

    public <R, R1, R3> Map<R, M2mDictMap<R, R1, R3>> m2mDict(Function<E, R> keyFunc1, Function<CyArrayList<R>, List<R1>> handlerFunc2
            , Function<R1, R> keyFunc3
            , Function<R1, R> keyFunc4, Function<CyArrayList<R>, List<R3>> handlerFunc5, Function<R3, R> keyFunc6) {
        return this.distinctByKeyAndCreateNewByHandlerAndM2mDictMapByKey(keyFunc1, handlerFunc2, keyFunc3, keyFunc4, handlerFunc5, keyFunc6);
    }

    @Override
    public <R> JdbcArrayList<R> distinctByKey(Function<E, R> keyFunc) {
        return new JdbcArrayList<>(super.distinctByKey(keyFunc));
    }

    @Override
    public <R> JdbcArrayList<R> createNewByHandler(Function<CyArrayList<E>, List<R>> handlerFunc) {
        return new JdbcArrayList<>(super.createNewByHandler(handlerFunc));
    }

    @Override
    public <R, R1> JdbcArrayList<R1> distinctByKeyAndCreateNewByHandler(Function<E, R> keyFunc1, Function<CyArrayList<R>, List<R1>> handlerFunc2) {
        return new JdbcArrayList<>(super.distinctByKeyAndCreateNewByHandler(keyFunc1, handlerFunc2));
    }

    @Override
    public <R, R1, R2> JdbcArrayList<R2> createNewByDictMap(Function<E, R> keyFunc1, Map<R, R1> dictMap2, BiFunction<E, R1, R2> resultMap) {
        return new JdbcArrayList<>(super.createNewByDictMap(keyFunc1, dictMap2, resultMap));
    }

    @Override
    public <R, R1, R2, R3> JdbcArrayList<R2> createNewByDictMap(Function<E, R> keyFunc1, Map<R, M2mDictMap<R, R1, R3>> dictMap2, TriFunction<E, CyArrayList<R1>, CyArrayList<R3>, R2> resultMap) {
        return new JdbcArrayList<>(super.createNewByDictMap(keyFunc1, dictMap2, resultMap));
    }

    @Override
    public <R, R1, R2> JdbcArrayList<R2> distinctByKeyAndCreateNewByHandlerAndO2oMapByKeyAndCreateNewByDictMap(Function<E, R> keyFunc1, Function<CyArrayList<R>, List<R1>> handlerFunc2, Function<R1, R> keyFunc3, BiFunction<E, R1, R2> resultMap4) {
        return new JdbcArrayList<>(super.distinctByKeyAndCreateNewByHandlerAndO2oMapByKeyAndCreateNewByDictMap(keyFunc1, handlerFunc2, keyFunc3, resultMap4));
    }

    @Override
    public <R, R1> JdbcArrayList<Map<String, Object>> distinctByKeyAndCreateNewByHandlerAndO2oMapByKeyAndCreateNewByDictMap(Function<E, R> keyFunc1, Function<CyArrayList<R>, List<R1>> handlerFunc2, SFunction<R1, R> keyFunc3) {
        return new JdbcArrayList<>(super.distinctByKeyAndCreateNewByHandlerAndO2oMapByKeyAndCreateNewByDictMap(keyFunc1, handlerFunc2, keyFunc3));
    }

    @Override
    public <R, R1, R2> JdbcArrayList<R2> distinctByKeyAndCreateNewByHandlerAndO2mMapByKeyAndCreateNewByDictMap(Function<E, R> keyFunc1, Function<CyArrayList<R>, List<R1>> handlerFunc2, Function<R1, R> keyFunc3, BiFunction<E, List<R1>, R2> resultMap4) {
        return new JdbcArrayList<>(super.distinctByKeyAndCreateNewByHandlerAndO2mMapByKeyAndCreateNewByDictMap(keyFunc1, handlerFunc2, keyFunc3, resultMap4));
    }

    @Override
    public <R, R1> JdbcArrayList<Map<String, Object>> distinctByKeyAndCreateNewByHandlerAndO2mMapByKeyAndCreateNewByDictMap(Function<E, R> keyFunc1, Function<CyArrayList<R>, List<R1>> handlerFunc2, SFunction<R1, R> keyFunc3) {
        return new JdbcArrayList<>(super.distinctByKeyAndCreateNewByHandlerAndO2mMapByKeyAndCreateNewByDictMap(keyFunc1, handlerFunc2, keyFunc3));
    }

    @Override
    public <R, R1, R2, R3> JdbcArrayList<R2> distinctByKeyAndCreateNewByHandlerAndM2mDictMapByKeyAndCreateNewByDictMap(Function<E, R> keyFunc1, Function<CyArrayList<R>, List<R1>> handlerFunc2
            , Function<R1, R> keyFunc3
            , Function<R1, R> keyFunc4, Function<CyArrayList<R>, List<R3>> handlerFunc5, Function<R3, R> keyFunc6
            , TriFunction<E, CyArrayList<R1>, CyArrayList<R3>, R2> resultMap7) {
        return new JdbcArrayList<>(super.distinctByKeyAndCreateNewByHandlerAndM2mDictMapByKeyAndCreateNewByDictMap(keyFunc1, handlerFunc2, keyFunc3, keyFunc4, handlerFunc5, keyFunc6, resultMap7));
    }

    @Override
    public <R, R1, R3> JdbcArrayList<Map<String, Object>> distinctByKeyAndCreateNewByHandlerAndM2mDictMapByKeyAndCreateNewByDictMap(Function<E, R> keyFunc1, Function<CyArrayList<R>, List<R1>> handlerFunc2
            , SFunction<R1, R> keyFunc3
            , Function<R1, R> keyFunc4, Function<CyArrayList<R>, List<R3>> handlerFunc5, SFunction<R3, R> keyFunc6) {
        return new JdbcArrayList<>(super.distinctByKeyAndCreateNewByHandlerAndM2mDictMapByKeyAndCreateNewByDictMap(keyFunc1, handlerFunc2, keyFunc3, keyFunc4, handlerFunc5, keyFunc6));
    }

    @Override
    public <R, R1, R2> JdbcArrayList<R2> m2o(Function<E, R> keyFunc1, Function<CyArrayList<R>, List<R1>> handlerFunc2, Function<R1, R> keyFunc3, BiFunction<E, R1, R2> resultMap4) {
        return new JdbcArrayList<>(super.m2o(keyFunc1, handlerFunc2, keyFunc3, resultMap4));
    }

    @Override
    public <R, R1> JdbcArrayList<Map<String, Object>> m2o(Function<E, R> keyFunc1, Function<CyArrayList<R>, List<R1>> handlerFunc2, SFunction<R1, R> keyFunc3) {
        return new JdbcArrayList<>(super.m2o(keyFunc1, handlerFunc2, keyFunc3));
    }

    @Override
    public <R, R1, R2> JdbcArrayList<R2> o2m(Function<E, R> keyFunc1, Function<CyArrayList<R>, List<R1>> handlerFunc2, Function<R1, R> keyFunc3, BiFunction<E, List<R1>, R2> resultMap4) {
        return new JdbcArrayList<>(super.o2m(keyFunc1, handlerFunc2, keyFunc3, resultMap4));
    }

    @Override
    public <R, R1> JdbcArrayList<Map<String, Object>> o2m(Function<E, R> keyFunc1, Function<CyArrayList<R>, List<R1>> handlerFunc2, SFunction<R1, R> keyFunc3) {
        return new JdbcArrayList<>(super.o2m(keyFunc1, handlerFunc2, keyFunc3));
    }

    @Override
    public <R, R1, R2, R3> JdbcArrayList<R2> m2m(Function<E, R> keyFunc1, Function<CyArrayList<R>, List<R1>> handlerFunc2
            , Function<R1, R> keyFunc3
            , Function<R1, R> keyFunc4, Function<CyArrayList<R>, List<R3>> handlerFunc5, Function<R3, R> keyFunc6
            , TriFunction<E, CyArrayList<R1>, CyArrayList<R3>, R2> resultMap7) {
        return new JdbcArrayList<>(super.m2m(keyFunc1, handlerFunc2, keyFunc3, keyFunc4, handlerFunc5, keyFunc6, resultMap7));
    }

    @Override
    public <R, R1, R3> JdbcArrayList<Map<String, Object>> m2m(Function<E, R> keyFunc1, Function<CyArrayList<R>, List<R1>> handlerFunc2
            , SFunction<R1, R> keyFunc3
            , Function<R1, R> keyFunc4, Function<CyArrayList<R>, List<R3>> handlerFunc5, SFunction<R3, R> keyFunc6) {
        return new JdbcArrayList<>(super.m2m(keyFunc1, handlerFunc2, keyFunc3, keyFunc4, handlerFunc5, keyFunc6));
    }
}
