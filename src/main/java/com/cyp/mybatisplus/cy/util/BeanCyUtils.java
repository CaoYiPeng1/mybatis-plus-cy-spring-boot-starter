package com.cyp.mybatisplus.cy.util;

import com.cyp.mybatisplus.cy.exception.CreateMapException;
import com.cyp.mybatisplus.cy.exception.CreateObjectException;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

/**
 * BeanUtils
 *
 * @author cyp
 * @since 2025/5/20
 */
@SuppressWarnings("all")
public class BeanCyUtils {
    public static <R> R cp(R target, Object... sources) {
        Arrays.stream(sources)
                .forEach(source -> org.springframework.beans.BeanUtils.copyProperties(source, target));
        return target;
    }

    public static <R> R cp(Class<R> targetClazz, Object... sources) {
        try {
            R target = targetClazz.getDeclaredConstructor().newInstance();
            return cp(target, sources);
        } catch (Exception e) {
            throw new CreateObjectException(e);
        }
    }

    public static Map<String, Object> m2oGetMap(Object objectA, Object objectB, Class<?> clazzObjectB) {
        return getMapByAggObject(objectA, objectB, clazzObjectB);
    }

    public static Map<String, Object> m2oGetMap(Object objectA, Object objectB) {
        return getMapByAggObject(objectA, objectB);
    }

    public static Map<String, Object> o2mGetMap(Object objectA, List<?> listB, Class<?> clazzObjectB) {
        return getMapByAggObjectAndInnerList(objectA, listB, clazzObjectB);
    }

    public static Map<String, Object> o2mGetMap(Object objectA, List<?> listB) {
        return getMapByAggObjectAndInnerList(objectA, listB);
    }

    public static Map<String, Object> m2mGetMap(Object objectA, List<?> listB, Class<?> clazzObjectB, List<?> listC, Class<?> clazzObjectC) {
        return getMapByAggObjectAndInnerList(objectA, listB, clazzObjectB, listC, clazzObjectC);
    }

    public static Map<String, Object> m2mGetMap(Object objectA, List<?> listB, List<?> listC) {
        return getMapByAggObjectAndInnerList(objectA, listB, listC);
    }

    public static Map<String, Object> getMapByAggObject(Object objectA, Object objectB) {
        Map<String, Object> result = new LinkedHashMap<>();
        if (objectA != null) {
            Map<String, Object> aMap = convertObjectToMap(objectA);
            result.putAll(aMap);
        }
        if (objectB != null) {
            Map<String, Object> bMap = convertObjectToMap(objectB);
            for (Map.Entry<String, Object> entry : bMap.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                if (!result.containsKey(key)) {
                    result.put(key, value);
                }
            }
        }
        return result;
    }

    public static Map<String, Object> getMapByAggObject(Object objectA, Object objectB, Class<?> clazzObjectB) {
        try {
            if (objectB == null) {
                objectB = clazzObjectB.getDeclaredConstructor().newInstance();
            }
            return getMapByAggObject(objectA, objectB);
        } catch (Exception e) {
            throw new CreateMapException(e);
        }
    }

    public static Map<String, Object> getMapByAggObjectAndInnerList(Object objectA, List<?>... listBs) {
        Map<String, Object> aMap = new HashMap<>();
        if (objectA != null) {
            aMap = convertObjectToMap(objectA);
        }
        for (List<?> listB : listBs) {
            setListInMap(listB, aMap);
        }
        return aMap;
    }

    public static Map<String, Object> getMapByAggObjectAndInnerList(Object objectA, List<?> listB, Class<?> clazzObjectB) {
        Map<String, Object> aMap = new HashMap<>();
        if (objectA != null) {
            aMap = convertObjectToMap(objectA);
        }
        setListInMap(listB, clazzObjectB, aMap);
        return aMap;
    }

    public static Map<String, Object> getMapByAggObjectAndInnerList(Object objectA, List<?> listB, Class<?> clazzObjectB, List<?> listC, Class<?> clazzObjectC) {
        Map<String, Object> aMap = getMapByAggObjectAndInnerList(objectA, listB, clazzObjectB);
        setListInMap(listC, clazzObjectC, aMap);
        return aMap;
    }

    public static Map<String, Object> convertObjectToMap(Object obj) {
        if (obj == null) {
            return null;
        }
        Map<String, Object> map = new LinkedHashMap<>();
        Class<?> clazz = obj.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            String name = field.getName();
            try {
                Object value = field.get(obj);
                map.put(name, value);
            } catch (IllegalAccessException e) {
                throw new CreateMapException(e);
            }
        }
        return map;
    }

    private static void setListInMap(List<?> listB, Map<String, Object> aggMap) {
        if (listB != null && !listB.isEmpty()) {
            Class<?> bClass = getElementTypeFromList(listB);
            if (bClass == null) {
                return;
            }
            String bClassName = bClass.getSimpleName();
            List<Map<String, Object>> bMaps = listB.stream()
                    .map(BeanCyUtils::convertObjectToMap)
                    .collect(Collectors.toList());
            bClassName = bClassName.substring(0, 1).toLowerCase() + bClassName.substring(1) + "List";
            aggMap.put(bClassName, bMaps);
        }
    }

    private static void setListInMap(List<?> listB, Class<?> clazzObjectB, Map<String, Object> aggMap) {
        String bClassName = clazzObjectB.getSimpleName();
        bClassName = bClassName.substring(0, 1).toLowerCase() + bClassName.substring(1) + "List";
        if (listB != null && !listB.isEmpty()) {
            List<Map<String, Object>> bMaps = listB.stream()
                    .map(BeanCyUtils::convertObjectToMap)
                    .collect(Collectors.toList());
            aggMap.put(bClassName, bMaps);
        } else {
            aggMap.put(bClassName, new ArrayList<>());
        }
    }

    private static Class<?> getElementTypeFromList(List<?> list) {
        Objects.requireNonNull(list);
        for (Object obj : list) {
            if (obj != null) {
                return obj.getClass();
            }
        }
        return null;
    }
}
