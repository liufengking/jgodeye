package com.lf65.jgodeye.trace.core;

import com.lf65.jgodeye.common.AssertUtils;
import com.lf65.jgodeye.common.Context;
import com.lf65.jgodeye.common.ExceptionQuietly;
import com.lf65.jgodeye.common.Pair;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.LinkedBlockingQueue;

public class Logger {

    private static final LinkedBlockingQueue<Pair<String,String>> logBuffer =
            new LinkedBlockingQueue<>(10000);

    public static void initLogger() {
        initOutPut();
        initConsumerLogTask();
    }

    public static void initOutPut() {
        String outPut = Context.getArgsOutPut();
        AssertUtils.notBlank(outPut);
        File file = new File(outPut);
        if (!file.exists()) {
            AssertUtils.state(file.mkdirs());
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
        Thread loggerConsumerThread = new Thread(() -> {
            while (true) {
                ExceptionQuietly.call(() -> {
                    Pair<String, String> pair = logBuffer.take();
                    log0(pair.getObject1(), pair.getObject2());
                });
            }
        });
        loggerConsumerThread.setDaemon(true);
        loggerConsumerThread.start();
    }

    private static void log0(String threadName, String s) throws Exception {
        String outPut = Context.getArgsOutPut();
        AssertUtils.notBlank(outPut);
        Path path = Paths.get(outPut + "/" + threadName + ".md");
        ensureFile(path);
        Files.write(path, s.getBytes(StandardCharsets.UTF_8), StandardOpenOption.APPEND);
    }
}
