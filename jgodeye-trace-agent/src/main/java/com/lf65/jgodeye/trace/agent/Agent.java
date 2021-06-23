package com.lf65.jgodeye.trace.agent;

import com.lf65.jgodeye.common.Context;

import java.io.File;
import java.lang.instrument.Instrumentation;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Objects;

public class Agent {
    
    private static final String JGODEYE_TRACE_CORE_BOOTSTRAP = "com.lf65.jgodeye.trace.core.Bootstrap";
    
    public static void premain(String agentArgs, Instrumentation inst) {
        try {
            Context.initConfig(agentArgs);
    
            ClassLoader classLoader = ClassLoader.getSystemClassLoader().getParent();
            loadJars(classLoader, Context.getJgodeyeHome());
            bind(classLoader, agentArgs, inst);
        } catch (Exception e) {
            // ignore
            e.printStackTrace();
        }
    }
    
    private static void loadJars(ClassLoader classLoader, String jgodeyeHome) throws Exception {
        String jarsPath = jgodeyeHome;
        if (!jarsPath.endsWith("/")) {
            jarsPath += "/";
        }
        File file = new File(jarsPath + "libs");
        if (!file.exists() || !file.isDirectory()) {
            throw new RuntimeException();
        }
        for (File jarFile : Objects.requireNonNull(file.listFiles())) {
            if (jarFile.getName().endsWith(".jar")) {
                loadJarFile(classLoader, jarFile);
            }
        }
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
    
    private static void bind(ClassLoader classLoader, String agentArgs, Instrumentation inst) throws Exception {
        Class<?> bootstrapClass = classLoader.loadClass(JGODEYE_TRACE_CORE_BOOTSTRAP);
        Method method = bootstrapClass.getDeclaredMethod("bind", String.class, Instrumentation.class);
        boolean accessible = method.isAccessible();
        if (!accessible) {
            method.setAccessible(true);
        }
        method.invoke(classLoader, agentArgs, inst);
        method.setAccessible(accessible);
    }
}
