package MultiThread;

import Config.ClientConfig;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Main class used to test API
 */
public class APITest {

  public static final int NUM_THREADS = 12;
  public static final int WAIT_TIME = 10;

  public static void main(String[] args) {
    long start = System.currentTimeMillis();
    ExecutorService threadPool = Executors.newFixedThreadPool(NUM_THREADS);

    AtomicInteger successCounter = new AtomicInteger(0);
    AtomicInteger failCounter = new AtomicInteger(0);
    ArrayBlockingQueue<RequestThread> processingQueue = new ArrayBlockingQueue<>(NUM_THREADS);

    //Create threads in thread pool
    for (int i = 0; i < NUM_THREADS; i++) {
      threadPool.submit(new RequestThread(successCounter, failCounter, processingQueue));
    }

    //Add processing pills to tell the threads to run
    for (int i = 0; i < ClientConfig.REQUEST_NUM; i++) {
      try {
        processingQueue.offer(RequestThread.PROCESS_PILL, WAIT_TIME, TimeUnit.SECONDS);
      } catch (InterruptedException e) {
        System.err.println(e);
      }
    }

    //Add poison pills to terminate threads
    for (int i = 0; i < NUM_THREADS; i++) {
      try {
        processingQueue.offer(RequestThread.POISON_PILL, WAIT_TIME, TimeUnit.SECONDS);
      } catch (InterruptedException e) {
        System.err.println(e);
      }
    }

    //Shutdown thread pool
    threadPool.shutdown();
    try {
      threadPool.awaitTermination(3, TimeUnit.MINUTES);
    } catch (InterruptedException e) {
      System.err.println(e);
    }

    long end = System.currentTimeMillis();
    long runTime = TimeUnit.MILLISECONDS.toSeconds(end - start);
    double throughput = 1.0 * ClientConfig.REQUEST_NUM / runTime;

    StringBuilder sb = new StringBuilder()
        .append(successCounter.get()).append(" requests succeeded").append(System.lineSeparator())
        .append(failCounter.get()).append(" requests failed").append(System.lineSeparator())
        .append("Took ").append(runTime).append(" seconds.").append(System.lineSeparator())
        .append("Total throughput: ").append(throughput).append(" requests/second")
        .append(System.lineSeparator());

    System.out.println(sb.toString());
  }
}