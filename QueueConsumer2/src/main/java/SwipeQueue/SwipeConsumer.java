package SwipeQueue;

import Config.ConsumerConfig;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * SwipeConsumer class - used to consume swipe data from queue
 *
 * @author Cody Cao
 */
public class SwipeConsumer {
  private Map<Integer, ArrayBlockingQueue<Integer>> rightSwipeMap;
  private Connection conn;
  private ExecutorService threadPool;

  /**
   * Construct a new SwipeConsumer
   *
   * @param rightSwipeMap a ConcurrentHashMap that stores information about a user's
   */
  public SwipeConsumer(Map<Integer, ArrayBlockingQueue<Integer>> rightSwipeMap) {
    this.rightSwipeMap = rightSwipeMap;

    //Setup connection pool
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost(ConsumerConfig.QUEUE_HOST);
    factory.setVirtualHost(ConsumerConfig.VHOST_NAME);
    factory.setUsername(ConsumerConfig.USER_NAME);
    factory.setPassword(ConsumerConfig.PASSWORD);

    try {
      this.conn = factory.newConnection();
    } catch (IOException | TimeoutException e) {
      e.printStackTrace();
      System.exit(1);
    }
  }

  /**
   * Connects to the queue and starts listening for messages
   */
  public void start() {
    threadPool = Executors.newFixedThreadPool(ConsumerConfig.NUM_CONNECTIONS);
    for(int i = 0; i < ConsumerConfig.NUM_CONNECTIONS; i++){
      threadPool.execute(new ConsumerThread(conn, rightSwipeMap));
    }
  }

  /**
   * Stops listening for message and disconnects from the queue
   */
  public void stop() {
    threadPool.shutdown();
    try {
      threadPool.awaitTermination(30, TimeUnit.SECONDS);
      this.conn.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
