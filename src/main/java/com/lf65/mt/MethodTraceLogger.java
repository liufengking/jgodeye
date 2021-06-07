package com.lf65.mt;

import com.lf65.util.AssertUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class MethodTraceLogger {
    
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
        try {
            String outPut = MethodTraceConfig.getArgsOutPut();
            AssertUtils.notBlank(outPut);
            Path path = Paths.get(outPut + "/" + Thread.currentThread().getName() + ".md");
            ensureFile(path);
            Files.write(path, s.getBytes(StandardCharsets.UTF_8), StandardOpenOption.APPEND);
        } catch (Exception e) {
        
        }
    }
    
    public static void ensureFile(Path path) throws IOException {
        if (Files.isWritable(path)) {
            return;
        }
        Files.createFile(path);
    }
}
