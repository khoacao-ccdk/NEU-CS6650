package SwipeQueue;

import Config.ConsumerConfig;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * SwipeConsumer class - used to consume swipe data from queue
 *
 * @author Cody Cao
 */
public class SwipeConsumer {

  private Connection conn;
  private ExecutorService threadPool;

  /**
   * Construct a new SwipeConsumer
   */
  public SwipeConsumer() {

    //Setup connection pool
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost(ConsumerConfig.QUEUE_HOST);
    factory.setPort(ConsumerConfig.QUEUE_PORT);
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
    for (int i = 0; i < ConsumerConfig.NUM_CONNECTIONS; i++) {
      threadPool.execute(new ConsumerThread(conn));
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
