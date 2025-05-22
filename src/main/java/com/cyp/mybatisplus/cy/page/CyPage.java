package com.cyp.mybatisplus.cy.page;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cyp.mybatisplus.cy.functionalinterface.SFunction;
import com.cyp.mybatisplus.cy.functionalinterface.TriFunction;
import com.cyp.mybatisplus.cy.list.CyArrayList;
import com.cyp.mybatisplus.cy.list.JdbcArrayList;
import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * CyPage
 *
 * @author cyp
 * @since 2025/5/20
 */
@SuppressWarnings("all")
@Data
public class CyPage<E> {
    private Page<E> page;

    public CyPage(Page<E> page) {
        this.page = page;
    }

    public <R, R1, R2> CyPage<R2> m2o(Function<E, R> keyFunc1, Function<CyArrayList<R>, List<R1>> handlerFunc2, Function<R1, R> keyFunc3, BiFunction<E, R1, R2> resultMap4) {
        JdbcArrayList<R2> records = new JdbcArrayList<>(page.getRecords()).m2o(keyFunc1, handlerFunc2, keyFunc3, resultMap4);
        return new CyPage<>(new CyPage<R2>(new Page<>(page.getCurrent(), page.getSize(), page.getTotal())).getPage().setRecords(records));
    }

    public <R, R1, R2> CyPage<R2> m2o(Function<E, R> keyFunc1, SFunction<R1, R> keyFunc3, BiFunction<E, R1, R2> resultMap4) {
        JdbcArrayList<R2> records = new JdbcArrayList<>(page.getRecords()).m2o(keyFunc1, keyFunc3, resultMap4);
        return new CyPage<>(new CyPage<R2>(new Page<>(page.getCurrent(), page.getSize(), page.getTotal())).getPage().setRecords(records));
    }

    public <R, R1> CyPage<Map<String, Object>> m2o(Function<E, R> keyFunc1, SFunction<R1, R> keyFunc3) {
        JdbcArrayList<Map<String, Object>> records = new JdbcArrayList<>(page.getRecords()).m2o(keyFunc1, keyFunc3);
        return new CyPage<>(new CyPage<Map<String, Object>>(new Page<>(page.getCurrent(), page.getSize(), page.getTotal())).getPage().setRecords(records));
    }

    public <R, R1, R2> CyPage<R2> o2m(Function<E, R> keyFunc1, Function<CyArrayList<R>, List<R1>> handlerFunc2, Function<R1, R> keyFunc3, BiFunction<E, List<R1>, R2> resultMap4) {
        JdbcArrayList<R2> records = new JdbcArrayList<>(page.getRecords()).o2m(keyFunc1, handlerFunc2, keyFunc3, resultMap4);
        return new CyPage<>(new CyPage<R2>(new Page<>(page.getCurrent(), page.getSize(), page.getTotal())).getPage().setRecords(records));
    }

    public <R, R1, R2> CyPage<R2> o2m(Function<E, R> keyFunc1, SFunction<R1, R> keyFunc3, BiFunction<E, List<R1>, R2> resultMap4) {
        JdbcArrayList<R2> records = new JdbcArrayList<>(page.getRecords()).o2m(keyFunc1, keyFunc3, resultMap4);
        return new CyPage<>(new CyPage<R2>(new Page<>(page.getCurrent(), page.getSize(), page.getTotal())).getPage().setRecords(records));
    }

    public <R, R1> CyPage<Map<String, Object>> o2m(Function<E, R> keyFunc1, SFunction<R1, R> keyFunc3) {
        JdbcArrayList<Map<String, Object>> records = new JdbcArrayList<>(page.getRecords()).o2m(keyFunc1, keyFunc3);
        return new CyPage<>(new CyPage<Map<String, Object>>(new Page<>(page.getCurrent(), page.getSize(), page.getTotal())).getPage().setRecords(records));
    }

    public <R, R1, R2, R3> CyPage<R2> m2m(Function<E, R> keyFunc1, Function<CyArrayList<R>, List<R1>> handlerFunc2
            , Function<R1, R> keyFunc3
            , Function<R1, R> keyFunc4, Function<CyArrayList<R>, List<R3>> handlerFunc5, Function<R3, R> keyFunc6
            , TriFunction<E, CyArrayList<R1>, CyArrayList<R3>, R2> resultMap7) {
        JdbcArrayList<R2> records = new JdbcArrayList<>(page.getRecords()).m2m(keyFunc1, handlerFunc2, keyFunc3, keyFunc4, handlerFunc5, keyFunc6, resultMap7);
        return new CyPage<>(new CyPage<R2>(new Page<>(page.getCurrent(), page.getSize(), page.getTotal())).getPage().setRecords(records));
    }

    public <R, R1, R2, R3> CyPage<R2> m2m(
            Function<E, R> keyFunc1, SFunction<R1, R> keyFunc3
            , Function<R1, R> keyFunc4, SFunction<R3, R> keyFunc6
            , TriFunction<E, CyArrayList<R1>, CyArrayList<R3>, R2> resultMap7) {
        JdbcArrayList<R2> records = new JdbcArrayList<>(page.getRecords()).m2m(keyFunc1, keyFunc3, keyFunc4, keyFunc6, resultMap7);
        return new CyPage<>(new CyPage<R2>(new Page<>(page.getCurrent(), page.getSize(), page.getTotal())).getPage().setRecords(records));
    }

    public <R, R1, R3> CyPage<Map<String, Object>> m2m(
            Function<E, R> keyFunc1, SFunction<R1, R> keyFunc3
            , Function<R1, R> keyFunc4, SFunction<R3, R> keyFunc6) {
        JdbcArrayList<Map<String, Object>> records = new JdbcArrayList<>(page.getRecords()).m2m(keyFunc1, keyFunc3, keyFunc4, keyFunc6);
        return new CyPage<>(new CyPage<Map<String, Object>>(new Page<>(page.getCurrent(), page.getSize(), page.getTotal())).getPage().setRecords(records));
    }

    public <R, R1, R3> CyPage<Map<String, Object>> ezM2m(
            Function<E, R> keyFunc1, SFunction<R1, R> keyFunc3
            , Function<R1, R> keyFunc4, SFunction<R3, R> keyFunc6) {
        JdbcArrayList<Map<String, Object>> records = new JdbcArrayList<>(page.getRecords()).ezM2m(keyFunc1, keyFunc3, keyFunc4, keyFunc6);
        return new CyPage<>(new CyPage<Map<String, Object>>(new Page<>(page.getCurrent(), page.getSize(), page.getTotal())).getPage().setRecords(records));
    }

}
