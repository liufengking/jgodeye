package com.lf65.mt;

import com.lf65.util.StringUtils;

public class MethodTraceStack {
    
    private static final ThreadLocal<String> preTabsThreadLocal = new ThreadLocal<>();
    
    public static void log(String s) {
        MethodTraceLogger.log(getPreTabs() + "- " + s + "\n");
    }
    
    public static void push() {
        preTabsThreadLocal.set(getPreTabs() + "\t");
    }
    
    public static void pop() {
        String preTabs = getPreTabs();
        if (preTabs.length() > 1) {
            preTabsThreadLocal.set(preTabs.substring(1));
        }
    }
    
    private static String getPreTabs() {
        String preTabs = preTabsThreadLocal.get();
        if (null == preTabs) {
            preTabs = StringUtils.EMPTY;
        }
        return preTabs;
    }
}
