package org.jgodeye.common;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class NamedThreadFactory implements ThreadFactory {

    private final String threadNamePrefix;
    private AtomicInteger counter = new AtomicInteger();

    public NamedThreadFactory(String threadNamePrefix) {
        this.threadNamePrefix = threadNamePrefix;
    }

    @Override
    public Thread newThread(Runnable runnable) {
        Thread thread = new Thread(runnable, threadNamePrefix + "-" + counter.incrementAndGet());
        thread.setPriority(Thread.MIN_PRIORITY);
        return thread;
    }
}
