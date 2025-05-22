package com.cyp.mybatisplus.cy.entity;

import com.cyp.mybatisplus.cy.list.CyArrayList;
import lombok.Data;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * M2mDictMap
 *
 * @author cyp
 * @since 2025/5/20
 */
@SuppressWarnings("all")
@Data
public class M2mDictMap<T, U, V> {

    private T record1;

    private CyArrayList<U> recordList2;

    private CyArrayList<V> recordList3;

    public M2mDictMap() {
    }

    public M2mDictMap(T record1, CyArrayList<U> recordList2, CyArrayList<V> recordList3) {
        this.record1 = record1;
        this.recordList2 = recordList2;
        this.recordList3 = recordList3;
    }

    public static <T, U, V> Map<T, M2mDictMap<T, U, V>> createBy2Map(Map<T, CyArrayList<U>> map1, Map<T, CyArrayList<V>> map2) {
        return map1.entrySet().stream().collect(Collectors
                .toMap(Map.Entry::getKey, e -> new M2mDictMap<>(e.getKey(), e.getValue(), map2.get(e.getKey()))));
    }
}
