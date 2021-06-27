package org.jgodeye.trace.core;

import org.jgodeye.common.*;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.*;

public class Logger {

    private static final ScheduledExecutorService threadTraceInfoFluser =
        new ScheduledThreadPoolExecutor(1, new DaemonThreadFactory("jgodeye-thread-trace-info-fluser"));

    private static final LinkedBlockingQueue<Pair<String, String>> logBuffer =
        new LinkedBlockingQueue<>(10000);

    private static final Map<String, StringBuilder> threadTraceInfoBuffers = new ConcurrentHashMap<>();

    public static void initLogger() {
        initOutPut();
        initConsumerLogTask();
        initFlushLogTask();
    }

    public static void log(String s) {
        String threadName = Thread.currentThread().getName();
        ExceptionQuietly.call(() -> logBuffer.put(new Pair<>(threadName, s)));
    }

    private static void initOutPut() {
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

    private static void initConsumerLogTask() {
        Thread loggerConsumerThread = new Thread(() -> {
            while (true) {
                ExceptionQuietly.call(() -> {
                    Pair<String, String> pair = logBuffer.take();
                    appendToThreadTraceInfoBuffer(pair.getObject1(), pair.getObject2());
                });
            }
        });
        loggerConsumerThread.setName("jgodeye-thread-trace-info-consuer");
        loggerConsumerThread.setDaemon(true);
        loggerConsumerThread.start();
    }

    private static void initFlushLogTask() {
        threadTraceInfoFluser.scheduleWithFixedDelay(() -> {
            Set<Map.Entry<String, StringBuilder>> entries = threadTraceInfoBuffers.entrySet();
            for (Map.Entry<String, StringBuilder> entry : entries) {
                flushLog(entry.getKey(), entry.getValue());
            }
        }, 0, 1, TimeUnit.SECONDS);
    }

    private static void appendToThreadTraceInfoBuffer(String threadName, String line) {
        StringBuilder threadTraceInfoBuffer = threadTraceInfoBuffers.get(threadName);
        if (null == threadTraceInfoBuffer) {
            threadTraceInfoBuffer = new StringBuilder();
            threadTraceInfoBuffers.put(threadName, threadTraceInfoBuffer);
        }
        threadTraceInfoBuffer.append(line);
        if (threadTraceInfoBuffer.length() > 2048) {
            flushLog(threadName, threadTraceInfoBuffer);
        }
    }

    private static void flushLog(String threadName, StringBuilder line) {
        ExceptionQuietly.call(() -> {
            String outPut = Context.getArgsOutPut();
            AssertUtils.notBlank(outPut);
            Path path = Paths.get(outPut + "/" + threadName + ".md");
            ensureFile(path);
            Files.write(path, line.toString().getBytes(StandardCharsets.UTF_8), StandardOpenOption.APPEND);
        });
    }

    private static void ensureFile(Path path) throws IOException {
        if (Files.isWritable(path)) {
            return;
        }
        Files.createFile(path);
    }
}
