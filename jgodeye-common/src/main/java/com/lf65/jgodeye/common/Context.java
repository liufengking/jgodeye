package com.lf65.jgodeye.common;

import java.util.HashMap;
import java.util.Map;

public class Context {
    private static final String ARGS_JGODEYE_HOME = "jgodeyeHome";
    public static final String ARGS_START_WITH = "startWith";
    public static final String OUT_PUT = "outPut";
    private static final Map<String, String> configs = new HashMap<>();
    
    public static void initConfig(String agentArgs) {
        AssertUtils.notBlank(agentArgs);
        String[] argsArrs = agentArgs.split(",");
        for (String item : argsArrs) {
            String[] kvs = item.split(":");
            if (kvs.length > 1) {
                configs.put(kvs[0], kvs[1]);
            }
        }
    }
    
    public static String getArgsStartWith() {
        return configs.get(ARGS_START_WITH);
    }
    
    public static String getJgodeyeHome() {
        return configs.get(ARGS_JGODEYE_HOME);
    }
    
    public static String getArgsOutPut() {
        String outPut = getJgodeyeHome();
        if (!outPut.endsWith("/")) {
            outPut += "/";
        }
        return outPut + OUT_PUT;
    }
}
