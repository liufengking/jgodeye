package org.jgodeye.trace.core;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.*;
import org.jgodeye.common.Constants;
import org.jgodeye.common.ExceptionQuietly;
import org.jgodeye.common.StringUtils;

public class Context {

    private static final Map<String, String> configs = new HashMap<>();
    private static final List<String> pacakgeExcludeList = new ArrayList<>();

    public static void initContext(String jgodeyeHome) {
        initArgsJgodeyeHome(jgodeyeHome);
        initJgodeyeConf();
        initPackageExcludes();
    }

    private static void initJgodeyeConf() {
        ExceptionQuietly.call(() -> {
            InputStream inputStream = new FileInputStream(getJgodeyeHome() + Constants.JGODEYE_CONF);
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
        configs.put(Constants.ARGS_JGODEYE_HOME, home);
    }

    private static void initPackageExcludes() {
        String packageExcludes = configs.get(Constants.JGODEYE_TRACE_EXCLUDES);
        if (StringUtils.isBlank(packageExcludes)) {
            return;
        }
        pacakgeExcludeList.addAll(Arrays.asList(packageExcludes.split(",")));
        pacakgeExcludeList.add(Constants.JGODEYE_TRACE_PACKAGE_START);
    }

    public static String getJgodeyeHome() {
        return configs.get(Constants.ARGS_JGODEYE_HOME);
    }

    public static String getJgodeyeTraceMatches() {
        return configs.get(Constants.JGODEYE_TRACE_MATCHES);
    }

    public static String getArgsOutPut() {
        return getJgodeyeHome() + "outPut";
    }

    public static List<String> getPacakgeExcludes() {
        return pacakgeExcludeList;
    }
}
