package com.lf65.mt;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.LinkedBlockingQueue;

import com.lf65.model.Pair;
import com.lf65.util.AssertUtils;
import com.lf65.util.ExceptionQuietly;

public class MethodTraceLogger {

    private static final LinkedBlockingQueue<Pair<String,String>> logBuffer =
            new LinkedBlockingQueue<>(10000);

    public static void initLogger() {
        initOutPut();
        initConsumerLogTask();
    }

    public static void initOutPut() {
        String outPut = MethodTraceConfig.getArgsOutPut();
        AssertUtils.notBlank(outPut);
        File file = new File(outPut);
        if (!file.exists()) {
            AssertUtils.state(file.mkdir());
            return;
        }
        File[] subFiles = file.listFiles();
        if (subFiles == null || subFiles.length <= 0) {
            return;
        }
        for (File subFile : subFiles) {
            AssertUtils.state(subFile.delete());
        }
    }

    public static void log(String s) {
        String threadName = Thread.currentThread().getName();
        ExceptionQuietly.call(() -> logBuffer.put(new Pair<>(threadName, s)));
    }

    public static void ensureFile(Path path) throws IOException {
        if (Files.isWritable(path)) {
            return;
        }
        Files.createFile(path);
    }
    
    private static void initConsumerLogTask() {
        new Thread(()->{
            while (true) {
                ExceptionQuietly.call(() -> {
                    Pair<String, String> pair = logBuffer.take();
                    log0(pair.getObject1(), pair.getObject2());
                });
            }
        }).start();
    }

    private static void log0(String threadName, String s) throws Exception {
        String outPut = MethodTraceConfig.getArgsOutPut();
        AssertUtils.notBlank(outPut);
        Path path = Paths.get(outPut + "/" + threadName + ".md");
        ensureFile(path);
        Files.write(path, s.getBytes(StandardCharsets.UTF_8), StandardOpenOption.APPEND);
    }
}
