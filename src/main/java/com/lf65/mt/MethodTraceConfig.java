package com.lf65.mt;

import com.lf65.util.AssertUtils;

import java.util.HashMap;
import java.util.Map;

public class MethodTraceConfig {
    public static final String ARGS_START_WITH = "startWith";
    public static final String ARGS_OUT_PUT = "outPut";
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
    
    public static String getArgsOutPut() {
        return configs.get(ARGS_OUT_PUT);
    }
}
