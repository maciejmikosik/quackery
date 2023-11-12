package org.quackery.common;

import java.time.Duration;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public class ExecutorBuilder {
  private int corePoolSize = 0;
  private int maximumPoolSize = Integer.MAX_VALUE;
  private Duration keepAlive = Duration.ofMinutes(1);
  private Supplier<BlockingQueue<Runnable>> blockingQueueSupplier = LinkedBlockingQueue<Runnable>::new;
  private boolean allowCoreThreadTimeOut = false;

  private ExecutorBuilder() {}

  public static ExecutorBuilder executorBuilder() {
    return new ExecutorBuilder();
  }

  public ExecutorBuilder poolSize(int corePoolSize, int maximumPoolSize) {
    this.corePoolSize = corePoolSize;
    this.maximumPoolSize = maximumPoolSize;
    return this;
  }

  public ExecutorBuilder poolSize(int poolSize) {
    return poolSize(poolSize, poolSize);
  }

  public ExecutorBuilder keepAlive(Duration keepAlive) {
    this.keepAlive = keepAlive;
    return this;
  }

  public ExecutorBuilder blockingQueue(Supplier<BlockingQueue<Runnable>> blockingQueueSupplier) {
    this.blockingQueueSupplier = blockingQueueSupplier;
    return this;
  }

  public ExecutorBuilder allowCoreThreadTimeOut(boolean allowCoreThreadTimeOut) {
    this.allowCoreThreadTimeOut = allowCoreThreadTimeOut;
    return this;
  }

  public ThreadPoolExecutor build() {
    ThreadPoolExecutor executor = new ThreadPoolExecutor(
        corePoolSize, maximumPoolSize,
        keepAlive.toNanos(), TimeUnit.NANOSECONDS,
        blockingQueueSupplier.get());
    executor.allowCoreThreadTimeOut(allowCoreThreadTimeOut);
    return executor;
  }
}
