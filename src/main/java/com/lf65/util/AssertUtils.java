package com.lf65.util;

public class AssertUtils {
    
    public static void notBlank(String text) {
        if (StringUtils.isBlank(text)) {
            throw new IllegalArgumentException("args is blank");
        }
    }
    
    public static void state(boolean expression) {
        if(!expression) {
            throw new RuntimeException("expression is false");
        }
    }
}
