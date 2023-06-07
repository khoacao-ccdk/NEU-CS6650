package MultiThread;

import Config.ClientConfig;
import Output.Analyzer;
import Output.RequestOutput;
import Output.Writer;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
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
    ExecutorService threadPool = Executors.newFixedThreadPool(NUM_THREADS);

    AtomicInteger successCounter = new AtomicInteger(0);
    AtomicInteger failCounter = new AtomicInteger(0);
    ArrayBlockingQueue<RequestThread> processingQueue = new ArrayBlockingQueue<>(NUM_THREADS);
    ConcurrentLinkedQueue<RequestOutput> writeQueue = new ConcurrentLinkedQueue<>();

    long start = System.currentTimeMillis();
    //Create threads in thread pool
    for (int i = 0; i < NUM_THREADS; i++) {
      threadPool.submit(new RequestThread(
          successCounter,
          failCounter,
          processingQueue,
          writeQueue
      ));
    }

    Thread writeThread = new Thread(
        new Writer(writeQueue, Writer.CONCURRENT_FILE_NAME, NUM_THREADS));
    writeThread.start();

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
      e.printStackTrace();
    }
    long end = System.currentTimeMillis();

    //Close the writer thread
    try {
      writeThread.join();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    Analyzer analyzer = new Analyzer(Writer.CONCURRENT_FILE_NAME);
    try {
      analyzer.analyze();
    } catch (Exception e) {
      e.printStackTrace();
    }

    int runTime = (int) TimeUnit.MILLISECONDS.toSeconds(end - start);
    StringBuilder sb = new StringBuilder()
        .append(successCounter.get()).append(" requests succeeded").append(System.lineSeparator())
        .append(failCounter.get()).append(" requests failed").append(System.lineSeparator())
        .append("Took ").append(runTime).append(" seconds.").append(System.lineSeparator())
        .append("Min latency: ").append(analyzer.getMin()).append(" milliseconds")
        .append(System.lineSeparator())
        .append("Max latency: ").append(analyzer.getMax()).append(" milliseconds")
        .append(System.lineSeparator())
        .append("Mean latency: ").append(analyzer.getMean()).append(" milliseconds")
        .append(System.lineSeparator())
        .append("Median latency: ").append(analyzer.getMedian()).append(" milliseconds")
        .append(System.lineSeparator())
        .append("99th percentile latency: ").append(analyzer.getPercentile99())
        .append(" milliseconds").append(System.lineSeparator())
        .append("Total throughput: ").append(analyzer.getThroughput()).append(" requests/second")
        .append(System.lineSeparator());

    System.out.println(sb.toString());
  }
}