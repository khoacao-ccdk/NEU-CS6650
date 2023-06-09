package MultiThreadPart2;

import Common.RequestThread;
import Config.ClientConfig;
import Common.Analyzer;
import Common.OutputGenerator;
import Common.RequestOutput;
import Common.Writer;
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
  public static final int WAIT_TIME = 10;

  public static void main(String[] args) {
    ExecutorService threadPool = Executors.newFixedThreadPool(ClientConfig.NUM_THREADS);

    AtomicInteger successCounter = new AtomicInteger(0);
    AtomicInteger failCounter = new AtomicInteger(0);
    ArrayBlockingQueue<RequestThread> processingQueue = new ArrayBlockingQueue<>(ClientConfig.NUM_THREADS);
    ConcurrentLinkedQueue<RequestOutput> writeQueue = new ConcurrentLinkedQueue<>();

    long start = System.currentTimeMillis();
    //Create threads in thread pool
    for (int i = 0; i < ClientConfig.NUM_THREADS; i++) {
      threadPool.submit(new RequestThread(
          successCounter,
          failCounter,
          processingQueue,
          writeQueue
      ));
    }

    Thread writeThread = new Thread(
        new Writer(writeQueue, Writer.CONCURRENT_FILE_NAME_2, ClientConfig.NUM_THREADS));
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
    for (int i = 0; i < ClientConfig.NUM_THREADS; i++) {
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

    int runTime = (int) TimeUnit.MILLISECONDS.toSeconds(end - start);
    Analyzer analyzer = new Analyzer(Writer.CONCURRENT_FILE_NAME_2);
    try {
      analyzer.analyze();
    } catch (Exception e) {
      e.printStackTrace();
    }

    OutputGenerator output = new OutputGenerator(
        analyzer,
        successCounter.get(),
        failCounter.get(),
        runTime);
    System.out.println(output.getOuput());
  }
}