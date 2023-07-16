package MessageQueue;

import Config.ServerConfig;
import RequestBody.SwipeRequest;
import com.google.gson.Gson;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;

public class ProducerThread implements Runnable {

  private SwipeRequest swipeInfo;
  private BlockingQueue<Channel> connectionPool;

  /**
   * Csontructs a new ProducerThread to send messages
   *
   * @param swipeInfo      a SwipeRequest object that stores information about a swipe
   * @param connectionPool a BlockingQueue that stores information of channels
   */
  public ProducerThread(SwipeRequest swipeInfo, BlockingQueue<Channel> connectionPool) {
    this.swipeInfo = swipeInfo;
    this.connectionPool = connectionPool;
  }

  @Override
  public void run() {
    try {
      byte[] message = new Gson()
          .toJson(swipeInfo)
          .getBytes("UTF-8");

      //Poll the connection from connection pool
      Channel channel = connectionPool.poll();

      //Public message to the 2 queues
      channel.basicPublish("", ServerConfig.QUEUE_2_NAME, null, message);
      channel.basicPublish("", ServerConfig.QUEUE_1_NAME, null, message);

      //Put the connection back to the pool when finished
      connectionPool.offer(channel);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
