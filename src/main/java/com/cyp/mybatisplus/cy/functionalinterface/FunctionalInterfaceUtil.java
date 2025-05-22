package com.cyp.mybatisplus.cy.functionalinterface;

import com.cyp.mybatisplus.cy.exception.LambdaParsingException;

import java.lang.reflect.Method;

/**
 * FunctionalInterfaceUtil
 *
 * @author cyp
 * @since 2025/5/20
 */
public class FunctionalInterfaceUtil {
    public static Method getLambdaInnerMethod(SFunction<?, ?> function) {
        try {
            Method replaceMethod = function.getClass().getDeclaredMethod("writeReplace");
            replaceMethod.setAccessible(true);
            Object serializedLambda = replaceMethod.invoke(function);
            Class<?> lambdaClass = serializedLambda.getClass();
            String implMethodName = (String) lambdaClass.getMethod("getImplMethodName").invoke(serializedLambda);
            String implClassName = (String) lambdaClass.getMethod("getImplClass").invoke(serializedLambda);
            String ownerClassName = implClassName.replace("/", ".");
            Class<?> targetClass = Class.forName(ownerClassName);
            return targetClass.getDeclaredMethod(implMethodName);
        } catch (Exception e) {
            throw new LambdaParsingException(e);
        }
    }
}
