package com.lf65.jgodeye.common;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.*;

public class Context {
    
    private static final String ARGS_JGODEYE_HOME = "jgodeyeHome";
    private static final String JGODEYE_TRACE_STARTWITH = "jgodeye.trace.package.startWith";
    private static final String JGODEYE_TRACE_EXCLUDES = "jgodeye.trace.package.excludes";
    private static final String JGODEYE_CONF = "jgodeye.properties";
    private static final String JGODEYE_TRACE_PACKAGE_START = "com.lf65.jgodeye.trace";
    
    private static final Map<String, String> configs = new HashMap<>();
    private static final List<String> pacakgeExcludeList = new ArrayList<>();
    
    public static void initContext(String jgodeyeHome) {
        initArgsJgodeyeHome(jgodeyeHome);
        initJgodeyeConf();
        initPackageExcludes();
    }
    
    private static void initJgodeyeConf() {
        ExceptionQuietly.call(() -> {
            InputStream inputStream = new FileInputStream(getJgodeyeHome() + JGODEYE_CONF);
            Properties prop = new Properties();
            prop.load(inputStream);
            configs.putAll((Map) prop);
        });
    }
    
    private static void initArgsJgodeyeHome(String jgodeyeHome) {
        String home = jgodeyeHome;
        if (!home.endsWith("/")) {
            home += "/";
        }
        configs.put(ARGS_JGODEYE_HOME, home);
    }
    
    private static void initPackageExcludes() {
        String packageExcludes = configs.get(JGODEYE_TRACE_EXCLUDES);
        if (StringUtils.isBlank(packageExcludes)) {
            return;
        }
        pacakgeExcludeList.addAll(Arrays.asList(packageExcludes.split(",")));
        pacakgeExcludeList.add(JGODEYE_TRACE_PACKAGE_START);
    }
    
    public static String getJgodeyeHome() {
        return configs.get(ARGS_JGODEYE_HOME);
    }
    
    public static String getJgodeyeTraceStartwith() {
        return configs.get(JGODEYE_TRACE_STARTWITH);
    }
    
    public static String getArgsOutPut() {
        return getJgodeyeHome() + "outPut";
    }
    
    public static List<String> getPacakgeExcludes() {
        return pacakgeExcludeList;
    }
}
