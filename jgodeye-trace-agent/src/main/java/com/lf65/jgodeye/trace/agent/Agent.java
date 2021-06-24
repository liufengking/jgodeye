package com.lf65.jgodeye.trace.agent;

import java.io.File;
import java.lang.instrument.Instrumentation;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;

public class Agent {
    
    private static final String ARGS_JGODEYE_HOME = "jgodeyeHome";
    private static final String JGODEYE_TRACE_CORE_BOOTSTRAP = "com.lf65.jgodeye.trace.core.Bootstrap";
    private static final String JGODEYE_TRACE_CORE_JAR = "jgodeye-trace-core.jar";
    
    private static final Map<String, String> configs = new HashMap<>();
    
    public static void premain(String agentArgs, Instrumentation inst) {
        try {
            initConfig(agentArgs);
    
            ClassLoader classLoader = ClassLoader.getSystemClassLoader().getParent();
            String jgodeyeHome = configs.get(ARGS_JGODEYE_HOME);
            loadCoreJar(classLoader, jgodeyeHome);
            bind(classLoader, jgodeyeHome, inst);
            
        } catch (Exception e) {
            // ignore
            e.printStackTrace();
        }
    }
    
    private static void initConfig(String argentArgs) {
        if (argentArgs == null || argentArgs.length() == 0) {
            throw new IllegalArgumentException();
        }
        String[] argsArrs = argentArgs.split(",");
        for (String item : argsArrs) {
            String[] kvs = item.split(":");
            if (kvs.length > 1) {
                configs.put(kvs[0], kvs[1]);
            }
        }
    }
    
    private static void loadCoreJar(ClassLoader classLoader, String jgodeyeHome) throws Exception {
        String jarsPath = jgodeyeHome;
        if (!jarsPath.endsWith("/")) {
            jarsPath += "/";
        }
        File file = new File(jarsPath + JGODEYE_TRACE_CORE_JAR);
        if (!file.exists()) {
            throw new RuntimeException("core jar not exist");
        }
        loadJarFile(classLoader, file);
    }
    
    private static void loadJarFile(ClassLoader classLoader, File path) throws Exception {
        URL url = path.toURI().toURL();
        Method method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
        boolean accessible = method.isAccessible();
        if(!accessible) {
            method.setAccessible(true);
        }
        method.invoke(classLoader, url);
        method.setAccessible(accessible);
    }
    
    private static void bind(ClassLoader classLoader, String jgodeyeHome, Instrumentation inst) throws Exception {
        Class<?> bootstrapClass = classLoader.loadClass(JGODEYE_TRACE_CORE_BOOTSTRAP);
        Method method = bootstrapClass.getDeclaredMethod("bind", String.class, Instrumentation.class);
        boolean accessible = method.isAccessible();
        if (!accessible) {
            method.setAccessible(true);
        }
        method.invoke(classLoader, jgodeyeHome, inst);
        method.setAccessible(accessible);
    }
}
