package Client;

import Analyzer.Analyzer;
import Analyzer.Writer;
import Config.ClientConfig;
import Request.RequestOutput;
import Request.RequestThread;
import Analyzer.OutputGenerator;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.async.CloseableHttpAsyncClient;
import org.apache.hc.client5.http.impl.async.HttpAsyncClients;
import org.apache.hc.client5.http.impl.nio.PoolingAsyncClientConnectionManager;

public class Client2 {
  public static void main(String[] args){
    //Set up variables
    ExecutorService threadPool = Executors.newFixedThreadPool(ClientConfig.NUM_THREADS);

    AtomicInteger successCounter = new AtomicInteger(0);
    AtomicInteger failCounter = new AtomicInteger(0);
    ConcurrentLinkedQueue<RequestOutput> writeQueue = new ConcurrentLinkedQueue<>();

    CountDownLatch latch = new CountDownLatch(ClientConfig.REQUEST_NUM);

    PoolingAsyncClientConnectionManager cm = new PoolingAsyncClientConnectionManager();
    cm.setMaxTotal(ClientConfig.MAX_TOTAL_CONN);
    cm.setDefaultMaxPerRoute(ClientConfig.MAX_PER_ROUTE);

    RequestConfig requestConfig = RequestConfig.custom()
        //.setResponseTimeout(3000, TimeUnit.MILLISECONDS)
        .build();

    CloseableHttpAsyncClient httpClient = HttpAsyncClients.custom()
        .setDefaultRequestConfig(requestConfig)
        .setConnectionManager(cm)
        .build();
    httpClient.start();

    //Start the counter
    long start = System.currentTimeMillis();

    Thread writeThread = new Thread(
        new Writer(writeQueue, Writer.CONCURRENT_FILE_NAME_2, ClientConfig.NUM_THREADS));
    writeThread.start();

    //Start sending requests
    for(int i = 0; i < ClientConfig.REQUEST_NUM; i++){
      threadPool.execute(new RequestThread(
          successCounter,
          failCounter,
          writeQueue,
          latch,
          httpClient
      ));
    }

    //Wait for all threads to finish execution
    try {
      latch.await();
      writeThread.join();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    long end = System.currentTimeMillis();

    //Shut down connection pool & thread pool
    try {
      cm.close();
      threadPool.shutdown();
      threadPool.awaitTermination(1000, TimeUnit.MILLISECONDS);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }

    int runTime = (int) TimeUnit.MILLISECONDS.toSeconds(end - start);

    //Analyze and generate output
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
