package com.lf65.jgodeye.trace.core;


import com.lf65.jgodeye.common.StringUtils;

public class PreTabStack {
    
    private static final ThreadLocal<String> preTabsThreadLocal = new ThreadLocal<>();
    
    public static void log(String s) {
        Logger.log(getPreTabs() + "- " + s + "\n");
    }
    
    public static void push() {
        preTabsThreadLocal.set(getPreTabs() + "    ");
    }
    
    public static void pop() {
        String preTabs = getPreTabs();
        if (preTabs.length() > 4) {
            preTabsThreadLocal.set(preTabs.substring(4));
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
