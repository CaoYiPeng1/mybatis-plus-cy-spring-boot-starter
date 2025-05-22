package com.cyp.mybatisplus.cy.list;

import com.cyp.mybatisplus.cy.entity.M2mDictMap;
import com.cyp.mybatisplus.cy.functionalinterface.FunctionalInterfaceUtil;
import com.cyp.mybatisplus.cy.functionalinterface.SFunction;
import com.cyp.mybatisplus.cy.functionalinterface.TriFunction;
import com.cyp.mybatisplus.cy.util.BeanCyUtils;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * CyArrayList
 *
 * @author cyp
 * @since 2025/5/20
 */
@SuppressWarnings("all")
public class CyArrayList<E> extends ArrayList<E> {
    public CyArrayList() {
        super();
    }

    public CyArrayList(ArrayList<E> list) {
        super(list);
    }

    public CyArrayList(List<E> list) {
        super(list);
    }

    public <R> CyArrayList<R> distinctByKey(Function<E, R> keyFunc) {
        ArrayList<R> listRs = this.stream().map(keyFunc).distinct().collect(Collectors.toCollection(ArrayList::new));
        return new CyArrayList<>(listRs);
    }

    public <R> CyArrayList<R> createNewByHandler(Function<CyArrayList<E>, List<R>> handlerFunc) {
        if (CollectionUtils.isEmpty(this)) {
            return new CyArrayList<>();
        }
        List<R> listRs = handlerFunc.apply(this);
        return new CyArrayList<>(listRs);
    }

    public <R, R1> CyArrayList<R1> distinctByKeyAndCreateNewByHandler(Function<E, R> keyFunc1, Function<CyArrayList<R>, List<R1>> handlerFunc2) {
        return distinctByKey(keyFunc1).createNewByHandler(handlerFunc2);
    }

    public <R> Map<R, E> o2oMapByKey(Function<E, R> keyFunc) {
        return this.stream().collect(Collectors
                .toMap(keyFunc, e -> e
                ));
    }

    public <R> Map<R, List<E>> o2mMapByKey(Function<E, R> keyFunc) {
        return this.stream().collect(Collectors.groupingBy(keyFunc, Collectors.toCollection(ArrayList::new)));
    }

    public <R, R1> Map<R, R1> distinctByKeyAndCreateNewByHandlerAndO2oMapByKey(Function<E, R> keyFunc1, Function<CyArrayList<R>, List<R1>> handlerFunc2, Function<R1, R> keyFunc3) {
        return distinctByKey(keyFunc1).createNewByHandler(handlerFunc2).o2oMapByKey(keyFunc3);
    }

    public <R, R1> Map<R, List<R1>> distinctByKeyAndCreateNewByHandlerAndO2mMapByKey(Function<E, R> keyFunc1, Function<CyArrayList<R>, List<R1>> handlerFunc2, Function<R1, R> keyFunc3) {
        return distinctByKey(keyFunc1).createNewByHandler(handlerFunc2).o2mMapByKey(keyFunc3);
    }

    public <R, R1, R3> Map<R, M2mDictMap<R, R1, R3>> distinctByKeyAndCreateNewByHandlerAndM2mDictMapByKey(Function<E, R> keyFunc1, Function<CyArrayList<R>, List<R1>> handlerFunc2
            , Function<R1, R> keyFunc3
            , Function<R1, R> keyFunc4, Function<CyArrayList<R>, List<R3>> handlerFunc5, Function<R3, R> keyFunc6) {
        CyArrayList<R1> select1 = distinctByKey(keyFunc1).createNewByHandler(handlerFunc2);
        Map<R, List<R1>> dictMap1 = select1.o2mMapByKey(keyFunc3);
        Map<R, R3> dictMap2 = select1.distinctByKey(keyFunc4).createNewByHandler(handlerFunc5).o2oMapByKey(keyFunc6);
        Map<R, CyArrayList<R3>> dictMap2Buff = dictMap1.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey
                , e -> new CyArrayList<>(e.getValue().stream().map(e1 -> dictMap2.get(keyFunc4.apply(e1))).collect(Collectors.toCollection(ArrayList::new)))));
        return M2mDictMap.createBy2Map(dictMap1.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> new CyArrayList<>(e.getValue())))
                , dictMap2Buff);
    }


    public <R, R1, R2> CyArrayList<R2> createNewByDictMap(Function<E, R> keyFunc1, Map<R, R1> dictMap2, BiFunction<E, R1, R2> resultMap) {
        ArrayList<R2> listRs = this.stream().map(e -> resultMap.apply(e, dictMap2.get(keyFunc1.apply(e)))).collect(Collectors.toCollection(ArrayList::new));
        return new CyArrayList<>(listRs);
    }

    public <R, R1, R2, R3> CyArrayList<R2> createNewByDictMap(Function<E, R> keyFunc1, Map<R, M2mDictMap<R, R1, R3>> dictMap2, TriFunction<E, CyArrayList<R1>, CyArrayList<R3>, R2> resultMap) {
        List<R2> listRs = this.stream().map(e -> {
            M2mDictMap<R, R1, R3> m2mDictMap = dictMap2.get(keyFunc1.apply(e));
            if (m2mDictMap == null) {
                m2mDictMap = new M2mDictMap<>(null, new CyArrayList<>(), new CyArrayList<>());
            }
            return resultMap.apply(e, m2mDictMap.getRecordList2(), m2mDictMap.getRecordList3());
        }).collect(Collectors.toCollection(ArrayList::new));
        return new CyArrayList<>(listRs);
    }

    public <R, R1, R2> CyArrayList<R2> distinctByKeyAndCreateNewByHandlerAndO2oMapByKeyAndCreateNewByDictMap(Function<E, R> keyFunc1, Function<CyArrayList<R>, List<R1>> handlerFunc2, Function<R1, R> keyFunc3, BiFunction<E, R1, R2> resultMap4) {
        Map<R, R1> dictMap = distinctByKey(keyFunc1).createNewByHandler(handlerFunc2).o2oMapByKey(keyFunc3);
        return this.createNewByDictMap(keyFunc1, dictMap, resultMap4);
    }

    public <R, R1> CyArrayList<Map<String, Object>> distinctByKeyAndCreateNewByHandlerAndO2oMapByKeyAndCreateNewByDictMap(Function<E, R> keyFunc1, Function<CyArrayList<R>, List<R1>> handlerFunc2, SFunction<R1, R> keyFunc3) {
        return this.distinctByKeyAndCreateNewByHandlerAndO2oMapByKeyAndCreateNewByDictMap(keyFunc1, handlerFunc2, keyFunc3, (r1, r2) -> BeanCyUtils.m2oGetMap(r1, r2, FunctionalInterfaceUtil.getLambdaInnerMethod(keyFunc3).getDeclaringClass()));
    }

    public <R, R1, R2> CyArrayList<R2> distinctByKeyAndCreateNewByHandlerAndO2mMapByKeyAndCreateNewByDictMap(Function<E, R> keyFunc1, Function<CyArrayList<R>, List<R1>> handlerFunc2, Function<R1, R> keyFunc3, BiFunction<E, List<R1>, R2> resultMap4) {
        Map<R, List<R1>> dictMap = distinctByKey(keyFunc1).createNewByHandler(handlerFunc2).o2mMapByKey(keyFunc3);
        return this.createNewByDictMap(keyFunc1, dictMap, resultMap4);
    }

    public <R, R1> CyArrayList<Map<String, Object>> distinctByKeyAndCreateNewByHandlerAndO2mMapByKeyAndCreateNewByDictMap(Function<E, R> keyFunc1, Function<CyArrayList<R>, List<R1>> handlerFunc2, SFunction<R1, R> keyFunc3) {
        return this.distinctByKeyAndCreateNewByHandlerAndO2mMapByKeyAndCreateNewByDictMap(keyFunc1, handlerFunc2, keyFunc3, (r1, r2) -> BeanCyUtils.o2mGetMap(r1, r2, FunctionalInterfaceUtil.getLambdaInnerMethod(keyFunc3).getDeclaringClass()));
    }

    public <R, R1, R2, R3> CyArrayList<R2> distinctByKeyAndCreateNewByHandlerAndM2mDictMapByKeyAndCreateNewByDictMap(Function<E, R> keyFunc1, Function<CyArrayList<R>, List<R1>> handlerFunc2
            , Function<R1, R> keyFunc3
            , Function<R1, R> keyFunc4, Function<CyArrayList<R>, List<R3>> handlerFunc5, Function<R3, R> keyFunc6
            , TriFunction<E, CyArrayList<R1>, CyArrayList<R3>, R2> resultMap7) {
        Map<R, M2mDictMap<R, R1, R3>> m2mDictMap = this.distinctByKeyAndCreateNewByHandlerAndM2mDictMapByKey(keyFunc1, handlerFunc2, keyFunc3, keyFunc4, handlerFunc5, keyFunc6);
        return this.createNewByDictMap(keyFunc1, m2mDictMap, resultMap7);
    }

    public <R, R1, R3> CyArrayList<Map<String, Object>> distinctByKeyAndCreateNewByHandlerAndM2mDictMapByKeyAndCreateNewByDictMap(Function<E, R> keyFunc1, Function<CyArrayList<R>, List<R1>> handlerFunc2
            , SFunction<R1, R> keyFunc3
            , Function<R1, R> keyFunc4, Function<CyArrayList<R>, List<R3>> handlerFunc5, SFunction<R3, R> keyFunc6) {
        Map<R, M2mDictMap<R, R1, R3>> m2mDictMap = this.distinctByKeyAndCreateNewByHandlerAndM2mDictMapByKey(keyFunc1, handlerFunc2, keyFunc3, keyFunc4, handlerFunc5, keyFunc6);
        return this.createNewByDictMap(keyFunc1, m2mDictMap, (r1, r2s, r3s) -> BeanCyUtils.m2mGetMap(r1, r2s, FunctionalInterfaceUtil.getLambdaInnerMethod(keyFunc3).getDeclaringClass(), r3s, FunctionalInterfaceUtil.getLambdaInnerMethod(keyFunc6).getDeclaringClass()));
    }

    public <R, R1, R2> CyArrayList<R2> m2o(Function<E, R> keyFunc1, Function<CyArrayList<R>, List<R1>> handlerFunc2, Function<R1, R> keyFunc3, BiFunction<E, R1, R2> resultMap4) {
        return this.distinctByKeyAndCreateNewByHandlerAndO2oMapByKeyAndCreateNewByDictMap(keyFunc1, handlerFunc2, keyFunc3, resultMap4);
    }

    public <R, R1> CyArrayList<Map<String, Object>> m2o(Function<E, R> keyFunc1, Function<CyArrayList<R>, List<R1>> handlerFunc2, SFunction<R1, R> keyFunc3) {
        return this.distinctByKeyAndCreateNewByHandlerAndO2oMapByKeyAndCreateNewByDictMap(keyFunc1, handlerFunc2, keyFunc3);
    }

    public <R, R1, R2> CyArrayList<R2> o2m(Function<E, R> keyFunc1, Function<CyArrayList<R>, List<R1>> handlerFunc2, Function<R1, R> keyFunc3, BiFunction<E, List<R1>, R2> resultMap4) {
        return this.distinctByKeyAndCreateNewByHandlerAndO2mMapByKeyAndCreateNewByDictMap(keyFunc1, handlerFunc2, keyFunc3, resultMap4);
    }

    public <R, R1> CyArrayList<Map<String, Object>> o2m(Function<E, R> keyFunc1, Function<CyArrayList<R>, List<R1>> handlerFunc2, SFunction<R1, R> keyFunc3) {
        return this.distinctByKeyAndCreateNewByHandlerAndO2mMapByKeyAndCreateNewByDictMap(keyFunc1, handlerFunc2, keyFunc3);
    }

    public <R, R1, R2, R3> CyArrayList<R2> m2m(Function<E, R> keyFunc1, Function<CyArrayList<R>, List<R1>> handlerFunc2
            , Function<R1, R> keyFunc3
            , Function<R1, R> keyFunc4, Function<CyArrayList<R>, List<R3>> handlerFunc5, Function<R3, R> keyFunc6
            , TriFunction<E, CyArrayList<R1>, CyArrayList<R3>, R2> resultMap7) {
        return this.distinctByKeyAndCreateNewByHandlerAndM2mDictMapByKeyAndCreateNewByDictMap(keyFunc1, handlerFunc2, keyFunc3, keyFunc4, handlerFunc5, keyFunc6, resultMap7);
    }

    public <R, R1, R3> CyArrayList<Map<String, Object>> m2m(Function<E, R> keyFunc1, Function<CyArrayList<R>, List<R1>> handlerFunc2
            , SFunction<R1, R> keyFunc3
            , Function<R1, R> keyFunc4, Function<CyArrayList<R>, List<R3>> handlerFunc5, SFunction<R3, R> keyFunc6) {
        return this.distinctByKeyAndCreateNewByHandlerAndM2mDictMapByKeyAndCreateNewByDictMap(keyFunc1, handlerFunc2, keyFunc3, keyFunc4, handlerFunc5, keyFunc6);
    }

}
