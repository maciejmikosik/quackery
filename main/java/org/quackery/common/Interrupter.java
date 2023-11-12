package org.quackery.common;

import static java.util.concurrent.TimeUnit.NANOSECONDS;
import static org.quackery.common.ExecutorBuilder.executorBuilder;

import java.time.Duration;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

public class Interrupter {
  private final Executor executor;

  private Interrupter(Executor executor) {
    this.executor = executor;
  }

  public static Interrupter interrupter(Executor executor) {
    return new Interrupter(executor);
  }

  public static Interrupter interrupter() {
    return interrupter(executorBuilder()
        .poolSize(0, 1)
        .keepAlive(Duration.ofNanos(1))
        .allowCoreThreadTimeOut(true)
        .build());
  }

  public Future<Void> interrupt(Duration delay, Thread thread) {
    FutureTask<Void> futureTask = new FutureTask<Void>(() -> {
      try {
        NANOSECONDS.sleep(delay.toNanos());
        thread.interrupt();
      } catch (InterruptedException e) {}
      return null;
    });
    executor.execute(futureTask);
    return futureTask;
  }

  public Future<Void> interruptMe(Duration delay) {
    return interrupt(delay, Thread.currentThread());
  }
}
